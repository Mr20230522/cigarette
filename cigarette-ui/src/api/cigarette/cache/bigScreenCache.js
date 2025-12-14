import request from '@/utils/request'

// 1. 相机缓存
export function getCameraCache() {
  return request({
    url: 'cigarette/cache/selectCameraOne',
    method: 'get'
  })
}

// 2. 昼夜缓存
export function getDaynightCache() {
  return request({
    url: 'cigarette/cache/selectDaynightOne',
    method: 'get'
  })
}

// 3. 日切片缓存
export function getDaySliceCache() {
  return request({
    url: 'cigarette/cache/selectDaySliceOne',
    method: 'get'
  })
}

// 4. 小时缓存
export function getHourCache() {
  return request({
    url: 'cigarette/cache/selectHourOne',
    method: 'get'
  })
}

// 5. 季节缓存
export function getSeasonCache() {
  return request({
    url: 'cigarette/cache/selectSeasonOne',
    method: 'get'
  })
}

// 6. 车辆颜色缓存
export function getVehicleColorCache() {
  return request({
    url: 'cigarette/cache/selectVehicleColorOne',
    method: 'get'
  })
}

// 7. 车辆类型缓存
export function getVehicleTypeCache() {
  return request({
    url: 'cigarette/cache/selectVehicleTypeOne',
    method: 'get'
  })
}
