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
<template>
  <div>
    <el-row :gutter="15">
      <el-form ref="elForm" :model="formData" :rules="rules" size="medium" label-width="100px">
        <el-col :span="12">
          <el-form-item label="车牌类型" prop="field103">
            <el-select v-model="formData.field103" placeholder="请选择车牌类型" clearable :style="{width: '100%'}">
              <el-option v-for="(item, index) in field103Options" :key="index" :label="item.label"
                         :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车膜颜色" prop="field104">
            <el-select v-model="formData.field104" placeholder="请选择车膜颜色" clearable :style="{width: '100%'}">
              <el-option v-for="(item, index) in field104Options" :key="index" :label="item.label"
                         :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车辆类型" prop="field105">
            <el-select v-model="formData.field105" placeholder="请选择车辆类型" clearable :style="{width: '100%'}">
              <el-option v-for="(item, index) in field105Options" :key="index" :label="item.label"
                         :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车牌颜色" prop="field111">
            <el-select v-model="formData.field111" placeholder="请选择车牌颜色" clearable :style="{width: '100%'}">
              <el-option v-for="(item, index) in field111Options" :key="index" :label="item.label"
                         :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="车牌号码" prop="field110">
            <el-select v-model="formData.field110" placeholder="请选择车牌号码" clearable :style="{width: '100%'}">
              <el-option v-for="(item, index) in field110Options" :key="index" :label="item.label"
                         :value="item.value" :disabled="item.disabled"></el-option>
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="时间范围" prop="field106">
            <el-date-picker type="daterange" v-model="formData.field106" format="yyyy-MM-dd"
                            value-format="yyyy-MM-dd" :style="{width: '100%'}" start-placeholder="开始日期"
                            end-placeholder="结束日期" range-separator="至" clearable></el-date-picker>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="" prop="field108">
            <el-button type="primary" icon="el-icon-search" size="medium"> 查询 </el-button>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="" prop="field112">
            <el-button type="primary" icon="el-icon-search" size="medium"> 导出 </el-button>
          </el-form-item>
        </el-col>
      </el-form>
    </el-row>
  </div>
</template>
<script>
export default {
  components: {},
  props: [],
  data() {
    return {
      formData: {
        field103: undefined,
        field104: undefined,
        field105: undefined,
        field111: undefined,
        field110: undefined,
        field106: null,
        field108: '',
        field112: undefined,
      },
      rules: {
        field103: [{
          required: true,
          message: '请选择车牌类型',
          trigger: 'change'
        }],
        field104: [{
          required: true,
          message: '请选择车膜颜色',
          trigger: 'change'
        }],
        field105: [{
          required: true,
          message: '请选择车辆类型',
          trigger: 'change'
        }],
        field111: [{
          required: true,
          message: '请选择车牌颜色',
          trigger: 'change'
        }],
        field110: [{
          required: true,
          message: '请选择车牌号码',
          trigger: 'change'
        }],
        field106: [{
          required: true,
          message: '时间范围不能为空',
          trigger: 'change'
        }],
      },
      field103Options: [{
        "label": "选项一",
        "value": 1
      }, {
        "label": "选项二",
        "value": 2
      }],
      field104Options: [{
        "label": "深色膜",
        "value": 1
      }, {
        "label": "浅色膜",
        "value": 2
      }],
      field105Options: [{
        "label": "选项一",
        "value": 1
      }, {
        "label": "选项二",
        "value": 2
      }],
      field111Options: [{
        "label": "选项一",
        "value": 1
      }, {
        "label": "选项二",
        "value": 2
      }],
      field110Options: [{
        "label": "选项一",
        "value": 1
      }, {
        "label": "选项二",
        "value": 2
      }],
    }
  },
  computed: {},
  watch: {},
  created() {},
  mounted() {},
  methods: {
    submitForm() {
      this.$refs['elForm'].validate(valid => {
        if (!valid) return
        // TODO 提交表单
      })
    },
    resetForm() {
      this.$refs['elForm'].resetFields()
    },
  }
}

</script>
<style>
</style>
