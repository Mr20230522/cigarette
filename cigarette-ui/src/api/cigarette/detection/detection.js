import request from '@/utils/request'

// 查询监测区域列表
export function listDetection(query) {
  return request({
    url: '/cigarette/detection/detection/list',
    method: 'get',
    params: query
  })
}

// 查询监测区域详细
export function getDetection(detectionId) {
  return request({
    url: '/cigarette/detection/detection/' + detectionId,
    method: 'get'
  })
}

// 新增监测区域
export function addDetection(data) {
  return request({
    url: '/cigarette/detection/detection',
    method: 'post',
    data: data
  })
}

// 修改监测区域
export function updateDetection(data) {
  return request({
    url: '/cigarette/detection/detection',
    method: 'put',
    data: data
  })
}

// 删除监测区域
export function delDetection(detectionId) {
  return request({
    url: '/cigarette/detection/detection/' + detectionId,
    method: 'delete'
  })
}
