import request from '@/utils/request'

/* 推送日志 API */

/* 查询推送日志列表 */
export function listPushLog(query) {
  return request({
    url: '/cigarette/alert/pushLog/list',
    method: 'get',
    params: query
  })
}

/* 根据任务ID查询日志 */
export function listPushLogByTask(taskId) {
  return request({
    url: '/cigarette/alert/pushLog/byTask/' + taskId,
    method: 'get'
  })
}
