<template>
  <div class="app-container">
    <el-card shadow="never">
      <div slot="header">
        <span class="card-header-title">
          <i class="el-icon-odometer"></i> 嫌疑度阈值设置
        </span>
      </div>

      <el-alert
        type="info"
        :closable="false"
        description="设置系统全局嫌疑度阈值，支持精确到小数点后两位"
        class="alert-margin"
      />

      <div class="level-container">
        <div class="level-display">
          <el-progress
            :percentage="currentLevel"
            :color="customColors"
            :stroke-width="20"
            :text-inside="true"
            :format="formatProgress"
          />
          <div class="level-input">
            <el-input-number
              v-model="currentLevel"
              :min="0"
              :max="100"
              :step="0.1"
              :precision="2"
              controls-position="right"
              @change="handleLevelChange"
            />
            <span class="unit">%</span>
          </div>
        </div>

        <div class="slider-container">
          <el-slider
            v-model="currentLevel"
            :max="100"
            :min="0"
            :step="0.1"
            :format-tooltip="formatTooltip"
            show-input
            input-size="small"
            @change="handleLevelChange"
          />
        </div>

        <div class="action-buttons">
          <el-button
            type="primary"
            icon="el-icon-check"
            :loading="loading"
            @click="saveLevel"
          >
            保存设置
          </el-button>
          <el-button
            icon="el-icon-refresh-left"
            @click="resetLevel"
          >
            重置为默认值
          </el-button>
        </div>
      </div>

      <el-divider></el-divider>

      <div class="level-description">
        <h4><i class="el-icon-info"></i> 嫌疑度说明</h4>
        <el-table :data="levelRanges" border style="width: 100%">
          <el-table-column prop="range" label="数值范围" width="180" />
          <el-table-column prop="level" label="嫌疑等级" width="120">
            <template slot-scope="scope">
              <el-tag :type="scope.row.tagType">{{ scope.row.level }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="description" label="说明" />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getSuspicionLevel, updateSuspicionLevel } from "@/api/cigarette/caution/suspicion";

export default {
  name: "SuspicionSetting",
  data() {
    return {
      currentLevel: 50.0,
      loading: false,
      customColors: [
        { color: "#67C23A", percentage: 30 },
        { color: "#E6A23C", percentage: 70 },
        { color: "#F56C6C", percentage: 100 }
      ],
      levelRanges: [
        { range: "0.00 - 30.00", level: "低", tagType: "success", description: "系统将放宽检测标准，减少误判" },
        { range: "30.01 - 70.00", level: "中", tagType: "warning", description: "系统采用常规检测标准" },
        { range: "70.01 - 100.00", level: "高", tagType: "danger", description: "系统将采用严格检测标准，可能增加误判" }
      ]
    };
  },
  created() {
    this.fetchCurrentLevel();
  },
  methods: {
    fetchCurrentLevel() {
      getSuspicionLevel().then(response => {
        this.currentLevel = parseFloat(response.data) || 50.0;
      }).catch(() => {
        this.$message.error("获取当前嫌疑度失败");
      });
    },
    handleLevelChange(val) {
      this.currentLevel = parseFloat(val.toFixed(2));
    },
    saveLevel() {
      this.loading = true;
      console.log('this.currentLevel',this.currentLevel)
      updateSuspicionLevel(this.currentLevel)
        .then(() => {
          this.$message.success("嫌疑度设置保存成功");
        })
        .catch(error => {
          this.$message.error(error.message || "保存失败");
        })
        .finally(() => {
          this.loading = false;
        });
    },
    resetLevel() {
      this.$confirm("确定要将嫌疑度重置为默认值50.0吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.currentLevel = 50.0;
        this.saveLevel();
      });
    },
    formatProgress(percentage) {
      return `当前值: ${percentage.toFixed(2)}%`;
    },
    formatTooltip(val) {
      return `${val.toFixed(2)}%`;
    }
  }
};
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.card-header-title {
  font-size: 18px;
  font-weight: bold;
}

.alert-margin {
  margin-bottom: 20px;
}

.level-container {
  max-width: 800px;
  margin: 0 auto;
}

.level-display {
  margin: 30px 0;
  text-align: center;
}

.level-input {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.level-input .unit {
  margin-left: 10px;
  font-size: 16px;
  color: #606266;
}

.slider-container {
  margin: 40px 0;
}

.action-buttons {
  text-align: center;
  margin: 30px 0;
}

.level-description {
  margin-top: 30px;
}

.level-description h4 {
  margin-bottom: 15px;
  color: #303133;
}
</style>
