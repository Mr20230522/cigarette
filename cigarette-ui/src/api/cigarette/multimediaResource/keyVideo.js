import request from '@/utils/request'

// 查询关键帧视频列表
export function listVideo(query) {
  return request({
    url: '/cigarette/multimediaResource/keyVideo/list',
    method: 'get',
    params: query
  })
}

// 查询关键帧视频详细
export function getVideo(videoId) {
  return request({
    url: '/cigarette/multimediaResource/keyVideo/' + videoId,
    method: 'get'
  })
}



// 新增关键帧视频
export function addVideo(data) {
  return request({
    url: '/cigarette/multimediaResource/keyVideo',
    method: 'post',
    data: data
  })
}

// 修改关键帧视频
export function updateVideo(data) {
  return request({
    url: '/cigarette/multimediaResource/keyVideo',
    method: 'put',
    data: data
  })
}

// 删除关键帧视频
export function delVideo(videoId) {
  return request({
    url: '/cigarette/multimediaResource/keyVideo/' + videoId,
    method: 'delete'
  })
}


// 用户视频上传
export function uploadVideo(data) {
  return request({
    url: '/cigarette/multimediaResource/keyVideo/uploadVideo',
    method: 'post',
    data: data
  })
}


// 根据行为ID查询关键帧视频详细
export function listVideoByBehaviorId(behaviorId) {
  return request({
    url: '/cigarette/multimediaResource/keyVideo/behavior/' + behaviorId,
    method: 'get'
  })
}