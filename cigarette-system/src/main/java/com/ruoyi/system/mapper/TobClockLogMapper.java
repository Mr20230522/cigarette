package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobClockLog;

/**
 * 出勤记录Mapper接口
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public interface TobClockLogMapper 
{
    /**
     * 查询出勤记录
     * 
     * @param commutingId 出勤记录主键
     * @return 出勤记录
     */
    public TobClockLog selectTobClockLogByCommutingId(Long commutingId);

    /**
     * 查询出勤记录列表
     * 
     * @param tobClockLog 出勤记录
     * @return 出勤记录集合
     */
    public List<TobClockLog> selectTobClockLogList(TobClockLog tobClockLog);

    /**
     * 新增出勤记录
     * 
     * @param tobClockLog 出勤记录
     * @return 结果
     */
    public int insertTobClockLog(TobClockLog tobClockLog);

    /**
     * 修改出勤记录
     * 
     * @param tobClockLog 出勤记录
     * @return 结果
     */
    public int updateTobClockLog(TobClockLog tobClockLog);

    /**
     * 删除出勤记录
     * 
     * @param commutingId 出勤记录主键
     * @return 结果
     */
    public int deleteTobClockLogByCommutingId(Long commutingId);

    /**
     * 批量删除出勤记录
     * 
     * @param commutingIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobClockLogByCommutingIds(Long[] commutingIds);
}
