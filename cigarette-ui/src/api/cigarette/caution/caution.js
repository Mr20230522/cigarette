import request from '@/utils/request'

// 查询预警记录列表
export function listCaution(query) {
  return request({
    url: '/cigarette/caution/cautionLog/list',
    method: 'get',
    params: query
  })
}

// 查询预警记录详细
export function getCaution(cautionId) {
  return request({
    url: '/cigarette/caution/cautionLog/' + cautionId,
    method: 'get'
  })
}

// 新增预警记录
export function addCaution(data) {
  return request({
    url: '/cigarette/caution/cautionLog',
    method: 'post',
    data: data
  })
}

// 修改预警记录
export function updateCaution(data) {
  return request({
    url: '/cigarette/caution/cautionLog',
    method: 'put',
    data: data
  })
}

// 删除预警记录
export function delCaution(cautionId) {
  return request({
    url: '/cigarette/caution/cautionLog/' + cautionId,
    method: 'delete'
  })
}
