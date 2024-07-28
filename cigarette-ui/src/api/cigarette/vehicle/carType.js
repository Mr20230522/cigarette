import request from '@/utils/request'

// 查询车型列表
export function listCarType(query) {
  return request({
    url: '/cigarette/vehicle/carType/list',
    method: 'get',
    params: query
  })
}

// 查询车型详细
export function getCarType(carTypeId) {
  return request({
    url: '/cigarette/vehicle/carType/' + carTypeId,
    method: 'get'
  })
}

// 新增车型
export function addCarType(data) {
  return request({
    url: '/cigarette/vehicle/carType',
    method: 'post',
    data: data
  })
}

// 修改车型
export function updateCarType(data) {
  return request({
    url: '/cigarette/vehicle/carType',
    method: 'put',
    data: data
  })
}

// 删除车型
export function delCarType(carTypeId) {
  return request({
    url: '/cigarette/vehicle/carType/' + carTypeId,
    method: 'delete'
  })
}
