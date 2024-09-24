<template>
  <div class="feedback" @mouseenter="showText = true" @mouseleave="showText = false">
    <el-popover :visible="visible" trigger="click" placement="left" :width="510" @hide="resetForm">

      <div class="feedback-content" @dragover="handleDragOver" @drop="handleDrop">
        <header class="flex">
          <strong>反馈中心</strong>
          <el-link type="primary" v-if="!isMyFeed" @click="toJiraPage"><strong>我的反馈</strong></el-link>
          <el-link type="primary" v-else @click="toJiraPage"><strong>去反馈</strong></el-link>
        </header>
        <hr style="margin: 10px 0 0 -13px; border-top: 1px solid #dbdbdb"/>
        <section v-if="!isMyFeed" style="height: 80%; overflow-y: auto;">
          <p style="margin-top: 10px; letter-spacing: 1px"><strong>尊敬的用户：</strong></p>
          <p style="letter-spacing: 1px; text-indent: 4ch">感谢您提供诚挚的建议，我们将尽快帮您处理解决。</p>
          <el-form
            ref="refForm"
            :model="fromData"
            :rules="fromRules"
            label-position="top"
            size="large"
            style="margin-top: 20px"
            class="from-content"
          >
            <el-form-item
              label="问题类型"
              prop="question_type"
              :rules="[{ required: true, message: '请选择问题类型', trigger: 'change' }]"
            >
              <el-radio-group v-model="fromData.question_type">
                <el-radio-button label="1" value="1">功能异常</el-radio-button>
                <el-radio-button label="2" value="2">安全问题</el-radio-button>
                <el-radio-button label="3" value="3">体验问题</el-radio-button>
                <el-radio-button label="4" value="4">功能建议</el-radio-button>
                <el-radio-button label="5" value="5">其它</el-radio-button>
              </el-radio-group>
            </el-form-item>

            <el-form-item
              label="问题描述"
              prop="question_content"
              :rules="[{ required: true, message: '请填写问题描述', trigger: 'blur' }]"
            >
              <el-input v-model="fromData.question_content" type="textarea" :rows="3"></el-input>
            </el-form-item>

            <el-form-item
              label="问题所在页面和截图"
              prop="in_page"
              :rules="[{ required: true, message: '请填写问题所在页面', trigger: 'blur' }]"
            >
              <el-input v-model="fromData.in_page"></el-input>
            </el-form-item>

            <el-upload
              action="#"
              list-type="picture-card"
              :auto-upload="false"
              :on-remove="handleRemove"
              :file-list="fromData.file_paths"
            >
              <i class="el-icon-plus"></i>
            </el-upload>

            <el-form-item
              label="问题优先级"
              prop="priority"
              :rules="[{ required: true, message: '请选择问题优先级', trigger: 'change' }]"
            >
              <el-radio-group size="small" v-model="fromData.priority">
                <el-radio label="低" class="priority-low">低</el-radio>
                <el-radio label="中" class="priority-medium">中</el-radio>
                <el-radio label="高" class="priority-high">高</el-radio>
              </el-radio-group>
            </el-form-item>

            <el-form-item
              label="联系方式"
              prop="creator_phone_number"
              :rules="[
        { required: true, message: '请填写联系方式', trigger: 'blur' },
        { validator: validatePhone, trigger: 'blur' }
      ]"
            >
              <el-input v-model="fromData.creator_phone_number"></el-input>
            </el-form-item>

            <div class="btn-row">
              <el-button class="btn-row-left" type="default" size="small" round @click="reset">重 置</el-button>
              <!--<el-button class="btn-row-left" type="default" size="small" round @click="close">暂 存</el-button>-->
              <el-button
                class="btn-row-right"
                size="small"
                type="primary"
                round
                :disabled="loading"
                @click="handleSubmit(refForm)"
              >
                提 交
              </el-button>
            </div>
          </el-form>
        </section>
        <section v-else style="height: 80%; overflow-y: auto;">
          <p style="margin-top: 10px; letter-spacing: 1px"><strong>尊敬的用户：</strong></p>
          <p style="letter-spacing: 1px; text-indent: 4ch">感谢您提供诚挚的建议，我们将尽快帮您处理解决。</p>

        </section>
        <div class="dot"></div>
      </div>

      <div slot="reference">
        <div v-if="visible" class="line"></div>
        <div v-else class="slot-content" @click="this.visible = true">
          <!--<i class="el-icon-chat-line-square feedback-icon"></i>-->
          <!--<div v-if="showText" class="feedback-text">意见反馈</div>-->
          <div class="feedback-text">意见反馈</div>
        </div>
      </div>
    </el-popover>

    <!--<el-dialog :visible.sync="dialogVisible">-->
    <!--  <img :src="dialogImageUrl" width="100%" />-->
    <!--</el-dialog>-->
  </div>
</template>

<script>

export default {
  dicts: ['question_type'],
  data() {
    return {
      isMyFeed: false,
      showText: false,
      visible: false,
      dialogImageUrl: '',
      dialogVisible: false,
      feedbackType: [], // 模拟问题类型
      disabled: false,
      fromData: {
        question_type: '',
        question_content: '',
        in_page: '',
        file_paths: [],
        priority: '',
        creator_phone_number: ''
      },
      fromRules: {
        question_type: [
          {required: true, message: '请选择问题类型', trigger: 'change'}
        ],
        question_content: [
          {required: true, message: '请填写问题描述', trigger: 'blur'}
        ],
        in_page: [
          {required: true, message: '请填写问题所在页面', trigger: 'blur'}
        ],
        priority: [
          {required: true, message: '请选择问题优先级', trigger: 'change'}
        ],
        creator_phone_number: [
          {required: true, message: '请填写联系方式', trigger: 'blur'},
          {validator: this.validatePhone, trigger: 'blur'}
        ]
      },
      loading: false
    };
  },
  created() {
    this.feedbackType = ['功能异常', '安全问题']
  },
  methods: {
    // 验证电话号码
    validatePhone(rule, value, callback) {
      const phoneRegex = /^(?:\d{3,4}-)?\d{7,8}$|^(1[3-9]\d{9})$/; // 手机或座机的正则表达式
      if (!value) {
        callback(new Error('请输入电话号码'));
      } else if (!phoneRegex.test(value)) {
        callback(new Error('请输入有效的电话号码'));
      } else {
        callback();
      }
    },
    handleDragOver(event) {
      event.preventDefault();
    },
    handleDrop(event) {
      event.preventDefault();
      const file = event.dataTransfer.files[0];
      const allowedFormats = ['image/jpeg', 'image/png', 'image/gif', 'image/bmp', 'image/tiff', 'image/x-icon', 'image/svg+xml'];

      if (!allowedFormats.includes(file.type)) {
        this.$message.warning('只能上传 JPEG、PNG、GIF、BMP、TIFF、ICO 或 SVG 格式的图片');
        return;
      }
      if (this.fromData.imgs.length >= 5) {
        this.$message.warning('抱歉，最多只能上传5张图片！');
        return;
      }
      if (file.size > 2 * 1024 * 1024) {
        this.$message.warning('图片大小不能超过 2MB');
        return;
      }

      const reader = new FileReader();
      reader.onload = () => {
        const image = {
          name: file.name,
          url: reader.result,
          raw: file
        };
        this.fromData.imgs.push(image);
      };
      reader.readAsDataURL(file);
    },
    toJiraPage() {
      // 跳转到反馈页面
      this.isMyFeed = !this.isMyFeed
    },
    handleSubmit(formEl) {
      if (!formEl) return;
      formEl.validate((valid) => {
        if (valid) {
          this.loading = true;
          let fd = new FormData();
          fd.append('issueType', this.fromData.issueType);
          fd.append('summary', this.fromData.summary);
          fd.append('description', this.fromData.description);

          this.fromData.imgs.forEach((v) => fd.append('files', v.raw));

          // 调用 API 提交反馈
          submitFeedback(fd)
            .then((res) => {
              if (res.code === 200) {
                this.$message.success('反馈成功，感谢您的关注！');
                this.visible = false;
                this.resetForm();
              } else {
                this.$message.error('反馈失败:' + res.message);
              }
            })
            .catch((e) => this.$message.error('反馈失败:' + e))
            .finally(() => (this.loading = false));
        } else {
          return false;
        }
      });
    },
    resetForm() {
      this.fromData = {
        question_type: '',
        question_content: '',
        in_page: '',
        file_paths: [],
        priority: '',
        creator_phone_number: ''
      };
    },
    reset() {
      // this.visible = false;
      // this.showText = false;
      this.resetForm();
    },
    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
      console.log("图片出来了");
      console.log(this.dialogImageUrl);
    },
    handleRemove(file) {
      const index = this.fromData.imgs.findIndex((f) => f.uid === file.uid);
      this.fromData.imgs.splice(index, 1);
    }
  }
};
</script>


<style scoped>
::v-deep(.el-upload-list--picture-card .el-upload-list__item-actions span + span) {
  margin-left: 0.6rem !important;
}

::v-deep(.el-upload.el-upload--picture-card),
::v-deep(li.el-upload-list__item) {
  width: 70px !important;
  height: 70px !important;
}

::v-deep .el-upload-dragger {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.feedback-content {
  height: 82%;
  position: relative;
}

.feedback-content header {
  display: flex;
  justify-content: space-between;
  margin: 0 10px;
}

.feedback-content section .from-content {
  height: 454px;
  /* overflow-y: scroll; */
}

.feedback-content section .card-list {
  display: flex;
  gap: 20px;
}

.feedback-content section .card-list .card-item {
  padding: 0 20px;
  border-radius: 5px;
  background-color: #f2f3f5;
  border: 1px solid #dfdfdf;
  cursor: pointer;
  width: 100%;
  height: 35px;
  line-height: 35px;
  font-size: 12px;
}

.feedback-content section .card-list .card-item.active {
  color: #fff;
  background-color: #4c7cee;
}

.feedback-content section .upload {
  width: 60px;
  height: 60px;
  cursor: pointer;
  border: 1px dashed var(--el-border-color-darker);
  background-color: #fafafa;
}

.feedback-content section .upload:hover {
  border-color: var(--el-color-primary);
  color: var(--el-color-primary);
}

.feedback-content .dot {
  position: absolute;
  left: -12px;
  top: 0;
  width: 4px;
  height: 21px;
  border-radius: 5px;
  background-color: #4c7cee;
}

.feedback {
  position: fixed;
  top: 78%;
  right: 0;
  color: #fff;
  cursor: pointer;
  border-radius: 6px;
  transform: translateY(-50%);
  background-color: #4c7cea;
  z-index: 999999999999;
}

.feedback .line {
  width: 7px;
  height: 100px;
  border-radius: 6px;
  background-color: #4c7cea;
}

.feedback .feedback-text {
  letter-spacing: 0.3em;
  writing-mode: vertical-lr;
  text-orientation: upright;
}

@media only screen and (min-width: 1280px) {
  .feedback .slot-content {
    margin: 6px;
  }

  .feedback .slot-content .feedback-icon {
    width: 24px;
    height: 24px;
    margin-bottom: 5px;
  }

  .feedback .slot-content .feedback-text {
    font-size: 16px;
  }
}

@media only screen and (max-width: 1280px) {
  .feedback .slot-content {
    margin: 3px;
  }

  .feedback .slot-content .feedback-icon {
    width: 19px;
    height: 19px;
    margin-bottom: 3px;
  }

  .feedback .slot-content .feedback-text {
    font-size: 13px;
  }
}

.btn-row {
  margin: 16px 8px 0;
  text-align: end;
}

.btn-row-left {
  border-color: #4c7cee;
  color: #4c7cee;
}

/* 使用深度选择器修改字体颜色 */
::v-deep .priority-low .el-radio__label {
  color: green;
}

::v-deep .priority-medium .el-radio__label {
  color: orange;
}

::v-deep .priority-high .el-radio__label {
  color: red;
}
</style>
