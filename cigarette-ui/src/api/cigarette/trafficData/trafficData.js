import request from '@/utils/request'

//最新十条数据
export function tenList(query) {
  return request({
    url: '/cigarette/trafficData/tenList',
    method: 'get',
    params: query
  })
}


//比id大的数据获取
export function overIdList(query) {
  return request({
    url: '/cigarette/trafficData/overIdList',
    method: 'get',
    params: query
  })
}

//获取所有的数据
export function allList(query) {
  return request({
    url: '/cigarette/trafficData/allList',
    method: 'get',
    params: query
  })
}


//通过id获取视屏地址
export function byIdGetVideoPath(query) {
  return request({
    url: '/cigarette/trafficData/byIdGetVideoPath',
    method: 'get',
    params: query
  })
}

//获取复合条件的数据
export function searchVehicle(query) {
  return request({
    url: '/cigarette/trafficData/searchVehicle',
    method: 'get',
    params: query
  })
}


