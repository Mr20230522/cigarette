package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.Factor;
import com.ruoyi.system.mapper.FactorMapper;
import com.ruoyi.system.service.IFactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FactorServiceImpl implements IFactorService {

    @Autowired
    private FactorMapper factorMapper;

    @Override
    public List<Factor> selectFactorList(Factor factor) {
        return factorMapper.selectFactorList(factor);
    }

    @Override
    public Factor selectFactorById(Long id) {
        return factorMapper.selectFactorById(id);
    }

    @Override
    public int insertFactor(Factor factor) {
        return factorMapper.insertFactor(factor);
    }

    @Override
    public int updateFactor(Factor factor) {
        return factorMapper.updateFactor(factor);
    }

    @Override
    public int deleteFactorByIds(Long[] ids) {
        return factorMapper.deleteFactorByIds(ids);
    }

    @Override
    public int deleteFactorById(Long id) {
        return factorMapper.deleteFactorById(id);
    }
}
