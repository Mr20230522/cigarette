import request from '@/utils/request'

// 删除图片
export function deleteImage(data) {
  return request({
    url: '/common/delete',
    method: 'post',
    data: data
  })
}
