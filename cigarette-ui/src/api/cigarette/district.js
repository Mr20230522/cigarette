import request from '@/utils/request'

// 查询地区列表
export function listDistrict(query) {
  return request({
    url: '/system/district/list',
    method: 'get',
    params: query
  })
}

// 查询地区详细
export function getDistrict(districtId) {
  return request({
    url: '/system/district/' + districtId,
    method: 'get'
  })
}

// 新增地区
export function addDistrict(data) {
  return request({
    url: '/system/district',
    method: 'post',
    data: data
  })
}

// 修改地区
export function updateDistrict(data) {
  return request({
    url: '/system/district',
    method: 'put',
    data: data
  })
}

// 删除地区
export function delDistrict(districtId) {
  return request({
    url: '/system/district/' + districtId,
    method: 'delete'
  })
}
