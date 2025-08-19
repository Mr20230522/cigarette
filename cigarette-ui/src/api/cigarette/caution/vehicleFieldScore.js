import request from '@/utils/request'

// 查询车辆字段评分列表
export function listVehicleFieldScore(query) {
  return request({
    url: '/system/vehicleFieldScore/list',
    method: 'get',
    params: query
  })
}

// 获取车辆字段评分详细信息
export function getVehicleFieldScore(id) {
  return request({
    url: `/system/vehicleFieldScore/${id}`,
    method: 'get'
  })
}

// 根据trafficDataId获取评分信息
export function getByTrafficDataId(trafficDataId) {
  return request({
    url: '/system/vehicleFieldScore/byTrafficDataId',
    method: 'get',
    params: { trafficDataId }  // 通过 query 参数传递
  })
}

// 新增车辆字段评分
export function addVehicleFieldScore(data) {
  return request({
    url: '/system/vehicleFieldScore',
    method: 'post',
    data: data
  })
}

// 修改车辆字段评分
export function updateVehicleFieldScore(data) {
  return request({
    url: '/system/vehicleFieldScore',
    method: 'put',
    data: data
  })
}

// 删除车辆字段评分
export function deleteVehicleFieldScore(ids) {
  return request({
    url: `/system/vehicleFieldScore/${ids.join(',')}`,
    method: 'delete'
  })
}
