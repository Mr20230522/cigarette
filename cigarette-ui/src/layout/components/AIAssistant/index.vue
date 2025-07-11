<template>
  <div>
    <!-- 悬浮图标 -->
    <div class="ai-assistant-icon" @click="toggleDialog">
      <img src="@/assets/icon-ai.png" alt="AI助手">
    </div>

    <!-- 对话框 -->
    <transition name="fade-scale">
      <div
        v-if="showDialog"
        class="ai-dialog deepseek-style"
        ref="dialog"
        :style="{ top: dialogPosition.top, left: dialogPosition.left, right: dialogPosition.right, bottom: dialogPosition.bottom }"
      >
        <div class="dialog-header" @mousedown="startDrag">
          <div class="header-left">
            <span class="ai-name">AI 助理</span>
            <span class="ai-status">在线</span>
          </div>
          <button class="close-btn" @click.stop="closeDialog">
            <i class="el-icon-close"></i>
          </button>
        </div>
        <div class="dialog-content">
          <div class="chat-container">
            <div class="message-container" ref="messageContainer">
              <div class="message-list">
                <div
                  v-for="(msg, index) in messages"
                  :key="index"
                  :class="['message', msg.type + '-message']"
                >
                  <div class="avatar">{{ msg.type === 'ai' ? 'AI' : '我' }}</div>
                  <div class="bubble">
                    <p>{{ msg.content }}</p>
                    <div class="message-time">{{ formatTime(msg.time) }}</div>
                  </div>
                </div>
                <div v-if="isLoading" class="message ai-message">
                  <div class="avatar">AI</div>
                  <div class="bubble">
                    <p>正在思考中...</p>
                  </div>
                </div>
              </div>
            </div>

            <button
              class="scroll-bottom-btn"
              @click="scrollToBottom"
              v-show="showScrollBottom"
            >
              <i class="el-icon-bottom"></i>
            </button>

            <div class="input-area">
              <el-input
                type="textarea"
                :rows="4"
                placeholder="输入您的问题..."
                v-model="userInput"
                resize="none"
                class="chat-input"
                @keyup.enter.native="handleEnterKey"
                :disabled="isLoading"
              ></el-input>
              <div class="action-bar">
                <el-button
                  type="primary"
                  size="small"
                  @click="sendMessage"
                  :loading="isLoading"
                >
                  发送
                </el-button>
                <el-button size="small" @click="clearInput">
                  清空
                </el-button>
                <el-button size="small" @click="clearChatHistory" v-if="messages.length > 1">
                  清空历史
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
import { sendChatMessage, getChatHistory } from '@/api/cigarette/ai/ai'

export default {
  name: 'AIAssistant',
  data() {
    return {
      showDialog: false,
      userInput: '',
      messages: [],
      isLoading: false,
      showScrollBottom: false,
      dialogPosition: {
        top: 'auto',
        left: 'auto',
        right: '40px',
        bottom: '120px'
      },
      dragData: {
        isDragging: false,
        startX: 0,
        startY: 0,
        startLeft: 0,
        startTop: 0
      },
      sessionId: ''
    }
  },
  watch: {
    showDialog(val) {
      if (val) {
        this.$nextTick(() => {
          this.scrollToBottom()
        })
      }
    }
  },
  mounted() {
    this.initSession()
    this.loadChatHistory()
    window.addEventListener('mousemove', this.handleDrag)
    window.addEventListener('mouseup', this.stopDrag)

    const container = this.$refs.messageContainer
    if (container) {
      container.addEventListener('scroll', this.checkScrollPosition)
    }
  },
  beforeDestroy() {
    window.removeEventListener('mousemove', this.handleDrag)
    window.removeEventListener('mouseup', this.stopDrag)

    const container = this.$refs.messageContainer
    if (container) {
      container.removeEventListener('scroll', this.checkScrollPosition)
    }
  },
  methods: {
    initSession() {
      // 从localStorage获取或生成新的sessionId
      let sessionId = localStorage.getItem('ai_session_id')
      if (!sessionId) {
        sessionId = 'sess-' + Date.now() + '-' + Math.random().toString(36).substr(2, 6)
        localStorage.setItem('ai_session_id', sessionId)
      }
      this.sessionId = sessionId
    },
    toggleDialog() {
      this.showDialog = !this.showDialog
      if (this.showDialog) {
        this.resetDialogPosition()
      }
    },
    closeDialog() {
      this.showDialog = false
    },
    resetDialogPosition() {
      this.dialogPosition = {
        top: 'auto',
        left: 'auto',
        right: '40px',
        bottom: '120px'
      }
    },
    startDrag(e) {
      if (e.button !== 0) return

      const dialog = this.$refs.dialog
      if (!dialog) return

      const rect = dialog.getBoundingClientRect()
      this.dragData = {
        isDragging: true,
        startX: e.clientX,
        startY: e.clientY,
        startLeft: rect.left,
        startTop: rect.top
      }

      this.dialogPosition = {
        top: `${rect.top}px`,
        left: `${rect.left}px`,
        right: 'auto',
        bottom: 'auto'
      }
    },
    handleDrag(e) {
      if (!this.dragData.isDragging) return

      const dx = e.clientX - this.dragData.startX
      const dy = e.clientY - this.dragData.startY

      this.dialogPosition = {
        top: `${this.dragData.startTop + dy}px`,
        left: `${this.dragData.startLeft + dx}px`,
        right: 'auto',
        bottom: 'auto'
      }
    },
    stopDrag() {
      this.dragData.isDragging = false
    },
    handleEnterKey(e) {
      if (e.shiftKey) {
        // Shift+Enter换行
        return
      }
      e.preventDefault()
      this.sendMessage()
    },
    async sendMessage() {
      if (!this.userInput.trim() || this.isLoading) return

      const userMessage = this.userInput
      this.messages.push({
        type: 'user',
        content: userMessage,
        time: new Date()
      })

      this.userInput = ''
      this.isLoading = true

      try {
        this.scrollToBottom()

        const response = await sendChatMessage({
          message: userMessage,
          sessionId: this.sessionId
        })

        if (response.code === 200) {
          this.messages.push({
            type: 'ai',
            content: response.data.response,
            time: new Date()
          })

          // 更新sessionId如果后端返回了新的
          if (response.data.sessionId && response.data.sessionId !== this.sessionId) {
            this.sessionId = response.data.sessionId
            localStorage.setItem('ai_session_id', this.sessionId)
          }
        } else {
          this.messages.push({
            type: 'ai',
            content: response.msg || '请求失败，请稍后重试',
            time: new Date()
          })
          this.$message.error(response.msg || '请求失败')
        }
      } catch (error) {
        console.error('获取AI回复失败:', error)
        this.messages.push({
          type: 'ai',
          content: '抱歉，我暂时无法处理您的请求，请稍后再试。',
          time: new Date()
        })
        this.$message.error('请求失败: ' + (error.response?.data?.msg || error.message))
      } finally {
        this.isLoading = false
        this.scrollToBottom()
      }
    },
    async loadChatHistory() {
      if (!this.sessionId) return

      try {
        const response = await getChatHistory(this.sessionId)

        if (response.code === 200 && response.data) {
          // 格式化历史记录
          this.messages = response.data.map(item => ({
            type: item.type || (item.isAI ? 'ai' : 'user'),
            content: item.content || item.message || (item.isAI ? item.response : item.question),
            time: item.time ? new Date(item.time) : new Date()
          }))

          // 如果没有历史记录，添加欢迎消息
          if (this.messages.length === 0) {
            this.messages.push({
              type: 'ai',
              content: '你好，我是您的AI助手，请问有什么可以帮您？',
              time: new Date()
            })
          }
        } else {
          // 默认欢迎消息
          this.messages = [{
            type: 'ai',
            content: '你好，我是您的AI助手，请问有什么可以帮您？',
            time: new Date()
          }]
        }
      } catch (error) {
        console.error('加载历史记录失败:', error)
        // 默认欢迎消息
        this.messages = [{
          type: 'ai',
          content: '你好，我是您的AI助手，请问有什么可以帮您？',
          time: new Date()
        }]
      }
    },
    clearInput() {
      this.userInput = ''
    },
    clearChatHistory() {
      // 直接清空，无确认提示
      this.sessionId = 'sess-' + Date.now() + '-' + Math.random().toString(36).substr(2, 6)
      localStorage.setItem('ai_session_id', this.sessionId)
      this.messages = [{
        type: 'ai',
        content: '你好，我是您的AI助手，请问有什么可以帮您？',
        time: new Date()
      }]
    },
    formatTime(date) {
      if (!(date instanceof Date)) {
        date = new Date(date)
      }
      return date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const container = this.$refs.messageContainer
        if (container) {
          container.scrollTop = container.scrollHeight
          this.showScrollBottom = false
        }
      })
    },
    checkScrollPosition() {
      const container = this.$refs.messageContainer
      if (container) {
        const threshold = 100
        const isNearBottom =
          container.scrollHeight - container.scrollTop - container.clientHeight < threshold
        this.showScrollBottom = !isNearBottom
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.ai-assistant-icon {
  position: fixed;
  right: 20px;
  bottom: 20px;
  width: 60px;
  height: 60px;
  cursor: pointer;
  z-index: 9999;
  transition: all 0.3s cubic-bezier(0.18, 0.89, 0.32, 1.28);

  img {
    width: 100%;
    height: 100%;
    transition: all 0.3s ease;
    filter: drop-shadow(0 2px 5px rgba(64, 158, 255, 0.3));
  }

  &:hover {
    transform: translateY(-8px) rotate(5deg);

    img {
      transform: scale(1.15);
      filter: drop-shadow(0 4px 8px rgba(64, 158, 255, 0.5));
    }

    &::after {
      content: "点我咨询";
      position: absolute;
      top: -28px;
      left: 50%;
      transform: translateX(-50%);
      background: var(--el-color-primary);
      color: white;
      padding: 4px 10px;
      border-radius: 15px;
      font-size: 12px;
      font-weight: bold;
      white-space: nowrap;
      animation: float-up 0.5s ease-out;
    }

    &::before {
      content: "";
      position: absolute;
      top: -5px;
      left: -5px;
      right: -5px;
      bottom: -5px;
      background: radial-gradient(circle, rgba(64, 158, 255, 0.4) 0%, transparent 70%);
      border-radius: 50%;
      animation: pulse 2s infinite;
      z-index: -1;
    }
  }
}

.ai-dialog.deepseek-style {
  position: fixed;
  width: 540px;
  height: 720px;
  right: 40px;
  bottom: 120px;
  background: white;
  border: 1px solid #e4e7ed;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
  z-index: 9998;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif;

  .dialog-header {
    height: 60px;
    padding: 0 24px;
    background: linear-gradient(135deg, #6e8efb 0%, #4a6cf7 100%);
    border-radius: 16px 16px 0 0;
    color: white;
    display: flex;
    justify-content: space-between;
    align-items: center;
    cursor: move;
    flex-shrink: 0;

    .header-left {
      display: flex;
      align-items: center;

      .ai-name {
        font-size: 20px;
        font-weight: 600;
        color: white;
      }

      .ai-status {
        margin-left: 10px;
        font-size: 12px;
        background: rgba(255, 255, 255, 0.2);
        padding: 2px 8px;
        border-radius: 10px;
        color: white;
      }
    }

    .close-btn {
      background: transparent;
      border: none;
      color: white;
      font-size: 22px;
      cursor: pointer;
      padding: 5px;
      transition: all 0.2s;

      &:hover {
        color: #ffeb3b;
        transform: rotate(90deg);
      }
    }
  }

  .dialog-content {
    padding: 0;
    background: #f5f7fa;
    display: flex;
    flex-direction: column;
    height: calc(100% - 60px); /* 减去header高度 */
  }

  .chat-container {
    display: flex;
    flex-direction: column;
    height: 100%;
    position: relative;
  }

  .message-container {
    flex: 1;
    overflow-y: auto;
    padding: 0 20px;
    scroll-behavior: smooth;

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-thumb {
      background: rgba(0, 0, 0, 0.2);
      border-radius: 3px;
    }
  }

  .message-list {
    min-height: 100%;
    display: flex;
    flex-direction: column;
    justify-content: flex-end;
    padding-bottom: 20px;
  }

  .message {
    display: flex;
    margin-bottom: 20px;

    .avatar {
      width: 40px;
      height: 40px;
      border-radius: 50%;
      background: #4a6cf7;
      color: white;
      display: flex;
      align-items: center;
      justify-content: center;
      font-weight: bold;
      margin-right: 15px;
      flex-shrink: 0;
    }

    .bubble {
      max-width: 80%;
      min-width: 120px;
      padding: 12px 16px;
      border-radius: 18px;
      line-height: 1.5;
      position: relative;

      p {
        margin: 0;
        font-size: 15px;
        white-space: pre-wrap;
        word-wrap: break-word;
        word-break: break-word;
        overflow-wrap: break-word;
      }

      .message-time {
        font-size: 12px;
        color: #999;
        margin-top: 5px;
        text-align: right;
      }
    }
  }

  .ai-message {
    .bubble {
      background: white;
      border-bottom-left-radius: 4px;
    }
  }

  .user-message {
    justify-content: flex-end;

    .bubble {
      background: #4a6cf7;
      color: white;
      border-bottom-right-radius: 4px;

      p {
        word-break: break-all;
      }

      .message-time {
        color: rgba(255, 255, 255, 0.7);
      }
    }
  }

  .scroll-bottom-btn {
    position: absolute;
    right: 30px;
    bottom: 100px;
    width: 36px;
    height: 36px;
    border-radius: 50%;
    background: white;
    border: 1px solid #e4e7ed;
    color: #4a6cf7;
    cursor: pointer;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
    z-index: 10;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s;

    &:hover {
      background: #4a6cf7;
      color: white;
      transform: scale(1.1);
    }
  }

  .input-area {
    flex-shrink: 0;
    padding: 15px;
    background: white;
    border-top: 1px solid #e4e7ed;

    .chat-input {
      ::v-deep .el-textarea__inner {
        border: 1px solid #e4e7ed;
        border-radius: 12px;
        padding: 12px;
        font-size: 15px;
        resize: none;
      }
    }

    .action-bar {
      display: flex;
      justify-content: flex-end;
      margin-top: 10px;

      .el-button {
        margin-left: 10px;
        border-radius: 8px;
      }
    }
  }
}

.fade-scale-enter-active,
.fade-scale-leave-active {
  transition: all 0.3s cubic-bezier(0.22, 0.61, 0.36, 1);
}

.fade-scale-enter-from,
.fade-scale-leave-to {
  opacity: 0;
  transform: translateY(20px) scale(0.95);
}

@keyframes pulse {
  0% { transform: scale(0.95); opacity: 0.8; }
  50% { transform: scale(1.05); opacity: 0.4; }
  100% { transform: scale(0.95); opacity: 0.8; }
}

@keyframes float-up {
  0% { opacity: 0; transform: translate(-50%, 10px); }
  100% { opacity: 1; transform: translate(-50%, 0); }
}
</style>
