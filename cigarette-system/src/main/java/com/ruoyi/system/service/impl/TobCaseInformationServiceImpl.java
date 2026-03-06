package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.system.domain.TobPerson;
import com.ruoyi.system.domain.TobVehicle;
import com.ruoyi.system.domain.TobVehicleBehavior;
import com.ruoyi.system.domain.vo.TobCaseInformationVo;
import com.ruoyi.system.mapper.TobPersonMapper;
import com.ruoyi.system.mapper.TobVehicleBehaviorMapper;
import com.ruoyi.system.mapper.TobVehicleMapper;
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
    @Autowired
    private TobVehicleBehaviorMapper tobVehicleBehaviorMapper;
    @Autowired
    private TobVehicleMapper tobVehicleMapper;
    @Autowired
    private TobPersonMapper tobPersonMapper;

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
     * 查询案件信息列表Vo
     *
     * @param tobCaseInformationVo 案件信息
     * @return 案件信息
     */
    @Override
    public List<TobCaseInformationVo> selectTobCaseInformationVoList(TobCaseInformationVo tobCaseInformationVo) {
        return tobCaseInformationMapper.selectTobCaseInformationVoList(tobCaseInformationVo);
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
        TobVehicleBehavior vehiclebehavior =tobVehicleBehaviorMapper.selectTobVehicleBehaviorByBehaviorId(tobCaseInformation.getBehaviorId());

        TobVehicle vehicle =tobVehicleMapper.selectTobVehicleByCarId(vehiclebehavior.getCarId());
        vehicle.setCasesInvolved(vehicle.getCasesInvolved()+1);

        tobVehicleMapper.updateTobVehicle(vehicle);
        TobPerson person =tobPersonMapper.selectTobPersonBySuspectId(vehiclebehavior.getDriverId());
        person.setCasesInvolved(person.getCasesInvolved()+1);
        tobPersonMapper.updateTobPerson(person);
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
