package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TobSuspicionLevel;
import com.ruoyi.system.mapper.TobSuspicionLevelMapper;
import com.ruoyi.system.service.ITobSuspicionLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TobSuspicionLevelServiceImpl implements ITobSuspicionLevelService {

    @Autowired
    private TobSuspicionLevelMapper suspicionLevelMapper;

    @Override
    public Double getCurrentLevel() {
        TobSuspicionLevel level = suspicionLevelMapper.selectSuspicionLevel();
        if (level == null) {
            // 如果表中没有数据，插入默认值并返回
            suspicionLevelMapper.insertSuspicionLevel(50.0);
            return 50.0;
        }
        return level.getLevel();
    }

    @Override
    public boolean updateLevel(Double newLevel) {
        if (newLevel < 0 || newLevel > 100) {
            throw new RuntimeException("嫌疑度值必须在0-100之间");
        }

        // 先检查记录是否存在
        TobSuspicionLevel current = suspicionLevelMapper.selectSuspicionLevel();
        if (current == null) {
            return suspicionLevelMapper.insertSuspicionLevel(newLevel) > 0;
        } else {
            return suspicionLevelMapper.updateSuspicionLevel(newLevel) > 0;
        }
    }
}
