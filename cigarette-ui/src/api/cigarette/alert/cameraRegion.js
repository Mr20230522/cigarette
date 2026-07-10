import request from '@/utils/request'

/* 卡口地域映射 API */

/* 查询卡口地域映射列表 */
export function listCameraRegion(query) {
  return request({
    url: '/cigarette/alert/cameraRegion/list',
    method: 'get',
    params: query
  })
}

/* 查询卡口地域映射详情 */
export function getCameraRegion(id) {
  return request({
    url: '/cigarette/alert/cameraRegion/' + id,
    method: 'get'
  })
}

/* 新增卡口地域映射 */
export function addCameraRegion(data) {
  return request({
    url: '/cigarette/alert/cameraRegion',
    method: 'post',
    data: data
  })
}

/* 修改卡口地域映射 */
export function updateCameraRegion(data) {
  return request({
    url: '/cigarette/alert/cameraRegion',
    method: 'put',
    data: data
  })
}

/* 删除卡口地域映射 */
export function delCameraRegion(ids) {
  return request({
    url: '/cigarette/alert/cameraRegion/' + ids,
    method: 'delete'
  })
}
