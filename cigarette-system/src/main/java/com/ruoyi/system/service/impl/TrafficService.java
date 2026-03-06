package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TobClockLog;
import com.ruoyi.system.domain.TrafficData;
import com.ruoyi.system.domain.undefine.SearchOfTheSuspectedVehicle;
import com.ruoyi.system.domain.vo.CameraIdNameVO;
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
        System.out.println("========== 开始处理 ==========");
        System.out.println("当前时间: " + now);
        System.out.println("车辆ID: " + item.getId());
        System.out.println("检测点ID: " + item.getCameraId());

        // 第1步：查值班记录
        TobClockLog duty = tobClockLogMapper.selectCurrentDuty(
                Long.valueOf(item.getCameraId()), now
        );
        System.out.println("值班记录: " + (duty == null ? "null" : "找到，staffId=" + duty.getStaffId()));
        if (duty == null) return;

        // 第2步：查工作人员
        TobStaffVo staff = tobStaffMapper.selectByIdWithPhone(duty.getStaffId());
        System.out.println("工作人员: " + (staff == null ? "null" : "找到"));
        if (staff == null) return;

        // 第3步：查手机号
        String phone = staff.getPhonenumber();
        System.out.println("手机号: " + (phone == null || phone.isEmpty() ? "空" : phone));
        if (phone == null || phone.isEmpty()) return;

        // 第4步：发短信
        System.out.println("准备发短信...");
    }

    @Override
    public List<TrafficData> byIdGetVideoPath(TrafficData trafficData){
        return trafficDataMapper.byIdGetVideoPath(trafficData);
    }

    @Override
    public List<TrafficData> searchVehicle(SearchOfTheSuspectedVehicle search){
        return trafficDataMapper.searchVehicle(search);
    }

    @Override
    public List<CameraIdNameVO> getAllCameraIdNamePairs() {
        return trafficDataMapper.getAllCameraIdNamePairs();
    }
}