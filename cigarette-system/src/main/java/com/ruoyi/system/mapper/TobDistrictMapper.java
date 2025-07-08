package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobDistrict;

/**
 * 地区管理Mapper接口
 *
 * @author ruoyi
 * @date 2024-07-24
 */
public interface TobDistrictMapper
{
    /**
     * 查询地区管理
     *
     * @param districtId 地区管理主键
     * @return 地区管理
     */
    public TobDistrict selectTobDistrictByDistrictId(Long districtId);

    /**
     * 查询地区管理列表
     *
     * @param tobDistrict 地区管理
     * @return 地区管理集合
     */
    public List<TobDistrict> selectTobDistrictList(TobDistrict tobDistrict);

    /**
     * 新增地区管理
     *
     * @param tobDistrict 地区管理
     * @return 结果
     */
    public int insertTobDistrict(TobDistrict tobDistrict);

    /**
     * 修改地区管理
     *
     * @param tobDistrict 地区管理
     * @return 结果
     */
    public int updateTobDistrict(TobDistrict tobDistrict);

    /**
     * 删除地区管理
     *
     * @param districtId 地区管理主键
     * @return 结果
     */
    public int deleteTobDistrictByDistrictId(Long districtId);

    /**
     * 批量删除地区管理
     *
     * @param districtIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobDistrictByDistrictIds(Long[] districtIds);

    /**
     * 根据地区ID查询信息
     * @param districtId
     * @return
     */
    public TobDistrict selectTobDistrictByID(Long districtId);

    /**
     * 根据地区名查询地区ID
     */
    public Long getDistrictId(String districtName);
}
