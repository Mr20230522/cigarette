import request from '@/utils/request'

/* 预警接收记录 API */

/* 查询接收记录列表 */
export function listReceive(query) {
  return request({
    url: '/cigarette/alert/receive/list',
    method: 'get',
    params: query
  })
}

/* 查询接收记录详情 */
export function getReceive(id) {
  return request({
    url: '/cigarette/alert/receive/' + id,
    method: 'get'
  })
}
