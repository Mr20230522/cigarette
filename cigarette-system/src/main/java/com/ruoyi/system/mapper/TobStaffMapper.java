package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobStaff;

/**
 * 工作人员Mapper接口
 * 
 * @author ruoyi
 * @date 2024-07-25
 */
public interface TobStaffMapper 
{
    /**
     * 查询工作人员
     * 
     * @param staffId 工作人员主键
     * @return 工作人员
     */
    public TobStaff selectTobStaffByStaffId(Long staffId);

    /**
     * 查询工作人员列表
     * 
     * @param tobStaff 工作人员
     * @return 工作人员集合
     */
    public List<TobStaff> selectTobStaffList(TobStaff tobStaff);

    /**
     * 新增工作人员
     * 
     * @param tobStaff 工作人员
     * @return 结果
     */
    public int insertTobStaff(TobStaff tobStaff);

    /**
     * 修改工作人员
     * 
     * @param tobStaff 工作人员
     * @return 结果
     */
    public int updateTobStaff(TobStaff tobStaff);

    /**
     * 删除工作人员
     * 
     * @param staffId 工作人员主键
     * @return 结果
     */
    public int deleteTobStaffByStaffId(Long staffId);

    /**
     * 批量删除工作人员
     * 
     * @param staffIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobStaffByStaffIds(Long[] staffIds);
}
