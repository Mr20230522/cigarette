package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobDistrict;

/**
 * 地区Mapper接口
 * 
 * @author ruoyi
 * @date 2024-07-11
 */
public interface TobDistrictMapper 
{
    /**
     * 查询地区
     * 
     * @param districtId 地区主键
     * @return 地区
     */
    public TobDistrict selectTobDistrictByDistrictId(Long districtId);

    /**
     * 查询地区列表
     * 
     * @param tobDistrict 地区
     * @return 地区集合
     */
    public List<TobDistrict> selectTobDistrictList(TobDistrict tobDistrict);

    /**
     * 新增地区
     * 
     * @param tobDistrict 地区
     * @return 结果
     */
    public int insertTobDistrict(TobDistrict tobDistrict);

    /**
     * 修改地区
     * 
     * @param tobDistrict 地区
     * @return 结果
     */
    public int updateTobDistrict(TobDistrict tobDistrict);

    /**
     * 删除地区
     * 
     * @param districtId 地区主键
     * @return 结果
     */
    public int deleteTobDistrictByDistrictId(Long districtId);

    /**
     * 批量删除地区
     * 
     * @param districtIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobDistrictByDistrictIds(Long[] districtIds);
}
