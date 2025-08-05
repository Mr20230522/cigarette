import request from '@/utils/request'

export function getFactors() {
  return request({
    url: '/cigarette/vehicle/factorConfig',
    method: 'get'
  })
}

export function updateFactors(data) {
  return request({
    url: '/cigarette/vehicle/factorConfig',
    method: 'post',
    data
  })
}
