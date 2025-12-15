package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.vo.ToVehicleBehaviorMediaVo;
import com.ruoyi.system.mapper.ToVehicleBehaviorMediaMapper;
import com.ruoyi.system.service.ITobVehicleBehaviorMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TobVehicleBehaviorMediaServiceImpl implements ITobVehicleBehaviorMediaService {
    @Autowired
    private ToVehicleBehaviorMediaMapper VehicleBehaviorMediaMapper;
    @Override
    public ToVehicleBehaviorMediaVo getBehaviorMedia(Long behaviorId) {
        return VehicleBehaviorMediaMapper.selectBehaviorMedia(behaviorId);
    }
}
