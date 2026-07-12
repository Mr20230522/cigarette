# 企微后台域名配置指引

## 前置条件

- 拥有一个已备案的域名（如 `alert.example.com`）
- 域名已解析到服务器 IP
- 服务器已配置 HTTPS 证书（企微要求 HTTPS）

## 企微管理后台配置步骤

### 1. 配置可信域名

登录企业微信管理后台 → 应用管理 → 自建应用 → 你的应用 → 开发者接口 → 网页授权及JS-SDK

- **可信域名**：填入你的域名（如 `alert.example.com`），不带 `http://` 前缀
- 需要下载域名归属校验文件，放到 web 服务器根目录

### 2. 配置应用主页

同一页面 → 应用主页

- **应用主页URL**：`https://alert.example.com/wxwork/handle.html`

### 3. 配置工作台应用主页（可选）

如果希望企微工作台点应用图标也能打开处理页面：
- 工作台应用主页：`https://alert.example.com/wxwork/handle.html`

## 后端配置

### application.yml

```yaml
wxwork:
  app-home-url: https://alert.example.com/wxwork/handle.html
```

### 数据库 tob_wxwork_config

确认以下配置项存在且值正确：

| config_key | 说明 |
|---|---|
| wxwork.corpid | 企业ID（企微后台 → 我的企业 → 企业信息） |
| wxwork.secret | 应用Secret（应用管理 → 你的应用 → Secret） |
| wxwork.agentid | 应用AgentId |

## 部署检查清单

- [ ] 域名已备案且解析正确
- [ ] HTTPS 证书有效
- [ ] 企微后台可信域名已配置
- [ ] 企微后台应用主页已配置
- [ ] application.yml 的 app-home-url 与企微后台一致
- [ ] tob_wxwork_config 表 corpid/secret/agentid 正确
- [ ] 后端服务通过域名可访问（`https://你的域名/wxwork/handle.html` 能加载页面）
- [ ] 企微内点击 TextCard 消息「立即处理」能正常打开任务详情
