package com.ruoyi.system.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.ITobAlertReceiveService;
import com.ruoyi.system.service.ITobAlertTaskService;
import com.ruoyi.system.service.ITobAlertTaskLogService;
import com.ruoyi.system.service.ITobCameraRegionService;
import com.ruoyi.system.service.ITobRegionPersonService;
import com.ruoyi.system.service.ITobWxworkConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 预警数据接收Service业务层处理
 * 核心流程：接收JSON数据 → 查trafficdata → 写接收表 → 创建任务 → 企微推送
 */
@Service
public class TobAlertReceiveServiceImpl implements ITobAlertReceiveService {

    private static final Logger log = LoggerFactory.getLogger(TobAlertReceiveServiceImpl.class);

    @Autowired
    private TobAlertReceiveMapper tobAlertReceiveMapper;

    @Autowired
    private TrafficDataMapper trafficDataMapper;

    @Autowired
    private TobCameraRegionMapper tobCameraRegionMapper;

    @Autowired
    private ITobAlertTaskService tobAlertTaskService;

    @Autowired
    private ITobAlertTaskLogService tobAlertTaskLogService;

    @Autowired
    private ITobRegionPersonService tobRegionPersonService;

    @Autowired
    private WxworkPushServiceImpl wxworkPushService;

    @Autowired
    private ITobWxworkConfigService wxworkConfigService;

    @Override
    public TobAlertReceive selectTobAlertReceiveById(Long id) {
        return tobAlertReceiveMapper.selectTobAlertReceiveById(id);
    }

    @Override
    public TobAlertReceive selectTobAlertReceiveByOriginalId(Long originalId) {
        return tobAlertReceiveMapper.selectTobAlertReceiveByOriginalId(originalId);
    }

    @Override
    public List<TobAlertReceive> selectTobAlertReceiveList(TobAlertReceive tobAlertReceive) {
        return tobAlertReceiveMapper.selectTobAlertReceiveList(tobAlertReceive);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long receiveAlert(Long originalId, String reason) {
        // 1. 防重校验
        TobAlertReceive existReceive = tobAlertReceiveMapper.selectTobAlertReceiveByOriginalId(originalId);
        if (existReceive != null) {
            log.warn("预警数据已存在，originalId={}", originalId);
            return existReceive.getId();
        }

        // 2. 查询trafficdata
        TrafficData trafficData = trafficDataMapper.selectById(originalId);
        if (trafficData == null) {
            log.error("trafficdata中未找到数据，originalId={}", originalId);
            throw new RuntimeException("trafficdata中未找到数据，originalId=" + originalId);
        }

        // 3. 写入接收表
        TobAlertReceive receive = new TobAlertReceive();
        receive.setReceiveNo("R" + DateUtil.format(new Date(), "yyyyMMdd") + IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase());
        receive.setOriginalId(originalId);
        receive.setReason(reason);
        receive.setTrafficId(trafficData.getId());
        receive.setPlate(trafficData.getPlate());
        receive.setCameraId(trafficData.getCameraId());
        receive.setCameraName(trafficData.getCameraName());
        if (trafficData.getCaptureTime() != null) {
            receive.setCaptureTime(DateUtil.parse(trafficData.getCaptureTime()));
        }
        receive.setProcessStatus("0");
        tobAlertReceiveMapper.insertTobAlertReceive(receive);
        log.info("预警数据接收成功，receiveId={}, originalId={}", receive.getId(), originalId);

        // 4. 卡口→地域映射
        TobCameraRegion cameraRegion = null;
        if (trafficData.getCameraId() != null) {
            cameraRegion = tobCameraRegionMapper.selectTobCameraRegionByCameraId(trafficData.getCameraId());
        }
        if (cameraRegion == null) {
            log.error("卡口未绑定地域，cameraId={}", trafficData.getCameraId());
            throw new RuntimeException("卡口未绑定地域，cameraId=" + trafficData.getCameraId());
        }

        // 5. 创建任务
        TobAlertTask task = new TobAlertTask();
        task.setTaskNo("T" + DateUtil.format(new Date(), "yyyyMMdd") + IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase());
        task.setOriginalId(originalId);
        task.setReceiveId(receive.getId());
        task.setLocationId(cameraRegion.getLocationId());
        task.setPlate(trafficData.getPlate());
        task.setCameraName(trafficData.getCameraName());
        if (trafficData.getCaptureTime() != null) {
            task.setCaptureTime(DateUtil.parse(trafficData.getCaptureTime()));
        }
        task.setReason(reason);
        task.setStatus(0);
        task.setAssignIndex(0);
        task.setPushedToAdmin(0);
        task.setExpireTime(DateUtil.offsetHour(new Date(), 2));
        task.setCreateBy("system");
        tobAlertTaskService.insertTobAlertTask(task);
        log.info("预警任务创建成功，taskId={}, taskNo={}", task.getId(), task.getTaskNo());

        // 6. 查询绑定人员并推送
        List<TobRegionPerson> personList = tobRegionPersonService.selectTobRegionPersonByLocationId(cameraRegion.getLocationId());
        if (personList == null || personList.isEmpty()) {
            // 无绑定人员，尝试推管理员
            log.warn("地域{}无绑定人员，尝试推送管理员", cameraRegion.getLocationId());
            pushToAdmin(task, "地域无绑定人员");
        } else {
            boolean pushed = pushToNextAvailable(task, personList, 0);
            if (!pushed) {
                log.info("区域{}全员忙碌，taskId={}等待重试", cameraRegion.getLocationId(), task.getId());
                TobAlertTaskLog busyLog = new TobAlertTaskLog();
                busyLog.setTaskId(task.getId());
                busyLog.setOperateUser("system");
                busyLog.setOperateType(6);
                busyLog.setOperateContent("全员忙碌，等待空闲");
                tobAlertTaskLogService.insertTobAlertTaskLog(busyLog);
            }
        }

        // 7. 更新接收表状态
        receive.setProcessStatus("0");
        tobAlertReceiveMapper.updateTobAlertReceive(receive);

        return receive.getId();
    }

    /**
     * 从指定index开始，找第一个空闲的人推送
     * @return true=找到并推送了，false=全员忙碌
     */
    private boolean pushToNextAvailable(TobAlertTask task, List<TobRegionPerson> personList, int startIndex) {
        for (int i = startIndex; i < personList.size(); i++) {
            TobRegionPerson person = personList.get(i);
            if (tobAlertTaskService.isPersonBusy(person.getUserId())) {
                log.info("用户{}忙碌，跳过，taskId={}", person.getUserId(), task.getId());
                continue;
            }
            boolean success = wxworkPushService.pushTextCard(task, person.getUserId());

            TobAlertTaskLog pushLog = new TobAlertTaskLog();
            pushLog.setTaskId(task.getId());
            pushLog.setOperateUser("system");
            pushLog.setOperateType(success ? 3 : 4);
            pushLog.setOperateContent(success ?
                    "推送成功，推送至用户[" + person.getUserId() + "]" :
                    "推送失败，推送至用户[" + person.getUserId() + "]");
            tobAlertTaskLogService.insertTobAlertTaskLog(pushLog);

            if (success) {
                task.setAssignIndex(i);
                tobAlertTaskService.updateTobAlertTask(task);
            } else {
                task.setAssignIndex(i + 1);
                tobAlertTaskService.updateTobAlertTask(task);
                return pushToNextAvailable(task, personList, i + 1);
            }
            return true;
        }
        return false;
    }

    /**
     * 推送给下一个人员（拒绝/超时后流转，兼容旧调用）
     */
    private void pushNextPerson(TobAlertTask task, List<TobRegionPerson> personList) {
        int index = task.getAssignIndex() != null ? task.getAssignIndex() : 0;
        if (index >= personList.size()) {
            pushToAdmin(task, "所有绑定人员均已尝试");
            return;
        }
        boolean pushed = pushToNextAvailable(task, personList, index);
        if (!pushed) {
            pushToAdmin(task, "所有绑定人员均已尝试");
        }
    }

    /**
     * 推送至管理员（location_id=0 绑定的用户）
     */
    private void pushToAdmin(TobAlertTask task, String reason) {
        task.setPushedToAdmin(1);
        tobAlertTaskService.updateTobAlertTask(task);

        // 查询管理员（location_id=0）
        List<TobRegionPerson> adminList = tobRegionPersonService.selectTobRegionPersonByLocationId(0);
        if (adminList != null && !adminList.isEmpty()) {
            boolean success = wxworkPushService.pushTextCard(task, adminList.get(0).getUserId());
            TobAlertTaskLog log = new TobAlertTaskLog();
            log.setTaskId(task.getId());
            log.setOperateUser("system");
            if (success) {
                log.setOperateType(5);
                log.setOperateContent("超时/失败推送至管理员[" + adminList.get(0).getUserId() + "]，原因：" + reason);
            } else {
                log.setOperateType(4);
                log.setOperateContent("推送管理员失败，原因：" + reason);
            }
            tobAlertTaskLogService.insertTobAlertTaskLog(log);
        }
    }

    @Override
    public int updateTobAlertReceive(TobAlertReceive tobAlertReceive) {
        return tobAlertReceiveMapper.updateTobAlertReceive(tobAlertReceive);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long receiveAlertFromMonitoring(ToVehicleRealTimMonitoring data, double level) {
        // 1. 防重校验
        TobAlertReceive existReceive = tobAlertReceiveMapper.selectTobAlertReceiveByOriginalId(data.getId());
        if (existReceive != null) {
            log.info("监控数据已创建过预警，skip monitoringId={}", data.getId());
            return existReceive.getId();
        }

        // 2. 卡口→地域映射
        TobCameraRegion cameraRegion = null;
        if (data.getCameraId() != null) {
            cameraRegion = tobCameraRegionMapper.selectTobCameraRegionByCameraId(data.getCameraId());
        }
        if (cameraRegion == null) {
            log.warn("卡口未绑定地域，跳过预警，monitoringId={}, cameraId={}", data.getId(), data.getCameraId());
            return null;
        }

        // 3. 写入接收表
        TobAlertReceive receive = new TobAlertReceive();
        receive.setReceiveNo("R" + DateUtil.format(new Date(), "yyyyMMdd") + IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase());
        receive.setOriginalId(data.getId());
        receive.setReason("等级：" + getLevelText(level) + "（Level=" + level + "）");
        receive.setTrafficId(data.getId());
        receive.setPlate(data.getPlate());
        receive.setCameraId(data.getCameraId());
        receive.setCameraName(data.getCameraName());
        if (data.getCaptureTime() != null) {
            receive.setCaptureTime(data.getCaptureTime());
        }
        receive.setProcessStatus("0");
        tobAlertReceiveMapper.insertTobAlertReceive(receive);
        log.info("风险计算预警接收成功，receiveId={}, monitoringId={}, plate={}, level={}",
                 receive.getId(), data.getId(), data.getPlate(), level);

        // 4. 创建任务
        TobAlertTask task = new TobAlertTask();
        task.setTaskNo("T" + DateUtil.format(new Date(), "yyyyMMdd") + IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase());
        task.setOriginalId(data.getId());
        task.setReceiveId(receive.getId());
        task.setLocationId(cameraRegion.getLocationId());
        task.setPlate(data.getPlate());
        task.setCameraName(data.getCameraName());
        if (data.getCaptureTime() != null) {
            task.setCaptureTime(data.getCaptureTime());
        }
        task.setReason("等级：" + getLevelText(level) + "（Level=" + level + "）");
        task.setStatus(0);
        task.setAssignIndex(0);
        task.setPushedToAdmin(0);
        task.setExpireTime(DateUtil.offsetHour(new Date(), 2));
        task.setCreateBy("system");
        tobAlertTaskService.insertTobAlertTask(task);
        log.info("风险计算预警任务创建成功，taskId={}, taskNo={}", task.getId(), task.getTaskNo());

        // 5. 查询绑定人员并推送
        List<TobRegionPerson> personList = tobRegionPersonService.selectTobRegionPersonByLocationId(cameraRegion.getLocationId());
        if (personList == null || personList.isEmpty()) {
            pushToAdmin(task, "地域无绑定人员");
        } else {
            boolean pushed = pushToNextAvailable(task, personList, 0);
            if (!pushed) {
                log.info("区域{}全员忙碌，taskId={}等待重试", cameraRegion.getLocationId(), task.getId());
                TobAlertTaskLog busyLog = new TobAlertTaskLog();
                busyLog.setTaskId(task.getId());
                busyLog.setOperateUser("system");
                busyLog.setOperateType(6);
                busyLog.setOperateContent("全员忙碌，等待空闲");
                tobAlertTaskLogService.insertTobAlertTaskLog(busyLog);
            }
        }

        // 6. 更新接收表状态（monitoring）("1");
        tobAlertReceiveMapper.updateTobAlertReceive(receive);

        return receive.getId();
    }

    private String getLevelText(double level) {
        int low = getLevelConfigInt("alert.level.low", 30);
        int medium = getLevelConfigInt("alert.level.medium", 60);
        int high = getLevelConfigInt("alert.level.high", 100);
        if (level > high)   return "严重";
        if (level > medium) return "高";
        if (level > low)    return "中";
        return "低";
    }

    private int getLevelConfigInt(String key, int defaultValue) {
        String val = wxworkConfigService.getConfigValue(key, "");
        if (val == null || val.isEmpty()) return defaultValue;
        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException e) {
            log.warn("等级配置{}非法：{}，使用默认值{}", key, val, defaultValue);
            return defaultValue;
        }
    }
}
