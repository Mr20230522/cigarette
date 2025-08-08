package com.ruoyi.system.service;

import com.ruoyi.system.domain.Factor;
import java.util.List;

public interface IFactorService {

    /**
     * 查询因子列表
     */
    List<Factor> selectFactorList(Factor factor);

    /**
     * 查询因子详情
     */
    Factor selectFactorById(Long id);

    /**
     * 新增因子
     */
    int insertFactor(Factor factor);

    /**
     * 修改因子
     */
    int updateFactor(Factor factor);

    /**
     * 批量删除因子
     */
    int deleteFactorByIds(Long[] ids);

    /**
     * 删除单个因子
     */
    int deleteFactorById(Long id);
}
