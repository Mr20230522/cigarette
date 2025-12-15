package com.ruoyi.system.service;

import com.ruoyi.system.domain.vo.ToVehicleBehaviorMediaVo;


public interface ITobVehicleBehaviorMediaService {

        /**
         * 获取车辆行为关联的媒体文件信息
         * @param behaviorId 行为ID
         * @return 媒体文件信息VO
         */
        ToVehicleBehaviorMediaVo getBehaviorMedia(Long behaviorId);
}
