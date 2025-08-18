package com.ruoyi.system.service;

public interface ITobSuspicionLevelService {
    /**
     * 获取当前嫌疑度值
     */
    Double getCurrentLevel();

    /**
     * 更新嫌疑度值
     * @param newLevel 新的嫌疑度值
     * @return 是否更新成功
     */
    boolean updateLevel(Double newLevel);
}
