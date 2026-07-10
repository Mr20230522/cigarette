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
            // 推送首个人员
            pushToFirstPerson(task, personList);
        }

        // 7. 更新接收表状态
        receive.setProcessStatus("1");
        tobAlertReceiveMapper.updateTobAlertReceive(receive);

        return receive.getId();
    }

    /**
     * 推送给首个绑定人员
     */
    private void pushToFirstPerson(TobAlertTask task, List<TobRegionPerson> personList) {
        TobRegionPerson firstPerson = personList.get(0);
        boolean success = wxworkPushService.pushTextCard(task, firstPerson.getUserId());

        TobAlertTaskLog log = new TobAlertTaskLog();
        log.setTaskId(task.getId());
        log.setOperateUser("system");
        if (success) {
            log.setOperateType(3);
            log.setOperateContent("推送成功，推送至用户[" + firstPerson.getUserId() + "]");
        } else {
            log.setOperateType(4);
            log.setOperateContent("推送失败，推送至用户[" + firstPerson.getUserId() + "]");
            // 失败则推下一人
            task.setAssignIndex(task.getAssignIndex() + 1);
            tobAlertTaskService.updateTobAlertTask(task);
            pushNextPerson(task, personList);
        }
        tobAlertTaskLogService.insertTobAlertTaskLog(log);
    }

    /**
     * 推送给下一个人员（拒绝链逻辑）
     */
    private void pushNextPerson(TobAlertTask task, List<TobRegionPerson> personList) {
        int index = task.getAssignIndex();
        if (index >= personList.size()) {
            // 所有人员均推送失败/拒绝，推管理员
            pushToAdmin(task, "所有绑定人员均推送失败");
            return;
        }

        TobRegionPerson person = personList.get(index);
        boolean success = wxworkPushService.pushTextCard(task, person.getUserId());

        TobAlertTaskLog log = new TobAlertTaskLog();
        log.setTaskId(task.getId());
        log.setOperateUser("system");
        if (success) {
            log.setOperateType(3);
            log.setOperateContent("重新推送成功，推送至用户[" + person.getUserId() + "]");
        } else {
            log.setOperateType(4);
            log.setOperateContent("重新推送失败，推送至用户[" + person.getUserId() + "]");
            task.setAssignIndex(task.getAssignIndex() + 1);
            tobAlertTaskService.updateTobAlertTask(task);
            pushNextPerson(task, personList);
        }
        tobAlertTaskLogService.insertTobAlertTaskLog(log);
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
}
