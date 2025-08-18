import request from '@/utils/request'

// 获取当前嫌疑度值
export function getSuspicionLevel() {
  return request({
    url: '/system/suspicion/level',
    method: 'get'
  })
}

// 更新嫌疑度值
export function updateSuspicionLevel(level) {
  return request({
    url: '/system/suspicion/level',
    method: 'post',
    params: { level }
  })
}
