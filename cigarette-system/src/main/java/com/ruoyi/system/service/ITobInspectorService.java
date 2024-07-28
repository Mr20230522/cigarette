package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TobInspector;

/**
 * 检测点人员Service接口
 * 
 * @author cigarette
 * @date 2024-07-28
 */
public interface ITobInspectorService 
{
    /**
     * 查询检测点人员
     * 
     * @param inspectorId 检测点人员主键
     * @return 检测点人员
     */
    public TobInspector selectTobInspectorByInspectorId(Long inspectorId);

    /**
     * 查询检测点人员列表
     * 
     * @param tobInspector 检测点人员
     * @return 检测点人员集合
     */
    public List<TobInspector> selectTobInspectorList(TobInspector tobInspector);

    /**
     * 新增检测点人员
     * 
     * @param tobInspector 检测点人员
     * @return 结果
     */
    public int insertTobInspector(TobInspector tobInspector);

    /**
     * 修改检测点人员
     * 
     * @param tobInspector 检测点人员
     * @return 结果
     */
    public int updateTobInspector(TobInspector tobInspector);

    /**
     * 批量删除检测点人员
     * 
     * @param inspectorIds 需要删除的检测点人员主键集合
     * @return 结果
     */
    public int deleteTobInspectorByInspectorIds(Long[] inspectorIds);

    /**
     * 删除检测点人员信息
     * 
     * @param inspectorId 检测点人员主键
     * @return 结果
     */
    public int deleteTobInspectorByInspectorId(Long inspectorId);
}
