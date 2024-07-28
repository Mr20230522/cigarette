import request from '@/utils/request'

// 查询检测点人员列表
export function listInspector(query) {
  return request({
    url: '/cigarette/detection/inspector/list',
    method: 'get',
    params: query
  })
}

// 查询检测点人员详细
export function getInspector(inspectorId) {
  return request({
    url: '/cigarette/detection/inspector/' + inspectorId,
    method: 'get'
  })
}

// 新增检测点人员
export function addInspector(data) {
  return request({
    url: '/cigarette/detection/inspector',
    method: 'post',
    data: data
  })
}

// 修改检测点人员
export function updateInspector(data) {
  return request({
    url: '/cigarette/detection/inspector',
    method: 'put',
    data: data
  })
}

// 删除检测点人员
export function delInspector(inspectorId) {
  return request({
    url: '/cigarette/detection/inspector/' + inspectorId,
    method: 'delete'
  })
}
