import request from '@/utils/request'

// 发送聊天消息
export function sendChatMessage(data) {
  return request({
    url: '/ai/chat/send',
    method: 'post',
    data: data  // 确保使用data而不是params
  }).then(res => {
    // 统一响应结构处理
    return {
      code: res.code || 200,
      msg: res.msg || 'success',
      data: {
        response: res.data?.response || res.response,
        sessionId: res.data?.sessionId || res.sessionId
      }
    }
  })
}

// 获取聊天历史
export function getChatHistory(sessionId) {
  return request({
    url: '/ai/chat/history',
    method: 'get',
    params: { sessionId }
  })
}
