<!--<template>-->
<!--  <div class="query-page">-->
<!--    <el-card class="box-card">-->
<!--      <template #header>-->
<!--        <span>历史数据查询</span>-->
<!--      </template>-->

<!--      <el-form :model="form" label-width="100px" class="form-layout">-->
<!--        <el-row :gutter="20">-->
<!--          <el-col :xs="24" :sm="12" :md="8">-->
<!--            <el-form-item label="嫌疑程度">-->
<!--              <el-select v-model="form.suspicion" placeholder="请选择">-->
<!--                <el-option-->
<!--                  v-for="item in suspicionOptions"-->
<!--                  :key="item.value"-->
<!--                  :label="item.label"-->
<!--                  :value="item.value"-->
<!--                />-->
<!--              </el-select>-->
<!--            </el-form-item>-->
<!--          </el-col>-->

<!--          <el-col :xs="24" :sm="12" :md="8">-->
<!--            <el-form-item label="时间范围">-->
<!--              <el-date-picker-->
<!--                v-model="form.dateRange"-->
<!--                type="daterange"-->
<!--                range-separator="至"-->
<!--                start-placeholder="开始日期"-->
<!--                end-placeholder="结束日期"-->
<!--                format="yyyy-MM-dd"-->
<!--                value-format="yyyy-MM-dd"-->
<!--                style="width: 100%;"-->
<!--              />-->
<!--            </el-form-item>-->
<!--          </el-col>-->

<!--          <el-col :xs="24" :sm="12" :md="8">-->
<!--            <el-form-item label="车型">-->
<!--              <el-select v-model="form.carType" placeholder="请选择">-->
<!--                <el-option-->
<!--                  v-for="item in carTypeOptions"-->
<!--                  :key="item.value"-->
<!--                  :label="item.label"-->
<!--                  :value="item.value"-->
<!--                />-->
<!--              </el-select>-->
<!--            </el-form-item>-->
<!--          </el-col>-->

<!--          <el-col :xs="24" :sm="12" :md="8">-->
<!--            <el-form-item label="颜色">-->
<!--              <el-select v-model="form.color" placeholder="请选择">-->
<!--                <el-option-->
<!--                  v-for="item in colorOptions"-->
<!--                  :key="item.value"-->
<!--                  :label="item.label"-->
<!--                  :value="item.value"-->
<!--                />-->
<!--              </el-select>-->
<!--            </el-form-item>-->
<!--          </el-col>-->

<!--          <el-col :xs="24" :sm="24" :md="24" class="form-actions">-->
<!--            <el-form-item>-->
<!--              <el-button type="primary" @click="onSearch">查询</el-button>-->
<!--              <el-button @click="onReset">重置</el-button>-->
<!--            </el-form-item>-->
<!--          </el-col>-->
<!--        </el-row>-->
<!--      </el-form>-->
<!--    </el-card>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--export default {-->
<!--  data() {-->
<!--    return {-->
<!--      form: {-->
<!--        suspicion: '',-->
<!--        dateRange: [],-->
<!--        carType: '',-->
<!--        color: '',-->
<!--      },-->
<!--      suspicionOptions: [-->
<!--        { label: '高', value: 'high' },-->
<!--        { label: '中', value: 'medium' },-->
<!--        { label: '低', value: 'low' },-->
<!--      ],-->
<!--      carTypeOptions: [-->
<!--        { label: '轿车', value: 'sedan' },-->
<!--        { label: 'SUV', value: 'suv' },-->
<!--        { label: '货车', value: 'truck' },-->
<!--        { label: '其他', value: 'other' },-->
<!--      ],-->
<!--      colorOptions: [-->
<!--        { label: '黑色', value: 'black' },-->
<!--        { label: '白色', value: 'white' },-->
<!--        { label: '红色', value: 'red' },-->
<!--        { label: '蓝色', value: 'blue' },-->
<!--        { label: '银色', value: 'silver' },-->
<!--      ],-->
<!--    };-->
<!--  },-->
<!--  methods: {-->
<!--    onSearch() {-->
<!--      console.log('查询参数：', this.form);-->
<!--      // 可添加API请求-->
<!--    },-->
<!--    onReset() {-->
<!--      this.form = {-->
<!--        suspicion: '',-->
<!--        dateRange: [],-->
<!--        carType: '',-->
<!--        color: '',-->
<!--      };-->
<!--    },-->
<!--  },-->
<!--};-->
<!--</script>-->

<!--<style scoped>-->
<!--.query-page {-->
<!--  padding: 30px;-->
<!--  background: #f5f7fa;-->
<!--  min-height: 100vh;-->
<!--}-->

<!--.box-card {-->
<!--  max-width: 1100px;-->
<!--  margin: 0 auto;-->
<!--  border-radius: 12px;-->
<!--  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);-->
<!--}-->

<!--.el-card__header {-->
<!--  background-color: #f0f2f5;-->
<!--  font-size: 18px;-->
<!--  font-weight: 600;-->
<!--  color: #333;-->
<!--  padding: 16px 20px;-->
<!--  border-bottom: 1px solid #ebeef5;-->
<!--  border-radius: 12px 12px 0 0;-->
<!--}-->

<!--.form-layout {-->
<!--  padding: 20px;-->
<!--}-->

<!--.el-form-item {-->
<!--  margin-bottom: 20px;-->
<!--}-->

<!--.el-select,-->
<!--.el-date-picker {-->
<!--  width: 100%;-->
<!--}-->

<!--.form-actions {-->
<!--  text-align: right;-->
<!--  margin-top: 10px;-->
<!--}-->

<!--.el-button {-->
<!--  min-width: 100px;-->
<!--}-->

<!--.el-button:hover {-->
<!--  opacity: 0.9;-->
<!--}-->

<!--@media (max-width: 768px) {-->
<!--  .form-actions {-->
<!--    text-align: center;-->
<!--    margin-top: 20px;-->
<!--  }-->
<!--}-->

<!--</style>-->
<!--<template>-->
<!--  <div>-->
<!--    <el-row :gutter="15">-->
<!--      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">-->
<!--        <el-col :span="12">-->
<!--          <el-form-item label="车牌类型" prop="field103">-->
<!--            <el-select v-model="formData.field103" placeholder="请选择车牌类型" clearable :style="{width: '100%'}">-->
<!--              <el-option v-for="(item, index) in field103Options" :key="index" :label="item.label"-->
<!--                         :value="item.value" :disabled="item.disabled"></el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <el-form-item label="车膜颜色" prop="field104">-->
<!--            <el-select v-model="formData.field104" placeholder="请选择车膜颜色" clearable :style="{width: '100%'}">-->
<!--              <el-option v-for="(item, index) in field104Options" :key="index" :label="item.label"-->
<!--                         :value="item.value" :disabled="item.disabled"></el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <el-form-item label="车辆类型" prop="field105">-->
<!--            <el-select v-model="formData.field105" placeholder="请选择车辆类型" clearable :style="{width: '100%'}">-->
<!--              <el-option v-for="(item, index) in field105Options" :key="index" :label="item.label"-->
<!--                         :value="item.value" :disabled="item.disabled"></el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <el-form-item label="车牌颜色" prop="field111">-->
<!--            <el-select v-model="formData.field111" placeholder="请选择车牌颜色" clearable :style="{width: '100%'}">-->
<!--              <el-option v-for="(item, index) in field111Options" :key="index" :label="item.label"-->
<!--                         :value="item.value" :disabled="item.disabled"></el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <el-form-item label="车牌号码" prop="field110">-->
<!--            <el-select v-model="formData.field110" placeholder="请选择车牌号码" clearable :style="{width: '100%'}">-->
<!--              <el-option v-for="(item, index) in field110Options" :key="index" :label="item.label"-->
<!--                         :value="item.value" :disabled="item.disabled"></el-option>-->
<!--            </el-select>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <el-form-item label="时间范围" prop="field106">-->
<!--            <el-date-picker type="daterange" v-model="formData.field106" format="yyyy-MM-dd"-->
<!--                            value-format="yyyy-MM-dd" :style="{width: '100%'}" start-placeholder="开始日期"-->
<!--                            end-placeholder="结束日期" range-separator="至" clearable></el-date-picker>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <el-form-item label="" prop="field108">-->
<!--            <el-button type="primary" icon="el-icon-search" size="medium"> 查询 </el-button>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <el-form-item label="" prop="field112">-->
<!--            <el-button type="primary" icon="el-icon-search" size="medium"> 导出 </el-button>-->
<!--          </el-form-item>-->
<!--        </el-col>-->
<!--      </el-form>-->
<!--    </el-row>-->
<!--  </div>-->
<!--</template>-->
<!--<script>-->
<!--export default {-->
<!--  components: {},-->
<!--  props: [],-->
<!--  data() {-->
<!--    return {-->
<!--      formData: {-->
<!--        field103: undefined,-->
<!--        field104: undefined,-->
<!--        field105: undefined,-->
<!--        field111: undefined,-->
<!--        field110: undefined,-->
<!--        field106: null,-->
<!--        field108: '',-->
<!--        field112: undefined,-->
<!--      },-->
<!--      rules: {-->
<!--        field103: [{-->
<!--          required: true,-->
<!--          message: '请选择车牌类型',-->
<!--          trigger: 'change'-->
<!--        }],-->
<!--        field104: [{-->
<!--          required: true,-->
<!--          message: '请选择车膜颜色',-->
<!--          trigger: 'change'-->
<!--        }],-->
<!--        field105: [{-->
<!--          required: true,-->
<!--          message: '请选择车辆类型',-->
<!--          trigger: 'change'-->
<!--        }],-->
<!--        field111: [{-->
<!--          required: true,-->
<!--          message: '请选择车牌颜色',-->
<!--          trigger: 'change'-->
<!--        }],-->
<!--        field110: [{-->
<!--          required: true,-->
<!--          message: '请选择车牌号码',-->
<!--          trigger: 'change'-->
<!--        }],-->
<!--        field106: [{-->
<!--          required: true,-->
<!--          message: '时间范围不能为空',-->
<!--          trigger: 'change'-->
<!--        }],-->
<!--      },-->
<!--      field103Options: [{-->
<!--        "label": "选项一",-->
<!--        "value": 1-->
<!--      }, {-->
<!--        "label": "选项二",-->
<!--        "value": 2-->
<!--      }],-->
<!--      field104Options: [{-->
<!--        "label": "深色膜",-->
<!--        "value": 1-->
<!--      }, {-->
<!--        "label": "浅色膜",-->
<!--        "value": 2-->
<!--      }],-->
<!--      field105Options: [{-->
<!--        "label": "选项一",-->
<!--        "value": 1-->
<!--      }, {-->
<!--        "label": "选项二",-->
<!--        "value": 2-->
<!--      }],-->
<!--      field111Options: [{-->
<!--        "label": "选项一",-->
<!--        "value": 1-->
<!--      }, {-->
<!--        "label": "选项二",-->
<!--        "value": 2-->
<!--      }],-->
<!--      field110Options: [{-->
<!--        "label": "选项一",-->
<!--        "value": 1-->
<!--      }, {-->
<!--        "label": "选项二",-->
<!--        "value": 2-->
<!--      }],-->
<!--    }-->
<!--  },-->
<!--  computed: {},-->
<!--  watch: {},-->
<!--  created() {},-->
<!--  mounted() {},-->
<!--  methods: {-->
<!--    submitForm() {-->
<!--      this.$refs['elForm'].validate(valid => {-->
<!--        if (!valid) return-->
<!--        // TODO 提交表单-->
<!--      })-->
<!--    },-->
<!--    resetForm() {-->
<!--      this.$refs['elForm'].resetFields()-->
<!--    },-->
<!--  }-->
<!--}-->

<!--</script>-->
<!--<style>-->
<!--</style>-->
<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <el-form :model="queryParams" ref="queryForm" :inline="true" label-width="80px" size="small">
      <el-form-item label="车牌类型" prop="plateType">
        <el-select v-model="queryParams.plateType" placeholder="请选择" clearable>
          <el-option
            v-for="item in plateTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="车辆类型" prop="vehicleType">
        <el-select v-model="queryParams.vehicleType" placeholder="请选择" clearable>
          <el-option
            v-for="item in vehicleTypeOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="车牌号码" prop="plate">
        <el-input
          v-model="queryParams.plate"
          placeholder="请输入车牌号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="车身颜色" prop="vehicleColor">
        <el-select v-model="queryParams.vehicleColor" placeholder="请选择" clearable>
          <el-option
            v-for="item in vehicleColorOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="车牌颜色" prop="plateColor">
        <el-select v-model="queryParams.plateColor" placeholder="请选择" clearable>
          <el-option
            v-for="item in plateColorOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>

      <el-form-item label="时间范围" prop="dateRange">
        <el-date-picker
          v-model="dateRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始时间"
          end-placeholder="结束时间"
          value-format="yyyy-MM-dd HH:mm:ss"
          :default-time="['00:00:00', '23:59:59']"
          style="width: 360px"
        />
      </el-form-item>
      <el-form-item label="嫌疑度" prop="plate">
        <el-input
          v-model="queryParams.level"
          placeholder="请输入最小嫌疑度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        <el-button
          type="warning"
          icon="el-icon-download"
          @click="handleExport"
          v-hasPermi="['system:suspectVehicle:export']"
        >导出
        </el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="list" border stripe>
<!--      <el-table-column prop="cameraName" label="相机名称" width="120" align="center"/>-->
      <el-table-column prop="laneNo" label="车道号" width="80" align="center"/>
      <el-table-column prop="direction" label="方向" width="80" align="center"/>
      <el-table-column prop="speed" label="车速(km/h)" width="90" align="center"/>
      <el-table-column prop="plate" label="车牌" width="120" align="center"/>
      <el-table-column prop="plateType" label="车牌类型" width="100" align="center"/>
      <el-table-column prop="plateColor" label="车牌颜色" width="100" align="center"/>
<!--      <el-table-column prop="vehicleColor" label="车身颜色" width="100" align="center"/>-->
<!--      <el-table-column prop="vehicleType" label="车辆类型" width="100" align="center"/>-->
      <el-table-column prop="level" label="嫌疑度" width="100" align="center"/>
      <el-table-column prop="captureTime" label="捕获时间" width="160" align="center">
        <template slot-scope="scope">
          {{ scope.row.captureTime }} <!-- 直接显示原始值 -->
        </template>
      </el-table-column>
      <el-table-column label="操作" width="100" align="center" fixed="right">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="viewImage(scope.row)">查看图片</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
  </div>
</template>

<script>
// 引入 Ruoyi 时间工具（Vue 2 兼容）
import {parseTime} from "@/utils/ruoyi";
import request from "@/utils/request";

export default {
  data() {
    return {
      // 加载状态
      loading: true,
      // 总条数
      total: 0,
      // 列表数据
      list: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        plate: undefined,
        plateType: undefined,
        plateColor: undefined,
        vehicleType: undefined,
        vehicleColor: undefined,
        startTime: undefined,
        endTime: undefined,
        level: undefined
      },
      // 时间范围
      dateRange: [],
      // 字典数据（硬编码，实际建议从字典接口加载）
      plateTypeOptions: [
        {value: "01", label: "小型汽车"},
        {value: "02", label: "大型汽车"},
        {value: "03", label: "新能源汽车"}
      ],
      vehicleTypeOptions: [
        {value: "01", label: "轿车"},
        {value: "02", label: "SUV"},
        {value: "03", label: "货车"}
      ],
      vehicleColorOptions: [
        {value: "白色", label: "白色"},
        {value: "灰色", label: "灰色"},
        {value: "黑色", label: "黑色"},
        {value: "红色", label: "红色"},
        {value: "棕色", label: "棕色"},
        {value: "未知", label: "未知"},
        {value: "黄色", label: "黄色"},
        {value: "深蓝色", label: "深蓝色"},
        {value: "紫色", label: "紫色"}
      ],
      plateColorOptions: [
        {value: "blue", label: "蓝色"},
        {value: "yellow", label: "黄色"},
        {value: "green", label: "绿色"},
        {value: "black", label: "黑色"}
      ]
    };
  },
  created() {
    this.getList();
  },
  methods: {
    // 暴露 parseTime 到模板
    parseTime,

    // 查询列表
    getList() {
      // alert("测试代码已启用getList方法");
      this.loading = true;

      // 处理时间范围
      if (this.dateRange && this.dateRange.length === 2) {
        this.queryParams.startTime = this.dateRange[0];
        this.queryParams.endTime = this.dateRange[1];
      } else {
        this.queryParams.startTime = undefined;
        this.queryParams.endTime = undefined;
      }

      // 使用 this.$http 发起请求（Ruoyi-Vue 2 内置）
      request({
        url: "/suspectVehicle/list",
        method: "get",
        params: this.queryParams
      }).then(response => {
        this.list = response.rows || [];
        this.total = response.total || 0;
        this.loading = false;
      }).catch(error => {
        console.error("请求异常", error);
        this.loading = false;
      });
    },

    // 搜索
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    // 重置
    resetQuery() {
      this.$refs.queryForm.resetFields();
      this.dateRange = [];
      this.handleQuery();
    },

    // 导出
    handleExport() {
      this.$confirm("是否确认导出所有匹配数据？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$http({
          url: "/system/suspectVehicle/export",
          method: "get",
          params: this.queryParams,
          responseType: "blob"
        }).then(response => {
          const filename = `嫌疑车辆数据_${new Date().getTime()}.xlsx`;
          const blob = new Blob([response]);
          const link = document.createElement("a");
          link.href = URL.createObjectURL(blob);
          link.download = filename;
          link.click();
          URL.revokeObjectURL(link.href);
        }).catch(() => {
          this.$message.error("导出失败");
        });
      });
    },

    // 查看图片
    viewImage(row) {
      if (!row.picUrl) {
        this.$message.warning("无抓拍图片");
        return;
      }
      this.$alert(
        `<img src="${row.picUrl}?t=${Date.now()}" style="max-width:100%; max-height:500px;">`,
        "车辆抓拍图",
        {
          dangerouslyUseHTMLString: true,
          confirmButtonText: "关闭",
          customClass: "image-dialog"
        }
      );
    }
  }
};
</script>

<style scoped>
.image-dialog .el-message-box__content {
  text-align: center;
  padding: 20px 0;
}

.image-dialog img {
  border: 1px solid #eee;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>
