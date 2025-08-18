//package com.ruoyi.system.mapper;
//
//import com.ruoyi.system.domain.ToVehicleFieldScore;
//import java.util.List;
//
///**
// * 车辆各字段得分Mapper接口
// */
//public interface ToVehicleFieldScoreMapper {
//    /**
//     * 查询车辆各字段得分
//     */
//    ToVehicleFieldScore selectToVehicleFieldScoreById(Long id);
//
//    /**
//     * 查询车辆各字段得分列表
//     */
//    List<ToVehicleFieldScore> selectToVehicleFieldScoreList(ToVehicleFieldScore toVehicleFieldScore);
//
//    /**
//     * 新增车辆各字段得分
//     */
//    int insertToVehicleFieldScore(ToVehicleFieldScore toVehicleFieldScore);
//
//    /**
//     * 修改车辆各字段得分（按车牌更新）
//     */
//    int updateToVehicleFieldScoreByPlate(ToVehicleFieldScore toVehicleFieldScore);
//
//    /**
//     * 删除车辆各字段得分
//     */
//    int deleteToVehicleFieldScoreById(Long id);
//
//    /**
//     * 根据车牌查询
//     */
//    ToVehicleFieldScore selectByPlate(String plate);
//}
package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ToVehicleFieldScore;
import java.util.List;

/**
 * 车辆各字段得分Mapper接口
 */
public interface ToVehicleFieldScoreMapper {
    /**
     * 查询车辆各字段得分
     */
    ToVehicleFieldScore selectToVehicleFieldScoreById(Long id);

    /**
     * 查询车辆各字段得分列表
     */
    List<ToVehicleFieldScore> selectToVehicleFieldScoreList(ToVehicleFieldScore toVehicleFieldScore);

    /**
     * 新增车辆各字段得分
     */
    int insertToVehicleFieldScore(ToVehicleFieldScore toVehicleFieldScore);

    /**
     * 修改车辆各字段得分（按 trafficdata_id）
     */
    int updateToVehicleFieldScoreByTrafficdataId(ToVehicleFieldScore toVehicleFieldScore);

    /**
     * 删除车辆各字段得分
     */
    int deleteToVehicleFieldScoreById(Long id);

    /**
     * 根据 trafficdata_id 查询
     */
    ToVehicleFieldScore selectByTrafficdataId(Long trafficdataId);

    int deleteToVehicleFieldScoreByIds(String ids);
}