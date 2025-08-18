package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TobCaseHistory;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface TobCaseHistoryMapper {

    /**
     * 查询所有历史案件车牌（用于构建黑名单）
     */
    List<TobCaseHistory> selectAll();

    /**
     * 根据车牌查询是否存在历史案件记录
     */
    TobCaseHistory selectByPlate(String plate);
}