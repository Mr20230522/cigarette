package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobStaffMapper;
import com.ruoyi.system.domain.TobStaff;
import com.ruoyi.system.service.ITobStaffService;

/**
 * 工作人员Service业务层处理
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@Service
public class TobStaffServiceImpl implements ITobStaffService 
{
    @Autowired
    private TobStaffMapper tobStaffMapper;

    /**
     * 查询工作人员
     * 
     * @param staffId 工作人员主键
     * @return 工作人员
     */
    @Override
    public TobStaff selectTobStaffByStaffId(Long staffId)
    {
        return tobStaffMapper.selectTobStaffByStaffId(staffId);
    }

    /**
     * 查询工作人员列表
     * 
     * @param tobStaff 工作人员
     * @return 工作人员
     */
    @Override
    public List<TobStaff> selectTobStaffList(TobStaff tobStaff)
    {
        return tobStaffMapper.selectTobStaffList(tobStaff);
    }

    /**
     * 新增工作人员
     * 
     * @param tobStaff 工作人员
     * @return 结果
     */
    @Override
    public int insertTobStaff(TobStaff tobStaff)
    {
        tobStaff.setCreateTime(DateUtils.getNowDate());
        return tobStaffMapper.insertTobStaff(tobStaff);
    }

    /**
     * 修改工作人员
     * 
     * @param tobStaff 工作人员
     * @return 结果
     */
    @Override
    public int updateTobStaff(TobStaff tobStaff)
    {
        tobStaff.setUpdateTime(DateUtils.getNowDate());
        return tobStaffMapper.updateTobStaff(tobStaff);
    }

    /**
     * 批量删除工作人员
     * 
     * @param staffIds 需要删除的工作人员主键
     * @return 结果
     */
    @Override
    public int deleteTobStaffByStaffIds(Long[] staffIds)
    {
        return tobStaffMapper.deleteTobStaffByStaffIds(staffIds);
    }

    /**
     * 删除工作人员信息
     * 
     * @param staffId 工作人员主键
     * @return 结果
     */
    @Override
    public int deleteTobStaffByStaffId(Long staffId)
    {
        return tobStaffMapper.deleteTobStaffByStaffId(staffId);
    }
}
