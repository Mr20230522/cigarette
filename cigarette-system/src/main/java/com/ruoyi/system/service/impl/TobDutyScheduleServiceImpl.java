package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobDutyScheduleMapper;
import com.ruoyi.system.domain.TobDutySchedule;
import com.ruoyi.system.service.ITobDutyScheduleService;

/**
 * 值班Service业务层处理
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@Service
public class TobDutyScheduleServiceImpl implements ITobDutyScheduleService 
{
    @Autowired
    private TobDutyScheduleMapper tobDutyScheduleMapper;

    /**
     * 查询值班
     * 
     * @param dutyId 值班主键
     * @return 值班
     */
    @Override
    public TobDutySchedule selectTobDutyScheduleByDutyId(Long dutyId)
    {
        return tobDutyScheduleMapper.selectTobDutyScheduleByDutyId(dutyId);
    }

    /**
     * 查询值班列表
     * 
     * @param tobDutySchedule 值班
     * @return 值班
     */
    @Override
    public List<TobDutySchedule> selectTobDutyScheduleList(TobDutySchedule tobDutySchedule)
    {
        return tobDutyScheduleMapper.selectTobDutyScheduleList(tobDutySchedule);
    }

    /**
     * 新增值班
     * 
     * @param tobDutySchedule 值班
     * @return 结果
     */
    @Override
    public int insertTobDutySchedule(TobDutySchedule tobDutySchedule)
    {
        tobDutySchedule.setCreateTime(DateUtils.getNowDate());
        return tobDutyScheduleMapper.insertTobDutySchedule(tobDutySchedule);
    }

    /**
     * 修改值班
     * 
     * @param tobDutySchedule 值班
     * @return 结果
     */
    @Override
    public int updateTobDutySchedule(TobDutySchedule tobDutySchedule)
    {
        return tobDutyScheduleMapper.updateTobDutySchedule(tobDutySchedule);
    }

    /**
     * 批量删除值班
     * 
     * @param dutyIds 需要删除的值班主键
     * @return 结果
     */
    @Override
    public int deleteTobDutyScheduleByDutyIds(Long[] dutyIds)
    {
        return tobDutyScheduleMapper.deleteTobDutyScheduleByDutyIds(dutyIds);
    }

    /**
     * 删除值班信息
     * 
     * @param dutyId 值班主键
     * @return 结果
     */
    @Override
    public int deleteTobDutyScheduleByDutyId(Long dutyId)
    {
        return tobDutyScheduleMapper.deleteTobDutyScheduleByDutyId(dutyId);
    }
}
