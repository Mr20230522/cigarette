package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobDistrictMapper;
import com.ruoyi.system.domain.TobDistrict;
import com.ruoyi.system.service.ITobDistrictService;

/**
 * 地区Service业务层处理
 * 
 * @author ruoyi
 * @date 2024-07-11
 */
@Service
public class TobDistrictServiceImpl implements ITobDistrictService 
{
    @Autowired
    private TobDistrictMapper tobDistrictMapper;

    /**
     * 查询地区
     * 
     * @param districtId 地区主键
     * @return 地区
     */
    @Override
    public TobDistrict selectTobDistrictByDistrictId(Long districtId)
    {
        return tobDistrictMapper.selectTobDistrictByDistrictId(districtId);
    }

    /**
     * 查询地区列表
     * 
     * @param tobDistrict 地区
     * @return 地区
     */
    @Override
    public List<TobDistrict> selectTobDistrictList(TobDistrict tobDistrict)
    {
        return tobDistrictMapper.selectTobDistrictList(tobDistrict);
    }

    /**
     * 新增地区
     * 
     * @param tobDistrict 地区
     * @return 结果
     */
    @Override
    public int insertTobDistrict(TobDistrict tobDistrict)
    {
        tobDistrict.setCreateTime(DateUtils.getNowDate());
        return tobDistrictMapper.insertTobDistrict(tobDistrict);
    }

    /**
     * 修改地区
     * 
     * @param tobDistrict 地区
     * @return 结果
     */
    @Override
    public int updateTobDistrict(TobDistrict tobDistrict)
    {
        tobDistrict.setUpdateTime(DateUtils.getNowDate());
        return tobDistrictMapper.updateTobDistrict(tobDistrict);
    }

    /**
     * 批量删除地区
     * 
     * @param districtIds 需要删除的地区主键
     * @return 结果
     */
    @Override
    public int deleteTobDistrictByDistrictIds(Long[] districtIds)
    {
        return tobDistrictMapper.deleteTobDistrictByDistrictIds(districtIds);
    }

    /**
     * 删除地区信息
     * 
     * @param districtId 地区主键
     * @return 结果
     */
    @Override
    public int deleteTobDistrictByDistrictId(Long districtId)
    {
        return tobDistrictMapper.deleteTobDistrictByDistrictId(districtId);
    }
}
