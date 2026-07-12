package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TobAlertReceive;
import java.util.List;

/**
 * 预警数据接收Mapper接口
 */
public interface TobAlertReceiveMapper {

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
     * 新增预警接收记录
     */
    int insertTobAlertReceive(TobAlertReceive tobAlertReceive);

    /**
     * 修改预警接收记录
     */
    int updateTobAlertReceive(TobAlertReceive tobAlertReceive);
}
