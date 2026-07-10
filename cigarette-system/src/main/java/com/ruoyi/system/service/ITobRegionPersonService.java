package com.ruoyi.system.service;

import com.ruoyi.system.domain.TobRegionPerson;
import java.util.List;

/**
 * 地域人员绑定Service接口
 */
public interface ITobRegionPersonService {

    /**
     * 查询地域人员绑定
     */
    TobRegionPerson selectTobRegionPersonById(Long id);

    /**
     * 根据地域ID查询绑定人员列表（按优先级排序）
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
    int deleteTobRegionPersonByIds(Long[] ids);
}
