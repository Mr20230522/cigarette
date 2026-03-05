import request from '@/utils/request'

// 查询工作人员列表
export function listStaff(query) {
  return request({
    url: '/cigarette/personnel/staff/list',
    method: 'get',
    params: query
  })
}

// 查询工作人员详细
export function getStaff(staffId) {
  return request({
    url: '/cigarette/personnel/staff/' + staffId,
    method: 'get'
  })
}

// 新增工作人员
export function addStaff(data) {
  return request({
    url: '/cigarette/personnel/staff',
    method: 'post',
    data: data
  })
}

// 修改工作人员
export function updateStaff(data) {
  return request({
    url: '/cigarette/personnel/staff',
    method: 'put',
    data: data
  })
}

// 删除工作人员
export function delStaff(staffId) {
  return request({
    url: '/cigarette/personnel/staff/' + staffId,
    method: 'delete'
  })


}
// 根据 userId 查询工作人员信息
export function getStaffByUserId(userId) {
  return request({
    url: '/cigarette/personnel/staff/getByUserId/' + userId,
    method: 'get'
  })
}
