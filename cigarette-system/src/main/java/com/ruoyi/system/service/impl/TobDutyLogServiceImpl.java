package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobDutyLogMapper;
import com.ruoyi.system.domain.TobDutyLog;
import com.ruoyi.system.service.ITobDutyLogService;

/**
 * 执勤记录Service业务层处理
 * 
 * @author cigarette
 * @date 2024-07-28
 */
@Service
public class TobDutyLogServiceImpl implements ITobDutyLogService 
{
    @Autowired
    private TobDutyLogMapper tobDutyLogMapper;

    /**
     * 查询执勤记录
     * 
     * @param dutyId 执勤记录主键
     * @return 执勤记录
     */
    @Override
    public TobDutyLog selectTobDutyLogByDutyId(Long dutyId)
    {
        return tobDutyLogMapper.selectTobDutyLogByDutyId(dutyId);
    }

    /**
     * 查询执勤记录列表
     * 
     * @param tobDutyLog 执勤记录
     * @return 执勤记录
     */
    @Override
    public List<TobDutyLog> selectTobDutyLogList(TobDutyLog tobDutyLog)
    {
        return tobDutyLogMapper.selectTobDutyLogList(tobDutyLog);
    }

    /**
     * 新增执勤记录
     * 
     * @param tobDutyLog 执勤记录
     * @return 结果
     */
    @Override
    public int insertTobDutyLog(TobDutyLog tobDutyLog)
    {
        tobDutyLog.setCreateTime(DateUtils.getNowDate());
        return tobDutyLogMapper.insertTobDutyLog(tobDutyLog);
    }

    /**
     * 修改执勤记录
     * 
     * @param tobDutyLog 执勤记录
     * @return 结果
     */
    @Override
    public int updateTobDutyLog(TobDutyLog tobDutyLog)
    {
        tobDutyLog.setUpdateTime(DateUtils.getNowDate());
        return tobDutyLogMapper.updateTobDutyLog(tobDutyLog);
    }

    /**
     * 批量删除执勤记录
     * 
     * @param dutyIds 需要删除的执勤记录主键
     * @return 结果
     */
    @Override
    public int deleteTobDutyLogByDutyIds(Long[] dutyIds)
    {
        return tobDutyLogMapper.deleteTobDutyLogByDutyIds(dutyIds);
    }

    /**
     * 删除执勤记录信息
     * 
     * @param dutyId 执勤记录主键
     * @return 结果
     */
    @Override
    public int deleteTobDutyLogByDutyId(Long dutyId)
    {
        return tobDutyLogMapper.deleteTobDutyLogByDutyId(dutyId);
    }
}
