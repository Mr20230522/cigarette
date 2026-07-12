package com.ruoyi.system.service;

import com.ruoyi.system.domain.TobCaseHistory;

import java.util.List;

/**
 * 历史案件车辆信息 服务层
 */
public interface ITobCaseHistoryService {
    /**
     * 查询所有历史案件车辆信息
     *
     * @return 历史案件车辆集合
     */
    List<TobCaseHistory> listAll();

    /**
     * 根据车牌查询历史案件车辆
     *
     * @param plate 车牌号码
     * @return 历史案件车辆信息，不存在返回 null
     */
    TobCaseHistory getByPlate(String plate);

    /**
     * 根据ID查询历史案件
     */
    TobCaseHistory getById(Long id);

    /**
     * 新增历史案件
     */
    int insert(TobCaseHistory record);

    /**
     * 修改历史案件
     */
    int update(TobCaseHistory record);

    /**
     * 条件查询历史案件列表（支持车牌/车型/品牌模糊搜索）
     */
    List<TobCaseHistory> selectList(TobCaseHistory query);
}
