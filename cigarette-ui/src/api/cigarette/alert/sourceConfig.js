import request from '@/utils/request'

// 查询预警数据源配置（复用 wxworkConfig 的 list 接口）
export function getSourceConfig() {
  return request({
    url: '/cigarette/alert/wxworkConfig/list',
    method: 'get',
    params: { pageNum: 1, pageSize: 100 }
  })
}

// 修改单条配置
export function updateConfig(data) {
  return request({
    url: '/cigarette/alert/wxworkConfig',
    method: 'put',
    data: data
  })
}
