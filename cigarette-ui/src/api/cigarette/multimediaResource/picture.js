import request from '@/utils/request'

// 查询关键帧图片列表
export function listPicture(query) {
  return request({
    url: '/cigarette/multimediaResource/picture/list',
    method: 'get',
    params: query
  })
}

// 查询关键帧图片详细
export function getPicture(keyPictureId) {
  return request({
    url: '/cigarette/multimediaResource/picture/' + keyPictureId,
    method: 'get'
  })
}



// 新增关键帧图片
export function addPicture(data) {
  return request({
    url: '/cigarette/multimediaResource/picture',
    method: 'post',
    data: data
  })
}

// 修改关键帧图片
export function updatePicture(data) {
  return request({
    url: '/cigarette/multimediaResource/picture',
    method: 'put',
    data: data
  })
}

// 删除关键帧图片
export function delPicture(keyPictureId) {
  return request({
    url: '/cigarette/multimediaResource/picture/' + keyPictureId,
    method: 'delete'
  })
}


// 根据行为ID查询关键帧图片详细
export function listPictureByBehaviorId(behaviorId) {
  return request({
    url: '/cigarette/multimediaResource/picture/behavior/' + behaviorId,
    method: 'get'
  })
}