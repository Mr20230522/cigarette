package com.ruoyi.system.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobCaseInformationMapper;
import com.ruoyi.system.domain.TobCaseInformation;
import com.ruoyi.system.service.ITobCaseInformationService;

/**
 * 案件信息Service业务层处理
 * 
 * @author cigarette
 * @date 2024-07-28
 */
@Service
public class TobCaseInformationServiceImpl implements ITobCaseInformationService 
{
    @Autowired
    private TobCaseInformationMapper tobCaseInformationMapper;

    /**
     * 查询案件信息
     * 
     * @param caseId 案件信息主键
     * @return 案件信息
     */
    @Override
    public TobCaseInformation selectTobCaseInformationByCaseId(Long caseId)
    {
        return tobCaseInformationMapper.selectTobCaseInformationByCaseId(caseId);
    }

    /**
     * 查询案件信息列表
     * 
     * @param tobCaseInformation 案件信息
     * @return 案件信息
     */
    @Override
    public List<TobCaseInformation> selectTobCaseInformationList(TobCaseInformation tobCaseInformation)
    {
        return tobCaseInformationMapper.selectTobCaseInformationList(tobCaseInformation);
    }

    /**
     * 新增案件信息
     * 
     * @param tobCaseInformation 案件信息
     * @return 结果
     */
    @Override
    public int insertTobCaseInformation(TobCaseInformation tobCaseInformation)
    {
        return tobCaseInformationMapper.insertTobCaseInformation(tobCaseInformation);
    }

    /**
     * 修改案件信息
     * 
     * @param tobCaseInformation 案件信息
     * @return 结果
     */
    @Override
    public int updateTobCaseInformation(TobCaseInformation tobCaseInformation)
    {
        return tobCaseInformationMapper.updateTobCaseInformation(tobCaseInformation);
    }

    /**
     * 批量删除案件信息
     * 
     * @param caseIds 需要删除的案件信息主键
     * @return 结果
     */
    @Override
    public int deleteTobCaseInformationByCaseIds(Long[] caseIds)
    {
        return tobCaseInformationMapper.deleteTobCaseInformationByCaseIds(caseIds);
    }

    /**
     * 删除案件信息信息
     * 
     * @param caseId 案件信息主键
     * @return 结果
     */
    @Override
    public int deleteTobCaseInformationByCaseId(Long caseId)
    {
        return tobCaseInformationMapper.deleteTobCaseInformationByCaseId(caseId);
    }
}
