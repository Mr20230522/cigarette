import request from '@/utils/request'

// 查询执勤记录列表
export function listDutyLog(query) {
  return request({
    url: '/cigarette/detection/dutyLog/list',
    method: 'get',
    params: query
  })
}

// 查询执勤记录详细
export function getDutyLog(dutyId) {
  return request({
    url: '/cigarette/detection/dutyLog/' + dutyId,
    method: 'get'
  })
}

// 新增执勤记录
export function addDutyLog(data) {
  return request({
    url: '/cigarette/detection/dutyLog',
    method: 'post',
    data: data
  })
}

// 修改执勤记录
export function updateDutyLog(data) {
  return request({
    url: '/cigarette/detection/dutyLog',
    method: 'put',
    data: data
  })
}

// 删除执勤记录
export function delDutyLog(dutyId) {
  return request({
    url: '/cigarette/detection/dutyLog/' + dutyId,
    method: 'delete'
  })
}
