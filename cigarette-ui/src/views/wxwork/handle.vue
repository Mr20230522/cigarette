<!DOCTYPE html>
<html lang="zh-CN">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no">
  <title>预警处理系统</title>
  <style>
    * { margin: 0; padding: 0; box-sizing: border-box; }
    body { font-family: -apple-system, BlinkMacSystemFont, "PingFang SC", "Helvetica Neue", sans-serif;
      background: #f5f5f5; color: #333; font-size: 14px; padding-bottom: 80px; }
    .header { background: linear-gradient(135deg, #4A90D9, #357ABD); color: #fff; padding: 16px; text-align: center; position: sticky; top: 0; z-index: 10; }
    .header h2 { font-size: 17px; }
    .header .logout { position: absolute; right: 16px; top: 50%; transform: translateY(-50%); font-size: 13px; color: rgba(255,255,255,0.8); cursor: pointer; }
    .tabs { display: flex; background: #fff; border-bottom: 1px solid #e8e8e8; position: sticky; top: 56px; z-index: 9; }
    .tabs span { flex: 1; text-align: center; padding: 12px; font-size: 14px; color: #666; border-bottom: 2px solid transparent; cursor: pointer; }
    .tabs span.active { color: #4A90D9; border-bottom-color: #4A90D9; font-weight: bold; }
    .card { background: #fff; margin: 10px 12px; border-radius: 8px; padding: 14px; box-shadow: 0 1px 3px rgba(0,0,0,0.08); }
    .card .row { display: flex; justify-content: space-between; align-items: center; padding: 6px 0; }
    .card .plate { font-size: 16px; font-weight: bold; color: #E84545; }
    .card .reason { font-size: 13px; color: #666; margin-top: 2px; }
    .card .camera { font-size: 12px; color: #999; }
    .card .time { font-size: 12px; color: #999; }
    .actions { display: flex; gap: 10px; margin-top: 10px; }
    .btn { flex: 1; padding: 8px; border: none; border-radius: 6px; font-size: 13px; cursor: pointer; text-align: center; }
    .btn-accept { background: #07C160; color: #fff; }
    .btn-reject { background: #FA5151; color: #fff; }
    .btn-back { background: #f0f0f0; color: #333; }
    .empty { text-align: center; padding: 40px; color: #999; font-size: 14px; }
    .login-form { padding: 40px 20px; }
    .login-form h3 { text-align: center; margin-bottom: 24px; font-size: 18px; }
    .login-form .field { margin-bottom: 16px; }
    .login-form .field label { display: block; font-size: 13px; color: #666; margin-bottom: 6px; }
    .login-form .field input { width: 100%; padding: 12px; border: 1px solid #ddd; border-radius: 6px; font-size: 15px; }
    .login-form .btn-submit { width: 100%; padding: 14px; border: none; border-radius: 8px; background: #4A90D9; color: #fff; font-size: 16px; cursor: pointer; margin-top: 12px; }
    .detail-card .row { display: flex; padding: 10px 0; border-bottom: 1px solid #f0f0f0; }
    .detail-card .label { width: 80px; color: #999; font-size: 13px; flex-shrink: 0; }
    .detail-card .value { flex: 1; font-size: 14px; color: #333; }
    .feedback-panel { padding: 16px; }
    .feedback-panel .group { margin-bottom: 16px; }
    .feedback-panel .group-label { font-size: 14px; color: #666; margin-bottom: 8px; }
    .feedback-panel .options { display: flex; gap: 12px; }
    .feedback-panel .option { flex: 1; text-align: center; padding: 12px; border: 2px solid #e0e0e0; border-radius: 8px; cursor: pointer; font-size: 14px; }
    .feedback-panel .option.selected { border-color: #07C160; background: rgba(7,193,96,0.05); color: #07C160; }
    .feedback-panel textarea { width: 100%; padding: 10px; border: 1px solid #ddd; border-radius: 6px; font-size: 14px; resize: vertical; min-height: 80px; }
    .toast { display: none; position: fixed; top: 50%; left: 50%; transform: translate(-50%,-50%);
      background: rgba(0,0,0,0.75); color: #fff; padding: 12px 24px; border-radius: 6px; font-size: 14px; z-index: 200; }
    .toast.show { display: block; animation: fadeInOut 2s; }
    @keyframes fadeInOut { 0%{opacity:0} 10%{opacity:1} 90%{opacity:1} 100%{opacity:0} }
  </style>
</head>
<body>
  <div class="header">
    <h2>预警处理系统</h2>
    <span class="logout" id="btnLogout" onclick="doLogout()" style="display:none;">退出</span>
  </div>

  <!-- 登录表单 -->
  <div class="login-form" id="loginPanel">
    <h3>系统登录</h3>
    <div class="field">
      <label>账号</label>
      <input type="text" id="username" placeholder="请输入账号" value="">
    </div>
    <div class="field">
      <label>密码</label>
      <input type="password" id="password" placeholder="请输入密码">
    </div>
    <button class="btn-submit" onclick="doLogin()">登录</button>
  </div>

  <!-- 任务列表（登录后可见） -->
  <div id="listPanel" style="display:none;">
    <div class="tabs">
      <span class="active" id="tabPending" onclick="switchTab(0)">待处理</span>
      <span id="tabAccepted" onclick="switchTab(1)">处理中</span>
      <span id="tabDone" onclick="switchTab(2)">已完成</span>
    </div>
    <div id="taskList"></div>
  </div>

  <!-- 任务详情 + 操作 -->
  <div id="detailPanel" style="display:none;"></div>

  <!-- 反馈面板 -->
  <div id="feedbackPanel" style="display:none;"></div>

  <div class="toast" id="toast"></div>

  <script>
    var token = '';
    var baseUrl = '';
    var currentTab = 0;
    var feedbackTaskId = null;
    var isMeetAlert = null;
    var isIllegal = null;

    function initBaseUrl() {
      var path = window.location.pathname;
      if (path.indexOf('/wxwork/') >= 0) {
        baseUrl = path.substring(0, path.indexOf('/wxwork/'));
      } else {
        baseUrl = '/dev-api';
      }
      if (window.location.port === '80' || window.location.port === '') {
        baseUrl = '/dev-api';
      }
    }

    function checkLogin() {
      token = localStorage.getItem('alert_token');
      if (token) {
        showList();
      }
    }

    function doLogin() {
      var username = document.getElementById('username').value;
      var password = document.getElementById('password').value;
      if (!username || !password) { showToast('请输入账号和密码'); return; }

      var xhr = new XMLHttpRequest();
      xhr.open('GET', baseUrl + '/captchaImage', true);
      xhr.onload = function() {
        var res = JSON.parse(xhr.responseText);
        if (res.code === 200) {
          var uuid = res.uuid;
          var xhr2 = new XMLHttpRequest();
          xhr2.open('POST', baseUrl + '/login', true);
          xhr2.setRequestHeader('Content-Type', 'application/json');
          xhr2.onload = function() {
            var loginRes = JSON.parse(xhr2.responseText);
            if (loginRes.code === 200 && loginRes.token) {
              token = loginRes.token;
              localStorage.setItem('alert_token', token);
              showToast('登录成功');
              showList();
            } else {
              showToast(loginRes.msg || '登录失败');
            }
          };
          xhr2.send(JSON.stringify({ username: username, password: password, code: '0', uuid: uuid }));
        }
      };
      xhr.send();
    }

    function doLogout() {
      localStorage.removeItem('alert_token');
      token = '';
      document.getElementById('loginPanel').style.display = 'block';
      document.getElementById('listPanel').style.display = 'none';
      document.getElementById('detailPanel').style.display = 'none';
      document.getElementById('feedbackPanel').style.display = 'none';
      document.getElementById('btnLogout').style.display = 'none';
      document.getElementById('username').value = '';
      document.getElementById('password').value = '';
    }

    function showList() {
      document.getElementById('loginPanel').style.display = 'none';
      document.getElementById('listPanel').style.display = 'block';
      document.getElementById('detailPanel').style.display = 'none';
      document.getElementById('feedbackPanel').style.display = 'none';
      document.getElementById('btnLogout').style.display = 'block';
      loadTaskList(currentTab);
    }

    function switchTab(tab) {
      currentTab = tab;
      document.getElementById('tabPending').className = tab === 0 ? 'active' : '';
      document.getElementById('tabAccepted').className = tab === 1 ? 'active' : '';
      document.getElementById('tabDone').className = tab === 2 ? 'active' : '';
      loadTaskList(tab);
    }

    function loadTaskList(tab) {
      var statusParam = '';
      if (tab === 0) statusParam = '&status=0';
      else if (tab === 1) statusParam = '&status=1';
      else statusParam = '&status=2';

      var xhr = new XMLHttpRequest();
      xhr.open('GET', baseUrl + '/cigarette/alert/task/list?pageNum=1&pageSize=50' + statusParam, true);
      xhr.setRequestHeader('Authorization', 'Bearer ' + token);
      xhr.onload = function() {
        var res = JSON.parse(xhr.responseText);
        if (res.code === 401 || res.code === 402) { doLogout(); return; }
        var list = res.rows || [];
        var html = '';
        if (list.length === 0) {
          html = '<div class="empty">暂无任务</div>';
        } else {
          list.forEach(function(task) {
            html += '<div class="card" onclick="showDetail(' + task.id + ')">'
              + '<div class="row"><span class="plate">' + (task.plate || '-') + '</span>'
              + '<span style="font-size:12px;color:#999;">' + statusText(task.status) + '</span></div>'
              + '<div class="reason">原因：' + (task.reason || '-') + '</div>'
              + '<div class="camera">卡口：' + (task.cameraName || '-') + '</div>'
              + '<div class="time">' + (task.createTime || '-') + '</div></div>';
          });
        }
        document.getElementById('taskList').innerHTML = html;
      };
      xhr.send();
    }

    function statusText(s) {
      if (s === 0) return '待处理';
      if (s === 1) return '处理中';
      if (s === 2) return '已完成';
      if (s === 3) return '已关闭';
      return '未知';
    }

    function showDetail(taskId) {
      var xhr = new XMLHttpRequest();
      xhr.open('GET', baseUrl + '/cigarette/alert/task/' + taskId, true);
      xhr.setRequestHeader('Authorization', 'Bearer ' + token);
      xhr.onload = function() {
        var res = JSON.parse(xhr.responseText);
        if (res.code !== 200) { showToast('加载失败'); return; }
        var task = res.data;
        document.getElementById('listPanel').style.display = 'none';
        document.getElementById('detailPanel').style.display = 'block';
        document.getElementById('feedbackPanel').style.display = 'none';

        var html = '';
        html += '<div class="card detail-card">';
        html += '<div class="row"><span class="label">车牌</span><span class="value" style="color:#E84545;font-weight:bold;">' + (task.plate || '-') + '</span></div>';
        html += '<div class="row"><span class="label">预警原因</span><span class="value">' + (task.reason || '-') + '</span></div>';
        html += '<div class="row"><span class="label">卡口名称</span><span class="value">' + (task.cameraName || '-') + '</span></div>';
        html += '<div class="row"><span class="label">抓拍时间</span><span class="value">' + (task.captureTime || '-') + '</span></div>';
        html += '<div class="row"><span class="label">任务状态</span><span class="value">' + statusText(task.status) + '</span></div>';
        html += '<div class="row"><span class="label">任务编号</span><span class="value">' + (task.taskNo || '-') + '</span></div>';
        html += '<div class="row"><span class="label">创建时间</span><span class="value">' + (task.createTime || '-') + '</span></div>';
        if (task.acceptUser) {
          html += '<div class="row"><span class="label">处理人</span><span class="value">' + task.acceptUser + '</span></div>';
        }
        html += '</div>';

        html += '<div class="card">';
        html += '<button class="btn btn-back" onclick="showList()">返回列表</button>';
        if (task.status === 0) {
          html += '<div class="actions" style="margin-top:12px;">';
          html += '<button class="btn btn-accept" onclick="doAccept(' + task.id + ')">接受处理</button>';
          html += '<button class="btn btn-reject" onclick="doReject(' + task.id + ')">拒绝处理</button>';
          html += '</div>';
        } else if (task.status === 1) {
          html += '<div class="actions" style="margin-top:12px;">';
          html += '<button class="btn btn-accept" onclick="showFeedback(' + task.id + ')">提交反馈</button>';
          html += '</div>';
        }
        html += '</div>';

        document.getElementById('detailPanel').innerHTML = html;
      };
      xhr.send();
    }

    function doAccept(taskId) {
      var xhr = new XMLHttpRequest();
      xhr.open('PUT', baseUrl + '/cigarette/alert/task/accept/' + taskId, true);
      xhr.setRequestHeader('Authorization', 'Bearer ' + token);
      xhr.onload = function() {
        var res = JSON.parse(xhr.responseText);
        if (res.code === 200) {
          showToast('已接受，请提交反馈');
          showFeedback(taskId);
        } else {
          showToast(res.msg || '操作失败');
        }
      };
      xhr.send();
    }

    function doReject(taskId) {
      if (!confirm('确认拒绝该任务？系统将推送给下一位处理人。')) return;
      var xhr = new XMLHttpRequest();
      xhr.open('PUT', baseUrl + '/cigarette/alert/task/reject/' + taskId, true);
      xhr.setRequestHeader('Authorization', 'Bearer ' + token);
      xhr.onload = function() {
        var res = JSON.parse(xhr.responseText);
        if (res.code === 200) {
          showToast('已拒绝');
          showList();
        } else {
          showToast(res.msg || '操作失败');
        }
      };
      xhr.send();
    }

    function showFeedback(taskId) {
      feedbackTaskId = taskId;
      isMeetAlert = null;
      isIllegal = null;
      document.getElementById('detailPanel').style.display = 'none';
      document.getElementById('feedbackPanel').style.display = 'block';
      document.getElementById('feedbackPanel').innerHTML = ''
        + '<div class="feedback-panel">'
        + '<h3 style="text-align:center;margin-bottom:16px;">处理结果反馈</h3>'
        + '<div class="group"><div class="group-label">是否符合预警</div><div class="options">'
        + '<div class="option" id="optMeet" onclick="selectMeet(1)">符合</div>'
        + '<div class="option" id="optNotMeet" onclick="selectMeet(0)">不符合</div>'
        + '</div></div>'
        + '<div class="group"><div class="group-label">是否违法</div><div class="options">'
        + '<div class="option" id="optIllegal" onclick="selectIllegal(1)">违法</div>'
        + '<div class="option" id="optNotIllegal" onclick="selectIllegal(0)">未违法</div>'
        + '</div></div>'
        + '<div class="group"><div class="group-label">备注</div>'
        + '<textarea id="remarkInput" placeholder="补充说明（选填）"></textarea></div>'
        + '<button class="btn-submit" onclick="submitFeedback()">提交反馈</button>'
        + '</div>';
    }

    function selectMeet(val) {
      isMeetAlert = val;
      document.getElementById('optMeet').className = val === 1 ? 'option selected' : 'option';
      document.getElementById('optNotMeet').className = val === 0 ? 'option selected' : 'option';
    }

    function selectIllegal(val) {
      isIllegal = val;
      document.getElementById('optIllegal').className = val === 1 ? 'option selected' : 'option';
      document.getElementById('optNotIllegal').className = val === 0 ? 'option selected' : 'option';
    }

    function submitFeedback() {
      if (isMeetAlert === null) { showToast('请选择是否符合预警'); return; }
      if (isIllegal === null) { showToast('请选择是否违法'); return; }
      var remark = document.getElementById('remarkInput').value || '';

      var xhr = new XMLHttpRequest();
      xhr.open('POST', baseUrl + '/cigarette/alert/task/feedback', true);
      xhr.setRequestHeader('Authorization', 'Bearer ' + token);
      xhr.setRequestHeader('Content-Type', 'application/json');
      xhr.onload = function() {
        var res = JSON.parse(xhr.responseText);
        if (res.code === 200) {
          showToast('反馈已提交');
          showList();
        } else {
          showToast(res.msg || '提交失败');
        }
      };
      xhr.send(JSON.stringify({ taskId: feedbackTaskId, isMeetAlert: isMeetAlert, isIllegal: isIllegal, remark: remark }));
    }

    function showToast(msg) {
      var t = document.getElementById('toast');
      t.textContent = msg;
      t.className = 'toast show';
      setTimeout(function() { t.className = 'toast'; }, 2000);
    }

    initBaseUrl();
    checkLogin();
  </script>
</body>
</html>
