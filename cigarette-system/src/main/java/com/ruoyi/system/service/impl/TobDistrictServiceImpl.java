package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import com.ruoyi.system.domain.TobTreeSelect;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.spring.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobDistrictMapper;
import com.ruoyi.system.domain.TobDistrict;
import com.ruoyi.system.service.ITobDistrictService;

/**
 * 地区管理Service业务层处理
 *
 * @author ruoyi
 * @date 2024-07-24
 */
@Service
public class TobDistrictServiceImpl implements ITobDistrictService
{
    @Autowired
    private TobDistrictMapper tobDistrictMapper;

    /**
     * 查询地区管理
     *
     * @param districtId 地区管理主键
     * @return 地区管理
     */
    @Override
    public TobDistrict selectTobDistrictByDistrictId(Long districtId)
    {
        return tobDistrictMapper.selectTobDistrictByDistrictId(districtId);
    }

    /**
     * 查询地区管理列表
     *
     * @param tobDistrict 地区管理
     * @return 地区管理
     */
    @Override
    public List<TobDistrict> selectTobDistrictList(TobDistrict tobDistrict)
    {
        return tobDistrictMapper.selectTobDistrictList(tobDistrict);
    }

    /**
     * 新增地区管理
     *
     * @param tobDistrict 地区管理
     * @return 结果
     */
    @Override
    public int insertTobDistrict(TobDistrict tobDistrict)
    {
        TobDistrict info = tobDistrictMapper.selectTobDistrictByID(tobDistrict.getParentId());
        tobDistrict.setCreateTime(DateUtils.getNowDate());
        tobDistrict.setAncestors(info.getAncestors()+","+tobDistrict.getParentId());
        return tobDistrictMapper.insertTobDistrict(tobDistrict);
    }

    /**
     * 修改地区管理
     *
     * @param tobDistrict 地区管理
     * @return 结果
     */
    @Override
    public int updateTobDistrict(TobDistrict tobDistrict)
    {
        tobDistrict.setUpdateTime(DateUtils.getNowDate());
        return tobDistrictMapper.updateTobDistrict(tobDistrict);
    }

    /**
     * 批量删除地区管理
     *
     * @param districtIds 需要删除的地区管理主键
     * @return 结果
     */
    @Override
    public int deleteTobDistrictByDistrictIds(Long[] districtIds)
    {
        return tobDistrictMapper.deleteTobDistrictByDistrictIds(districtIds);
    }

    /**
     * 删除地区管理信息
     *
     * @param districtId 地区管理主键
     * @return 结果
     */
    @Override
    public int deleteTobDistrictByDistrictId(Long districtId)
    {
        return tobDistrictMapper.deleteTobDistrictByDistrictId(districtId);
    }

    /**
     * 根据地区ID查询信息
     * @param districtId
     * @return
     */
    @Override
    public TobDistrict selectTobDistrictByID(Long districtId)
        {
            return tobDistrictMapper.selectTobDistrictByID(districtId);
        }

    /**
     * 查询地区树结构信息
     * @param district
     * @return
     */
    @Override
    public List<TobTreeSelect> selectTobDistrictTreeList(TobDistrict district) {
        List<TobDistrict> tobDistricts = this.selectTobDistrictList(district);
        return buildTobDistrictTreeSelect(tobDistricts);
    }
    /**
     * 构建前端所需要下拉树结构
     *
     * @param tobDistricts 地区列表
     * @return 下拉树结构列表
     */
    @Override
    public List<TobTreeSelect> buildTobDistrictTreeSelect(List<TobDistrict> tobDistricts){
        List<TobDistrict> districtTrees = buildTobDistrictTree(tobDistricts);
        return districtTrees.stream().map(TobTreeSelect::new).collect(Collectors.toList());
    }

    /**
     * 构建前端所需要树结构
     *
     * @param districts 部门列表
     * @return 树结构列表
     */
    @Override
    public List<TobDistrict> buildTobDistrictTree(List<TobDistrict> districts)
    {
        List<TobDistrict> returnList = new ArrayList<TobDistrict>();
        List<Long> tempList = districts.stream().map(TobDistrict::getDistrictId).collect(Collectors.toList());
        for (TobDistrict district : districts)
        {
            // 如果是顶级节点, 遍历该父节点的所有子节点
            if (!tempList.contains(district.getParentId()))
            {
                recursionFn(districts, district);
                returnList.add(district);
            }
        }
        if (returnList.isEmpty())
        {
            returnList = districts;
        }
        return returnList;
    }
    /**
     * 递归列表
     */
    private void recursionFn(List<TobDistrict> list, TobDistrict t)
    {
        // 得到子节点列表
        List<TobDistrict> childList = getChildList(list, t);
        t.setChildren(childList);
        for (TobDistrict tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }
    /**
     * 得到子节点列表
     */
    private List<TobDistrict> getChildList(List<TobDistrict> list, TobDistrict t)
    {
        List<TobDistrict> tlist = new ArrayList<TobDistrict>();
        Iterator<TobDistrict> it = list.iterator();
        while (it.hasNext())
        {
            TobDistrict n = (TobDistrict) it.next();
            if (StringUtils.isNotNull(n.getParentId()) && n.getParentId().longValue() == t.getDistrictId().longValue())
            {
                tlist.add(n);
            }
        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<TobDistrict> list, TobDistrict t)
    {
        return getChildList(list, t).size() > 0;
    }


    public Long getDistrictId(String districtName ){
        System.out.println("!!!districtName!!!");
        return tobDistrictMapper.getDistrictId(districtName);

    }
}
