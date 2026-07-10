import request from '@/utils/request'

/* 企微配置 API */

/* 查询企微配置列表 */
export function listWxworkConfig(query) {
  return request({
    url: '/cigarette/alert/wxworkConfig/list',
    method: 'get',
    params: query
  })
}

/* 查询企微配置详情 */
export function getWxworkConfig(id) {
  return request({
    url: '/cigarette/alert/wxworkConfig/' + id,
    method: 'get'
  })
}

/* 新增企微配置 */
export function addWxworkConfig(data) {
  return request({
    url: '/cigarette/alert/wxworkConfig',
    method: 'post',
    data: data
  })
}

/* 修改企微配置 */
export function updateWxworkConfig(data) {
  return request({
    url: '/cigarette/alert/wxworkConfig',
    method: 'put',
    data: data
  })
}

/* 删除企微配置 */
export function delWxworkConfig(ids) {
  return request({
    url: '/cigarette/alert/wxworkConfig/' + ids,
    method: 'delete'
  })
}
