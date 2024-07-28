import request from '@/utils/request'

// 查询驾驶人员列表
export function listPerson(query) {
  return request({
    url: '/cigarette/personnel/person/list',
    method: 'get',
    params: query
  })
}

// 查询驾驶人员详细
export function getPerson(suspectId) {
  return request({
    url: '/cigarette/personnel/person/' + suspectId,
    method: 'get'
  })
}

// 新增驾驶人员
export function addPerson(data) {
  return request({
    url: '/cigarette/personnel/person',
    method: 'post',
    data: data
  })
}

// 修改驾驶人员
export function updatePerson(data) {
  return request({
    url: '/cigarette/personnel/person',
    method: 'put',
    data: data
  })
}

// 删除驾驶人员
export function delPerson(suspectId) {
  return request({
    url: '/cigarette/personnel/person/' + suspectId,
    method: 'delete'
  })
}
