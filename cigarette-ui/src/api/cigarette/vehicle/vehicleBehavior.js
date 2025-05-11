import request from '@/utils/request'

// 查询车辆行为记录列表
export function listVehicleBehavior(query) {
  return request({
    url: '/cigarette/vehicle/vehicleBehavior/list',
    method: 'get',
    params: query
  })
}

// 查询车辆行为记录列表
export function listVehicleBehaviorVo(query) {
  return request({
    url: '/cigarette/vehicle/vehicleBehavior/listVo',
    method: 'get',
    params: query
  })
}
// 查询车辆行为记录详细
export function getVehicleBehavior(behaviorId) {
  return request({
    url: '/cigarette/vehicle/vehicleBehavior/' + behaviorId,
    method: 'get'
  })
}

// 新增车辆行为记录
export function addVehicleBehavior(data) {
  return request({
    url: '/cigarette/vehicle/vehicleBehavior',
    method: 'post',
    data: data
  })
}

// 修改车辆行为记录
export function updateVehicleBehavior(data) {
  return request({
    url: '/cigarette/vehicle/vehicleBehavior',
    method: 'put',
    data: data
  })
}

// 删除车辆行为记录
export function delVehicleBehavior(behaviorId) {
  return request({
    url: '/cigarette/vehicle/vehicleBehavior/' + behaviorId,
    method: 'delete'
  })
}


