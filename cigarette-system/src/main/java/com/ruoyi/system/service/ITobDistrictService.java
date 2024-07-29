package com.ruoyi.system.service;

import java.util.List;

import com.ruoyi.system.domain.TobTreeSelect;
import com.ruoyi.system.domain.TobDistrict;


/**
 * 地区管理Service接口
 *
 * @author ruoyi
 * @date 2024-07-24
 */
public interface ITobDistrictService
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
     * 批量删除地区管理
     *
     * @param districtIds 需要删除的地区管理主键集合
     * @return 结果
     */
    public int deleteTobDistrictByDistrictIds(Long[] districtIds);

    /**
     * 删除地区管理信息
     *
     * @param districtId 地区管理主键
     * @return 结果
     */
    public int deleteTobDistrictByDistrictId(Long districtId);

    /**
     * 根据地区ID查询信息
     * @param districtId
     * @return
     */
    public TobDistrict selectTobDistrictByID(Long districtId);

    /**
     * 查询地区树结构信息
     * @param district
     * @return
     */
    public List<TobTreeSelect> selectTobDistrictTreeList(TobDistrict district);

    /**
     * 构建前端所需要下拉树结构
     *
     * @param districts 地区列表
     * @return 下拉树结构列表
     */
    public List<TobTreeSelect> buildTobDistrictTreeSelect(List<TobDistrict> districts);

    /**
     * 构建前端所需要树结构
     *
     * @param districts 部门列表
     * @return 树结构列表
     */
    public List<TobDistrict> buildTobDistrictTree(List<TobDistrict> districts);

}
