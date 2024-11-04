<template>
  <div class="app-container">
    <div class="page-title">
      <el-row type="flex" justify="center" align="middle">
        <el-col :span="24">
          <h1>预警信息</h1>
        </el-col>
      </el-row>
    </div>
    <el-row :gutter="20">
      <!-- 摄像头数据 -->
      <el-col>
        <el-table v-loading="loading" :data="cautionList" @selection-change="handleSelectionChange">
          <el-table-column label="警示记录id" align="center" prop="cautionId" />
          <el-table-column label="检测点" align="center" prop="detectionId">
            <template slot-scope="scope">
              {{ getDetectionName(scope.row.detectionId) }}
            </template>
          </el-table-column>
          <el-table-column label="车行为id" align="center" prop="behaviorId" />
          <el-table-column label="警示效果" align="center" prop="warningEffect" />
          <el-table-column label="状态" align="center" prop="status">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_caution_status" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" prop="remark" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                         v-hasPermi="['caution:cautionLog:edit']">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                         v-hasPermi="['caution:cautionLog:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
                    @pagination="getList" />
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { listCaution, getCaution, delCaution, addCaution, updateCaution } from "@/api/cigarette/caution/caution";
import { listDistrict } from "@/api/cigarette/detection/district";
import { listDetection } from "@/api/cigarette/detection/detection";

export default {
  name: "Caution",
  dicts: ['tob_caution_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 预警记录表格数据
      cautionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      treeData : [],
      districts: [],
      detections: [],
      detectionOptions: [],    //json数组，用于存储检测点选项
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        detectionId: null,
        behaviorId: null,
        warningEffect: null,
        status: null,
      },
    };
  },
  created() {
    this.getList();
    this.loadDetectionOptions(); // 加载检测点选项
    this.loadDistrictOptions(); // 加载地区选项
  },
  methods: {
    getDistrictName(districtId) {
      const district = this.districtOptions.find(item => item.districtId === districtId);
      return district ? district.districtName : '未知地区';
    },
    // 根据 detectionId 获取监测点名字
    getDetectionName(detectionId) {
      const detection = this.detectionOptions.find(item => item.detectionId === detectionId)
      return detection ? detection.detectionName : '未知监测点';
    },

    async getList() {
      try {
        const districtsResponse = await listDistrict();
        this.districts = districtsResponse.data;

        const detectionsResponse = await listDetection();
        this.detections = detectionsResponse.rows;

        listCaution(this.queryParams).then(response => {
          this.cautionList = response.rows;
          console.log("表格展示按钮");
          console.log(response.rows);
          this.total = response.total;
        });

        this.buildTreeData();
        this.loading = false;
      } catch (error) {
        console.error('Failed to fetch data:', error);
        this.loading = false;
      }
    },
  }
};
</script>
<style>
.bg-color {
  background-color: #f0f0f0;
}

.page-title h1 {
  font-size: 24px;
  margin: 20px 0;
  color: #333;
}

.selected {
  background-color: #d0e8f2;
  /* 天蓝色背景 */
}
</style>


