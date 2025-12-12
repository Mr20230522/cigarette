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
        {value: "黄色双行车尾牌", label: "黄色双行车尾牌"},
        {value: "92式民用车", label: "92式民用车"},
        {value: "未知", label: "未知"},
        {value: "新能源车牌", label: "新能源车牌"},
        {value: "摩托车", label: "摩托车"},
        {value: "教练车", label: "教练车"},
        {value: "黄色1225农用车", label: "黄色1225农用车"}
      ],
      vehicleTypeOptions: [
        {value: "货车", label: "货车"},
        {value: "轿车", label: "轿车"},
        {value: "小货车", label: "小货车"},
        {value: "SUV/MPV", label: "SUV/MPV"},
        {value: "二轮车", label: "二轮车"},
        {value: "面包车", label: "面包车"},
        {value: "皮卡车", label: "皮卡车"},
        {value: "三轮车", label: "三轮车"},
        {value: "行人", label: "行人"},
        {value: "大型客车", label: "大型客车"},
        {value: "非机动车", label: "非机动车"}
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
