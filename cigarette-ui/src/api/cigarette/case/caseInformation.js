import request from '@/utils/request'

// 查询案件信息列表
export function listCaseInformation(query) {
  return request({
    url: '/cigarette/case/caseInformation/list',
    method: 'get',
    params: query
  })
}


// 查询案件信息列表Vo
export function listCaseInformationVo(query) {
  return request({
    url: '/cigarette/case/caseInformation/listVo',
    method: 'get',
    params: query
  })
}


// 查询案件信息详细
export function getCaseInformation(caseId) {
  return request({
    url: '/cigarette/case/caseInformation/' + caseId,
    method: 'get'
  })
}

// 新增案件信息
export function addCaseInformation(data) {
  return request({
    url: '/cigarette/case/caseInformation',
    method: 'post',
    data: data
  })
}

// 修改案件信息
export function updateCaseInformation(data) {
  return request({
    url: '/cigarette/case/caseInformation',
    method: 'put',
    data: data
  })
}

// 删除案件信息
export function delCaseInformation(caseId) {
  return request({
    url: '/cigarette/case/caseInformation/' + caseId,
    method: 'delete'
  })
}
