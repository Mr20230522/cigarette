<template>
  <div class="app-container">
    <el-card>
      <div slot="header">
        <span>预警数据源配置</span>
      </div>
      <el-form ref="form" :model="form" label-width="120px">
        <el-form-item label="数据源">
          <el-radio-group v-model="form.sourceType" @change="onSourceChange">
            <el-radio label="risk">风险计算系统</el-radio>
            <el-radio label="model">模型计算</el-radio>
          </el-radio-group>
          <div style="color:#909399;font-size:12px;margin-top:4px;">
            两个数据源互斥，同时只能启用一个
          </div>
        </el-form-item>

        <el-form-item label="风险阈值" v-show="form.sourceType === 'risk'">
          <el-input-number v-model="form.threshold" :min="0" :max="1000" :step="1" controls-position="right" style="width:200px;" />
          <span style="margin-left:8px;color:#909399;font-size:12px;">Level > 阈值时触发预警</span>
          <el-tag style="margin-left:8px;" :type="levelTagType" size="small">当前等级：{{ levelLabel }}</el-tag>
        </el-form-item>

        <el-divider content-position="left" v-show="form.sourceType === 'risk'">等级划分</el-divider>

        <el-form-item label="低 ≤" v-show="form.sourceType === 'risk'">
          <el-input-number v-model="levels.low" :min="0" :max="999" :step="1" size="small" style="width:150px;" />
        </el-form-item>
        <el-form-item label="中 ≤" v-show="form.sourceType === 'risk'">
          <el-input-number v-model="levels.medium" :min="0" :max="999" :step="1" size="small" style="width:150px;" />
        </el-form-item>
        <el-form-item label="高 ≤" v-show="form.sourceType === 'risk'">
          <el-input-number v-model="levels.high" :min="0" :max="999" :step="1" size="small" style="width:150px;" />
        </el-form-item>
        <el-form-item label="严重 >" v-show="form.sourceType === 'risk'">
          <span style="font-size:14px;color:#606266;">Level > 高等级阈值</span>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitForm" :loading="saving">保存配置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getSourceConfig, updateConfig } from '@/api/cigarette/alert/sourceConfig'

export default {
  name: 'AlertSourceConfig',
  data() {
    return {
      form: {
        sourceType: 'risk',
        threshold: 60
      },
      levels: {
        low: 30,
        medium: 60,
        high: 100
      },
      saving: false,
      configMap: {}
    }
  },
  created() {
    this.loadConfig()
  },
  computed: {
    levelLabel() {
      const t = this.form.threshold
      if (t > this.levels.high)   return '严重'
      if (t > this.levels.medium) return '高'
      if (t > this.levels.low)    return '中'
      return '低'
    },
    levelTagType() {
      const t = this.form.threshold
      if (t > this.levels.high)   return 'danger'
      if (t > this.levels.medium) return 'warning'
      if (t > this.levels.low)    return 'success'
      return 'info'
    }
  },
  methods: {
    loadConfig() {
      getSourceConfig().then(res => {
        if (res.code === 200 && res.rows) {
          res.rows.forEach(item => {
            this.configMap[item.configKey] = item
            if (item.configKey === 'alert.source.type') {
              this.form.sourceType = item.configValue || 'risk'
            } else if (item.configKey === 'alert.risk.threshold') {
              this.form.threshold = parseInt(item.configValue) || 60
            }
            if (item.configKey === 'alert.level.low')    this.levels.low    = parseInt(item.configValue) || 0
            if (item.configKey === 'alert.level.medium') this.levels.medium = parseInt(item.configValue) || 0
            if (item.configKey === 'alert.level.high')   this.levels.high   = parseInt(item.configValue) || 0
          })
        }
      })
    },
    onSourceChange(val) {
      // 切换时不自动保存，需点按钮
    },
    submitForm() {
      this.saving = true
      const promises = []

      const sourceRecord = this.configMap['alert.source.type']
      if (sourceRecord) {
        sourceRecord.configValue = this.form.sourceType
        promises.push(updateConfig(sourceRecord))
      }

      const thresholdRecord = this.configMap['alert.risk.threshold']
      if (thresholdRecord) {
        thresholdRecord.configValue = String(this.form.threshold)
        promises.push(updateConfig(thresholdRecord))
      }

      ;['low','medium','high'].forEach(k => {
        const record = this.configMap['alert.level.' + k]
        if (record) {
          record.configValue = String(this.levels[k])
          promises.push(updateConfig(record))
        }
      })

      Promise.all(promises).then(() => {
        this.$message.success('配置保存成功')
        this.saving = false
      }).catch(() => {
        this.$message.error('保存失败')
        this.saving = false
      })
    }
  }
}
</script>
