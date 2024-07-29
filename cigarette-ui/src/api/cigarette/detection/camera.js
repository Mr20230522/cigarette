import request from '@/utils/request'

// 查询摄像头列表
export function listCamera(query) {
  return request({
    url: '/cigarette/detection/camera/list',
    method: 'get',
    params: query
  })
}

// 查询摄像头详细
export function getCamera(cameraId) {
  return request({
    url: '/cigarette/detection/camera/' + cameraId,
    method: 'get'
  })
}

// 新增摄像头
export function addCamera(data) {
  return request({
    url: '/cigarette/detection/camera',
    method: 'post',
    data: data
  })
}

// 修改摄像头
export function updateCamera(data) {
  return request({
    url: '/cigarette/detection/camera',
    method: 'put',
    data: data
  })
}

// 删除摄像头
export function delCamera(cameraId) {
  return request({
    url: '/cigarette/detection/camera/' + cameraId,
    method: 'delete'
  })
}
