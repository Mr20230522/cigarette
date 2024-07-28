package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobInspectorMapper;
import com.ruoyi.system.domain.TobInspector;
import com.ruoyi.system.service.ITobInspectorService;

/**
 * 检测点人员Service业务层处理
 * 
 * @author cigarette
 * @date 2024-07-28
 */
@Service
public class TobInspectorServiceImpl implements ITobInspectorService 
{
    @Autowired
    private TobInspectorMapper tobInspectorMapper;

    /**
     * 查询检测点人员
     * 
     * @param inspectorId 检测点人员主键
     * @return 检测点人员
     */
    @Override
    public TobInspector selectTobInspectorByInspectorId(Long inspectorId)
    {
        return tobInspectorMapper.selectTobInspectorByInspectorId(inspectorId);
    }

    /**
     * 查询检测点人员列表
     * 
     * @param tobInspector 检测点人员
     * @return 检测点人员
     */
    @Override
    public List<TobInspector> selectTobInspectorList(TobInspector tobInspector)
    {
        return tobInspectorMapper.selectTobInspectorList(tobInspector);
    }

    /**
     * 新增检测点人员
     * 
     * @param tobInspector 检测点人员
     * @return 结果
     */
    @Override
    public int insertTobInspector(TobInspector tobInspector)
    {
        tobInspector.setCreateTime(DateUtils.getNowDate());
        return tobInspectorMapper.insertTobInspector(tobInspector);
    }

    /**
     * 修改检测点人员
     * 
     * @param tobInspector 检测点人员
     * @return 结果
     */
    @Override
    public int updateTobInspector(TobInspector tobInspector)
    {
        tobInspector.setUpdateTime(DateUtils.getNowDate());
        return tobInspectorMapper.updateTobInspector(tobInspector);
    }

    /**
     * 批量删除检测点人员
     * 
     * @param inspectorIds 需要删除的检测点人员主键
     * @return 结果
     */
    @Override
    public int deleteTobInspectorByInspectorIds(Long[] inspectorIds)
    {
        return tobInspectorMapper.deleteTobInspectorByInspectorIds(inspectorIds);
    }

    /**
     * 删除检测点人员信息
     * 
     * @param inspectorId 检测点人员主键
     * @return 结果
     */
    @Override
    public int deleteTobInspectorByInspectorId(Long inspectorId)
    {
        return tobInspectorMapper.deleteTobInspectorByInspectorId(inspectorId);
    }
}
