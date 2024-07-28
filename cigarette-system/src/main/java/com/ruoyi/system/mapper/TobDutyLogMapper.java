package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobDutyLog;

/**
 * 执勤记录Mapper接口
 * 
 * @author cigarette
 * @date 2024-07-28
 */
public interface TobDutyLogMapper 
{
    /**
     * 查询执勤记录
     * 
     * @param dutyId 执勤记录主键
     * @return 执勤记录
     */
    public TobDutyLog selectTobDutyLogByDutyId(Long dutyId);

    /**
     * 查询执勤记录列表
     * 
     * @param tobDutyLog 执勤记录
     * @return 执勤记录集合
     */
    public List<TobDutyLog> selectTobDutyLogList(TobDutyLog tobDutyLog);

    /**
     * 新增执勤记录
     * 
     * @param tobDutyLog 执勤记录
     * @return 结果
     */
    public int insertTobDutyLog(TobDutyLog tobDutyLog);

    /**
     * 修改执勤记录
     * 
     * @param tobDutyLog 执勤记录
     * @return 结果
     */
    public int updateTobDutyLog(TobDutyLog tobDutyLog);

    /**
     * 删除执勤记录
     * 
     * @param dutyId 执勤记录主键
     * @return 结果
     */
    public int deleteTobDutyLogByDutyId(Long dutyId);

    /**
     * 批量删除执勤记录
     * 
     * @param dutyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobDutyLogByDutyIds(Long[] dutyIds);
}
