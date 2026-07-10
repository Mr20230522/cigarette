import request from '@/utils/request'

/* 预警任务管理 API */

/* 查询任务列表 */
export function listTask(query) {
  return request({
    url: '/cigarette/alert/task/list',
    method: 'get',
    params: query
  })
}

/* 查询任务详情 */
export function getTask(id) {
  return request({
    url: '/cigarette/alert/task/' + id,
    method: 'get'
  })
}

/* 新增任务 */
export function addTask(data) {
  return request({
    url: '/cigarette/alert/task',
    method: 'post',
    data: data
  })
}

/* 修改任务 */
export function updateTask(data) {
  return request({
    url: '/cigarette/alert/task',
    method: 'put',
    data: data
  })
}

/* 删除任务 */
export function delTask(ids) {
  return request({
    url: '/cigarette/alert/task/' + ids,
    method: 'delete'
  })
}

/* 接受任务 */
export function acceptTask(id) {
  return request({
    url: '/cigarette/alert/task/accept/' + id,
    method: 'put'
  })
}

/* 拒绝任务 */
export function rejectTask(id) {
  return request({
    url: '/cigarette/alert/task/reject/' + id,
    method: 'put'
  })
}

/* 关闭任务 */
export function closeTask(id) {
  return request({
    url: '/cigarette/alert/task/close/' + id,
    method: 'put'
  })
}

/* 提交反馈 */
export function submitFeedback(data) {
  return request({
    url: '/cigarette/alert/task/feedback',
    method: 'post',
    data: data
  })
}

/* 手动推送任务 */
export function pushTask(id) {
  return request({
    url: '/cigarette/alert/task/push/' + id,
    method: 'put'
  })
}
