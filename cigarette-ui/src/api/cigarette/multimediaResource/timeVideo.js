import request from '@/utils/request'

// 查询片段化视频列表
export function listVideo(query) {
  return request({
    url: '/cigarette/multimediaResource/timeVideo/list',
    method: 'get',
    params: query
  })
}

// 查询片段化视频详细
export function getVideo(videoId) {
  return request({
    url: '/cigarette/multimediaResource/timeVideo/' + videoId,
    method: 'get'
  })
}

// 新增片段化视频
export function addVideo(data) {
  return request({
    url: '/cigarette/multimediaResource/timeVideo',
    method: 'post',
    data: data
  })
}

// 修改片段化视频
export function updateVideo(data) {
  return request({
    url: '/cigarette/multimediaResource/timeVideo',
    method: 'put',
    data: data
  })
}

// 删除片段化视频
export function delVideo(videoId) {
  return request({
    url: '/cigarette/multimediaResource/timeVideo/' + videoId,
    method: 'delete'
  })
}
