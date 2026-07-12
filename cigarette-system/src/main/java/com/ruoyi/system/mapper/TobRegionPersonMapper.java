package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TobRegionPerson;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 地域人员绑定Mapper接口
 */
public interface TobRegionPersonMapper {

    /**
     * 查询地域人员绑定
     */
    TobRegionPerson selectTobRegionPersonById(Long id);

    /**
     * 根据地域ID查询绑定人员列表
     */
    List<TobRegionPerson> selectTobRegionPersonByLocationId(Integer locationId);

    /**
     * 查询地域人员绑定列表
     */
    List<TobRegionPerson> selectTobRegionPersonList(TobRegionPerson tobRegionPerson);

    /**
     * 新增地域人员绑定
     */
    int insertTobRegionPerson(TobRegionPerson tobRegionPerson);

    /**
     * 修改地域人员绑定
     */
    int updateTobRegionPerson(TobRegionPerson tobRegionPerson);

    /**
     * 删除地域人员绑定
     */
    int deleteTobRegionPersonById(Long id);

    /**
     * 批量删除地域人员绑定
     */
    int deleteTobRegionPersonByIds(Long[] ids);

    /**
     * 根据企微userId查询人员信息（用于OAuth自动注册时获取昵称）
     */
    TobRegionPerson selectDistinctByUserId(@Param("userId") String userId);
}
