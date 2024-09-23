import request from '@/utils/request'


// 删除
export function deleteImage(fileName) {
  return request({
    url: '/common/common/' + fileName,
    method: 'delete'
  })
}
