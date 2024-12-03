package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TobPerson;

/**
 * 驾驶人员Service接口
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public interface ITobPersonService 
{
    /**
     * 查询驾驶人员
     * 
     * @param suspectId 驾驶人员主键
     * @return 驾驶人员
     */
    public TobPerson selectTobPersonBySuspectId(Long suspectId);

    /**
     * 查询驾驶人员列表
     * 
     * @param tobPerson 驾驶人员
     * @return 驾驶人员集合
     */
    public List<TobPerson> selectTobPersonList(TobPerson tobPerson);

    /**
     * 新增驾驶人员
     * 
     * @param tobPerson 驾驶人员
     * @return 结果
     */
    public int insertTobPerson(TobPerson tobPerson);

    /**
     * 修改驾驶人员
     * 
     * @param tobPerson 驾驶人员
     * @return 结果
     */
    public int updateTobPerson(TobPerson tobPerson);

    /**
     * 批量删除驾驶人员
     * 
     * @param suspectIds 需要删除的驾驶人员主键集合
     * @return 结果
     */
    public int deleteTobPersonBySuspectIds(Long[] suspectIds);

    /**
     * 删除驾驶人员信息
     * 
     * @param suspectId 驾驶人员主键
     * @return 结果
     */
    public int deleteTobPersonBySuspectId(Long suspectId);
}
