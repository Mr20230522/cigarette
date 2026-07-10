import request from '@/utils/request'

/* 反馈结果 API */

/* 查询反馈列表 */
export function listFeedback(query) {
  return request({
    url: '/cigarette/alert/feedback/list',
    method: 'get',
    params: query
  })
}

/* 查询反馈详情 */
export function getFeedback(id) {
  return request({
    url: '/cigarette/alert/feedback/' + id,
    method: 'get'
  })
}
