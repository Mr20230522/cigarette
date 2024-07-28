package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobPersonMapper;
import com.ruoyi.system.domain.TobPerson;
import com.ruoyi.system.service.ITobPersonService;

/**
 * 驾驶人员Service业务层处理
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@Service
public class TobPersonServiceImpl implements ITobPersonService 
{
    @Autowired
    private TobPersonMapper tobPersonMapper;

    /**
     * 查询驾驶人员
     * 
     * @param suspectId 驾驶人员主键
     * @return 驾驶人员
     */
    @Override
    public TobPerson selectTobPersonBySuspectId(Long suspectId)
    {
        return tobPersonMapper.selectTobPersonBySuspectId(suspectId);
    }

    /**
     * 查询驾驶人员列表
     * 
     * @param tobPerson 驾驶人员
     * @return 驾驶人员
     */
    @Override
    public List<TobPerson> selectTobPersonList(TobPerson tobPerson)
    {
        return tobPersonMapper.selectTobPersonList(tobPerson);
    }

    /**
     * 新增驾驶人员
     * 
     * @param tobPerson 驾驶人员
     * @return 结果
     */
    @Override
    public int insertTobPerson(TobPerson tobPerson)
    {
        tobPerson.setCreateTime(DateUtils.getNowDate());
        return tobPersonMapper.insertTobPerson(tobPerson);
    }

    /**
     * 修改驾驶人员
     * 
     * @param tobPerson 驾驶人员
     * @return 结果
     */
    @Override
    public int updateTobPerson(TobPerson tobPerson)
    {
        tobPerson.setUpdateTime(DateUtils.getNowDate());
        return tobPersonMapper.updateTobPerson(tobPerson);
    }

    /**
     * 批量删除驾驶人员
     * 
     * @param suspectIds 需要删除的驾驶人员主键
     * @return 结果
     */
    @Override
    public int deleteTobPersonBySuspectIds(Long[] suspectIds)
    {
        return tobPersonMapper.deleteTobPersonBySuspectIds(suspectIds);
    }

    /**
     * 删除驾驶人员信息
     * 
     * @param suspectId 驾驶人员主键
     * @return 结果
     */
    @Override
    public int deleteTobPersonBySuspectId(Long suspectId)
    {
        return tobPersonMapper.deleteTobPersonBySuspectId(suspectId);
    }
}
