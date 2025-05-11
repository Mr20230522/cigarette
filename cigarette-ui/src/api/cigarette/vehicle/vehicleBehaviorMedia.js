import request from '@/utils/request'
// 获取车辆行为媒体文件
export function getBehaviorMedia(behaviorId) {
  return request({
    url: '/cigarette/vehicle/vehicleBehaviorMedia/' + behaviorId,
    method: 'get'
  })
}
