package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobDutySchedule;

/**
 * 值班Mapper接口
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public interface TobDutyScheduleMapper 
{
    /**
     * 查询值班
     * 
     * @param dutyId 值班主键
     * @return 值班
     */
    public TobDutySchedule selectTobDutyScheduleByDutyId(Long dutyId);

    /**
     * 查询值班列表
     * 
     * @param tobDutySchedule 值班
     * @return 值班集合
     */
    public List<TobDutySchedule> selectTobDutyScheduleList(TobDutySchedule tobDutySchedule);

    /**
     * 新增值班
     * 
     * @param tobDutySchedule 值班
     * @return 结果
     */
    public int insertTobDutySchedule(TobDutySchedule tobDutySchedule);

    /**
     * 修改值班
     * 
     * @param tobDutySchedule 值班
     * @return 结果
     */
    public int updateTobDutySchedule(TobDutySchedule tobDutySchedule);

    /**
     * 删除值班
     * 
     * @param dutyId 值班主键
     * @return 结果
     */
    public int deleteTobDutyScheduleByDutyId(Long dutyId);

    /**
     * 批量删除值班
     * 
     * @param dutyIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobDutyScheduleByDutyIds(Long[] dutyIds);
}
