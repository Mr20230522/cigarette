package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TobCamera;
import com.ruoyi.system.domain.TobClockLog;
import com.ruoyi.system.domain.TrafficData;
import com.ruoyi.system.domain.undefine.SearchOfTheSuspectedVehicle;
import com.ruoyi.system.domain.vo.CameraIdNameVO;
import com.ruoyi.system.domain.vo.TobStaffVo;
import com.ruoyi.system.mapper.TobCameraMapper;
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

    @Autowired
    private TobCameraMapper tobCameraMapper;

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

    /**
     * 处理告警逻辑
     */
    private void processAlert(TrafficData item) {
        try {
            // 记录开始处理
            System.out.println("========== 开始处理告警 ==========");
            System.out.println("车辆ID: " + item.getId());
            System.out.println("摄像头ID(traffic_camera_id): " + item.getCameraId());
            System.out.println("捕获时间: " + item.getCaptureTime());
            System.out.println("嫌疑值: " + item.getLevel());

            // 第1步：通过traffic_camera_id获取摄像头信息（包含detection_id）
            if (item.getCameraId() == null) {
                System.out.println("摄像头ID为空，无法处理");
                return;
            }

            // 使用traffic_camera_id查询摄像头信息
            TobCamera camera = tobCameraMapper.selectByTrafficCameraId(item.getCameraId().longValue());
            if (camera == null) {
                System.out.println("未找到摄像头信息，traffic_camera_id: " + item.getCameraId());
                return;
            }
            System.out.println("找到摄像头，detection_id: " + camera.getDetectionId());

            // 第2步：查询该检测点在捕获时间时的值班记录
            if (item.getCaptureTime() == null || item.getCaptureTime().isEmpty()) {
                System.out.println("捕获时间为空，无法处理");
                return;
            }

            // 将String类型的captureTime转换为Date
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            java.util.Date captureDate = sdf.parse(item.getCaptureTime());

            TobClockLog duty = tobClockLogMapper.selectCurrentDuty(
                    camera.getDetectionId(),    // 检测点ID
                    captureDate                 // 使用实际的捕获时间
            );

            if (duty == null) {
                System.out.println("未找到值班记录，detection_id: " + camera.getDetectionId() +
                        ", 时间: " + item.getCaptureTime());
                return;
            }
            System.out.println("找到值班记录，staff_id: " + duty.getStaffId());

            // 第3步：查询工作人员信息（带手机号）
            TobStaffVo staff = tobStaffMapper.selectByIdWithPhone(duty.getStaffId());
            if (staff == null) {
                System.out.println("未找到工作人员信息，staff_id: " + duty.getStaffId());
                return;
            }

            String phone = staff.getPhonenumber();
            if (phone == null || phone.isEmpty()) {
                System.out.println("工作人员手机号为空，staff_id: " + duty.getStaffId());
                return;
            }
            System.out.println("找到工作人员，手机号: " + phone);

            // 第4步：构建短信内容并发送
            String message = buildAlertMessage(item);
            System.out.println("准备发送短信: " + message);

            boolean success = smsService.sendSms(phone, message);
            if (success) {
                System.out.println("短信发送成功，车辆ID: " + item.getId() + ", 手机号: " + phone);
            } else {
                System.out.println("短信发送失败，车辆ID: " + item.getId());
            }

        } catch (Exception e) {
            System.err.println("处理告警异常，车辆ID: " + item.getId() +
                    ", 错误: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 构建告警短信内容
     */
    private String buildAlertMessage(TrafficData item) {
        StringBuilder sb = new StringBuilder();
        sb.append("【车辆告警】");
        sb.append("检测点：").append(item.getCameraName()).append("，");
        sb.append("时间：").append(item.getCaptureTime()).append("，");
        sb.append("车牌：").append(item.getPlate()).append("，");
        sb.append("嫌疑值：").append(item.getLevel()).append("。");
        sb.append("请及时查看监控。");
        return sb.toString();
    }
}