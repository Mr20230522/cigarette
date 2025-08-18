package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TobSuspicionLevel;
import io.lettuce.core.dynamic.annotation.Param;

public interface TobSuspicionLevelMapper {
    /**
     * 获取当前嫌疑度值
     */
    TobSuspicionLevel selectSuspicionLevel();

    /**
     * 更新嫌疑度值
     * @param level 新的嫌疑度值
     * @return 影响的行数
     */
    int updateSuspicionLevel(@Param("level") Double level);

    /**
     * 插入初始数据
     * @param level 初始嫌疑度值
     * @return 影响的行数
     */
    int insertSuspicionLevel(@Param("level") Double level);
}
