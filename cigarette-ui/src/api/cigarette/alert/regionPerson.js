import request from '@/utils/request'

/* 地域人员绑定 API */

/* 查询地域人员绑定列表 */
export function listRegionPerson(query) {
  return request({
    url: '/cigarette/alert/regionPerson/list',
    method: 'get',
    params: query
  })
}

/* 查询地域人员绑定详情 */
export function getRegionPerson(id) {
  return request({
    url: '/cigarette/alert/regionPerson/' + id,
    method: 'get'
  })
}

/* 新增地域人员绑定 */
export function addRegionPerson(data) {
  return request({
    url: '/cigarette/alert/regionPerson',
    method: 'post',
    data: data
  })
}

/* 修改地域人员绑定 */
export function updateRegionPerson(data) {
  return request({
    url: '/cigarette/alert/regionPerson',
    method: 'put',
    data: data
  })
}

/* 删除地域人员绑定 */
export function delRegionPerson(ids) {
  return request({
    url: '/cigarette/alert/regionPerson/' + ids,
    method: 'delete'
  })
}

/* 获取地域下拉选项 */
export function listLocations() {
  return request({
    url: '/cigarette/alert/cameraRegion/locations',
    method: 'get'
  })
}
