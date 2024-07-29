package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobClockLogMapper;
import com.ruoyi.system.domain.TobClockLog;
import com.ruoyi.system.service.ITobClockLogService;

/**
 * 出勤记录Service业务层处理
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@Service
public class TobClockLogServiceImpl implements ITobClockLogService 
{
    @Autowired
    private TobClockLogMapper tobClockLogMapper;

    /**
     * 查询出勤记录
     * 
     * @param commutingId 出勤记录主键
     * @return 出勤记录
     */
    @Override
    public TobClockLog selectTobClockLogByCommutingId(Long commutingId)
    {
        return tobClockLogMapper.selectTobClockLogByCommutingId(commutingId);
    }

    /**
     * 查询出勤记录列表
     * 
     * @param tobClockLog 出勤记录
     * @return 出勤记录
     */
    @Override
    public List<TobClockLog> selectTobClockLogList(TobClockLog tobClockLog)
    {
        return tobClockLogMapper.selectTobClockLogList(tobClockLog);
    }

    /**
     * 新增出勤记录
     * 
     * @param tobClockLog 出勤记录
     * @return 结果
     */
    @Override
    public int insertTobClockLog(TobClockLog tobClockLog)
    {
        tobClockLog.setCreateTime(DateUtils.getNowDate());
        return tobClockLogMapper.insertTobClockLog(tobClockLog);
    }

    /**
     * 修改出勤记录
     * 
     * @param tobClockLog 出勤记录
     * @return 结果
     */
    @Override
    public int updateTobClockLog(TobClockLog tobClockLog)
    {
        tobClockLog.setUpdateTime(DateUtils.getNowDate());
        return tobClockLogMapper.updateTobClockLog(tobClockLog);
    }

    /**
     * 批量删除出勤记录
     * 
     * @param commutingIds 需要删除的出勤记录主键
     * @return 结果
     */
    @Override
    public int deleteTobClockLogByCommutingIds(Long[] commutingIds)
    {
        return tobClockLogMapper.deleteTobClockLogByCommutingIds(commutingIds);
    }

    /**
     * 删除出勤记录信息
     * 
     * @param commutingId 出勤记录主键
     * @return 结果
     */
    @Override
    public int deleteTobClockLogByCommutingId(Long commutingId)
    {
        return tobClockLogMapper.deleteTobClockLogByCommutingId(commutingId);
    }
}
