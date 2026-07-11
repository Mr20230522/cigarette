package com.ruoyi.system.service;

import com.ruoyi.system.domain.TobAlertReceive;
import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import java.util.List;

/**
 * 预警数据接收Service接口
 */
public interface ITobAlertReceiveService {

    /**
     * 查询预警接收记录
     */
    TobAlertReceive selectTobAlertReceiveById(Long id);

    /**
     * 根据原始数据ID查询
     */
    TobAlertReceive selectTobAlertReceiveByOriginalId(Long originalId);

    /**
     * 查询预警接收记录列表
     */
    List<TobAlertReceive> selectTobAlertReceiveList(TobAlertReceive tobAlertReceive);

    /**
     * 接收预警数据并创建任务（核心方法）
     * @param originalId 原始数据ID
     * @param reason     预警原因
     * @return 接收记录ID
     */
    Long receiveAlert(Long originalId, String reason);

    /**
     * 修改预警接收记录
     */
    int updateTobAlertReceive(TobAlertReceive tobAlertReceive);

    /**
     * 从风险计算系统接收预警（直接用ToVehicleRealTimMonitoring数据，不查trafficdata）
     * @param data  实时监控数据
     * @param level 计算出的风险等级
     * @return 接收记录ID，null表示跳过（如卡口未绑定地域）
     */
    Long receiveAlertFromMonitoring(ToVehicleRealTimMonitoring data, double level);
}
