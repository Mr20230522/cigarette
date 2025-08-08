import request from '@/utils/request'

export function getList(params) {
  return request({
    url: '/factor/trigger/list',
    method: 'get',
    params
  })
}

export function getInfo(id) {
  return request({
    url: `/factor/trigger/${id}`,
    method: 'get'
  })
}

export function add(data) {
  return request({
    url: '/factor/trigger',
    method: 'post',
    data
  })
}

export function edit(data) {
  return request({
    url: '/factor/trigger',
    method: 'put',
    data
  })
}

export function del(id) {
  return request({
    url: `/factor/trigger/${id}`,
    method: 'delete'
  })
}
