package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TobCaseInformation;
import com.ruoyi.system.domain.vo.TobCaseInformationVo;

/**
 * 案件信息Service接口
 * 
 * @author cigarette
 * @date 2024-07-28
 */
public interface ITobCaseInformationService 
{
    /**
     * 查询案件信息
     * 
     * @param caseId 案件信息主键
     * @return 案件信息
     */
    public TobCaseInformation selectTobCaseInformationByCaseId(Long caseId);

    /**
     * 查询案件信息列表
     * 
     * @param tobCaseInformation 案件信息
     * @return 案件信息集合
     */
    public List<TobCaseInformation> selectTobCaseInformationList(TobCaseInformation tobCaseInformation);


    /**
     * 查询案件信息列表Vo
     *
     * @param tobCaseInformationVo 案件信息
     * @return 案件信息集合
     */
    public List<TobCaseInformationVo> selectTobCaseInformationVoList(TobCaseInformationVo tobCaseInformationVo);



    /**
     * 新增案件信息
     * 
     * @param tobCaseInformation 案件信息
     * @return 结果
     */
    public int insertTobCaseInformation(TobCaseInformation tobCaseInformation);

    /**
     * 修改案件信息
     * 
     * @param tobCaseInformation 案件信息
     * @return 结果
     */
    public int updateTobCaseInformation(TobCaseInformation tobCaseInformation);

    /**
     * 批量删除案件信息
     * 
     * @param caseIds 需要删除的案件信息主键集合
     * @return 结果
     */
    public int deleteTobCaseInformationByCaseIds(Long[] caseIds);

    /**
     * 删除案件信息信息
     * 
     * @param caseId 案件信息主键
     * @return 结果
     */
    public int deleteTobCaseInformationByCaseId(Long caseId);
}
