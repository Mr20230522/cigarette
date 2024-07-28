import request from '@/utils/request'

// 查询值班列表
export function listSchedule(query) {
  return request({
    url: '/cigarette/personnel/schedule/list',
    method: 'get',
    params: query
  })
}

// 查询值班详细
export function getSchedule(dutyId) {
  return request({
    url: '/cigarette/personnel/schedule/' + dutyId,
    method: 'get'
  })
}

// 新增值班
export function addSchedule(data) {
  return request({
    url: '/cigarette/personnel/schedule',
    method: 'post',
    data: data
  })
}

// 修改值班
export function updateSchedule(data) {
  return request({
    url: '/cigarette/personnel/schedule',
    method: 'put',
    data: data
  })
}

// 删除值班
export function delSchedule(dutyId) {
  return request({
    url: '/cigarette/personnel/schedule/' + dutyId,
    method: 'delete'
  })
}
