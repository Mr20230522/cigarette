import request from '@/utils/request'

// 查询出勤记录列表
export function listLog(query) {
  return request({
    url: '/cigarette/personnel/clockLog/list',
    method: 'get',
    params: query
  })
}

// 查询出勤记录详细
export function getLog(commutingId) {
  return request({
    url: '/cigarette/personnel/clockLog/' + commutingId,
    method: 'get'
  })
}

// 新增出勤记录
export function addLog(data) {
  return request({
    url: '/cigarette/personnel/clockLog',
    method: 'post',
    data: data
  })
}

// 修改出勤记录
export function updateLog(data) {
  return request({
    url: '/cigarette/personnel/clockLog',
    method: 'put',
    data: data
  })
}

// 删除出勤记录
export function delLog(commutingId) {
  return request({
    url: '/cigarette/personnel/clockLog/' + commutingId,
    method: 'delete'
  })
}
