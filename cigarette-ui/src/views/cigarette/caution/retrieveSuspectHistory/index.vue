<template>
  <div class="query-page">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>历史数据查询</span>
      </div>

      <el-form :model="form" label-width="100px" class="form-inline" inline>
        <el-form-item label="嫌疑程度">
          <el-select v-model="form.suspicion" placeholder="请选择">
            <el-option
              v-for="item in suspicionOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="时间范围">
          <el-date-picker
            v-model="form.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            format="yyyy-MM-dd"
            value-format="yyyy-MM-dd"
          />
        </el-form-item>

        <el-form-item label="车型">
          <el-select v-model="form.carType" placeholder="请选择">
            <el-option
              v-for="item in carTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item label="颜色">
          <el-select v-model="form.color" placeholder="请选择">
            <el-option
              v-for="item in colorOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="onSearch">查询</el-button>
          <el-button @click="onReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
export default {
  data() {
    return {
      form: {
        suspicion: '',
        dateRange: [],
        carType: '',
        color: '',
      },
      suspicionOptions: [
        { label: '高', value: 'high' },
        { label: '中', value: 'medium' },
        { label: '低', value: 'low' },
      ],
      carTypeOptions: [
        { label: '轿车', value: 'sedan' },
        { label: 'SUV', value: 'suv' },
        { label: '货车', value: 'truck' },
        { label: '其他', value: 'other' },
      ],
      colorOptions: [
        { label: '黑色', value: 'black' },
        { label: '白色', value: 'white' },
        { label: '红色', value: 'red' },
        { label: '蓝色', value: 'blue' },
        { label: '银色', value: 'silver' },
      ],
    };
  },
  methods: {
    onSearch() {
      console.log('查询参数：', this.form);
      // 可添加API请求
    },
    onReset() {
      this.form = {
        suspicion: '',
        dateRange: [],
        carType: '',
        color: '',
      };
    },
  },
};
</script>

<style scoped>
.query-page {
  padding: 20px;
}
.box-card {
  max-width: 1000px;
  margin: 0 auto;
}
.form-inline {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}
.el-form-item {
  min-width: 200px;
}
</style>
