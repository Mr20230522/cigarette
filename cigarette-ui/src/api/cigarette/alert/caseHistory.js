import request from '@/utils/request'

// 历史案件 API

// 查询历史案件列表（支持车牌模糊搜索）
export function listCaseHistory(query) {
  return request({
    url: '/cigarette/alert/caseHistory/list',
    method: 'get',
    params: query
  })
}

// 查询历史案件详情
export function getCaseHistory(id) {
  return request({
    url: '/cigarette/alert/caseHistory/' + id,
    method: 'get'
  })
}

// 新增历史案件
export function addCaseHistory(data) {
  return request({
    url: '/cigarette/alert/caseHistory',
    method: 'post',
    data: data
  })
}

// 修改历史案件
export function updateCaseHistory(data) {
  return request({
    url: '/cigarette/alert/caseHistory',
    method: 'put',
    data: data
  })
}
