package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TobClockLog;
import com.ruoyi.system.domain.TrafficData;
import com.ruoyi.system.domain.undefine.SearchOfTheSuspectedVehicle;
import com.ruoyi.system.domain.vo.TobStaffVo;  // 修改这里
import com.ruoyi.system.mapper.TobClockLogMapper;
import com.ruoyi.system.mapper.TobStaffMapper;
import com.ruoyi.system.mapper.TrafficDataMapper;
import com.ruoyi.system.service.ITrafficDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TrafficService implements ITrafficDataService {

    @Autowired
    private TrafficDataMapper trafficDataMapper;

    @Autowired
    private SmsService smsService;

    @Autowired
    private TobClockLogMapper tobClockLogMapper;

    @Autowired
    private TobStaffMapper tobStaffMapper;

    @Override
    public List<TrafficData> tenList(TrafficData trafficData){
        List<TrafficData> list = trafficDataMapper.tenList(trafficData);
        if (list != null && !list.isEmpty()) {
            for (TrafficData item : list) {
                try {
                    processAlert(item);
                } catch (Exception e) {
                    System.out.println("处理告警失败，车辆ID: " + item.getId() + ", 错误: " + e.getMessage());
                }
            }
        }
        return list;
    }

    @Override
    public List<TrafficData> allList(TrafficData trafficData){
        List<TrafficData> list = trafficDataMapper.allList(trafficData);
        if (list != null && !list.isEmpty()) {
            for (TrafficData item : list) {
                try {
                    processAlert(item);
                } catch (Exception e) {
                    System.out.println("处理告警失败，车辆ID: " + item.getId() + ", 错误: " + e.getMessage());
                }
            }
        }
        return list;
    }

    @Override
    public List<TrafficData> overIdList(TrafficData trafficData){
        List<TrafficData> list = trafficDataMapper.overIdList(trafficData);
        if (list != null && !list.isEmpty()) {
            for (TrafficData item : list) {
                try {
                    processAlert(item);
                } catch (Exception e) {
                    System.out.println("处理告警失败，车辆ID: " + item.getId() + ", 错误: " + e.getMessage());
                }
            }
        }
        return list;
    }

    private void processAlert(TrafficData item) {
        Date now = new Date();

        System.out.println("========================================");
        System.out.println("处理告警 - 车辆ID: " + item.getId());
        System.out.println("车牌: " + item.getPlate());
        System.out.println("监测点: " + item.getCameraName() + " (ID: " + item.getCameraId() + ")");
        System.out.println("嫌疑度: " + item.getLevel());

        // 查询当前值班人员
        TobClockLog duty = tobClockLogMapper.selectCurrentDuty(
                Long.valueOf(item.getCameraId()),
                now
        );

        if (duty == null) {
            System.out.println("未找到值班人员，检测点ID: " + item.getCameraId());
            System.out.println("========================================");
            return;
        }

        System.out.println("找到值班记录 - staffId: " + duty.getStaffId());

        // 查询工作人员获取手机号（使用Vo）
        TobStaffVo staff = tobStaffMapper.selectByIdWithPhone(duty.getStaffId());  // 修改这里
        if (staff == null) {
            System.out.println("未找到工作人员，staffId: " + duty.getStaffId());
            System.out.println("========================================");
            return;
        }

        String phone = staff.getPhonenumber();
        if (phone == null || phone.isEmpty()) {
            System.out.println("工作人员无手机号，staffId: " + staff.getStaffId());
            System.out.println("========================================");
            return;
        }

        // 构建短信内容
        String msg = String.format(
                "【云南省烟草公司曲靖市公司】告警：卡口%s发现嫌疑车辆%s，嫌疑度%s，时间%s",
                item.getCameraName(),
                item.getPlate(),
                item.getLevel(),
                item.getCaptureTime()
        );

        System.out.println("发送短信到: " + phone);
        System.out.println("内容: " + msg);

        boolean success = smsService.sendSms(phone, msg);
        System.out.println("短信发送" + (success ? "成功" : "失败"));
        System.out.println("========================================");
    }

    @Override
    public List<TrafficData> byIdGetVideoPath(TrafficData trafficData){
        return trafficDataMapper.byIdGetVideoPath(trafficData);
    }

    @Override
    public List<TrafficData> searchVehicle(SearchOfTheSuspectedVehicle search){
        return trafficDataMapper.searchVehicle(search);
    }
}