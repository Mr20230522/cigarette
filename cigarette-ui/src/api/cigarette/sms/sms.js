import request from '@/utils/request'

// 发送短信验证码
export function sendSmsCode(phone) {
  return request({
    url: '/sms/send',
    method: 'post',
    data: { phone }  // 注意后端接收参数的方式
  })
}

// 验证短信验证码
export function verifySmsCode(data) {
  return request({
    url: '/sms/verify',
    method: 'post',
    data  // { phone: '', code: '' }
  })
}

//获取用户列表
