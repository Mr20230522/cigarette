import request from '@/utils/request'

// 查询地区管理列表
export function listDistrict(query) {
  return request({
    url: '/cigarette/detection/district/list',
    method: 'get',
    params: query
  })
}

// 查询地区管理详细
export function getDistrict(districtId) {
  return request({
    url: '/cigarette/detection/district/' + districtId,
    method: 'get'
  })
}

// 新增地区管理
export function addDistrict(data) {
  return request({
    url: '/cigarette/detection/district',
    method: 'post',
    data: data
  })
}

// 修改地区管理
export function updateDistrict(data) {
  return request({
    url: '/cigarette/detection/district',
    method: 'put',
    data: data
  })
}

// 删除地区管理
export function delDistrict(districtId) {
  return request({
    url: '/cigarette/detection/district/' + districtId,
    method: 'delete'
  })
}

//查询地区下拉树结构
export function districtTreeSelect() { 
  return request({ 
    url: '/cigarette/detection/district/districtTree', 
    method: 'get' 
  }) 
}