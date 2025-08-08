package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.Factor;
import java.util.List;

public interface FactorMapper {
    List<Factor> selectFactorList(Factor factor);
    Factor selectFactorById(Long id);
    int insertFactor(Factor factor);
    int updateFactor(Factor factor);
    int deleteFactorById(Long id);
    int deleteFactorByIds(Long[] ids);
}
