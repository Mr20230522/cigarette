<template>
  <div>
    <el-row :gutter="20">
      <!-- 地区数据 -->
      <el-col :span="4" :xs="24">
        <!-- <div>
                    <el-input v-model="districtName" placeholder="请输入地区名称" clearable size="small"
                        prefix-icon="el-icon-search" style="margin-bottom: 20px" />
                </div> -->
        <div>
          <el-tree :data="treeData" node-key="nodeKey" default-expand-all :props="defaultProps"
            @node-click="handleNodeClick">
          </el-tree>
        </div>
      </el-col>
      <!-- 摄像头数据 -->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
          label-width="68px">
          <el-form-item label="监测点id" prop="detectionId" >
            <el-input v-model="queryParams.detectionId" placeholder="请输入监测点id" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="车行为id" prop="behaviorId">
            <el-input v-model="queryParams.behaviorId" placeholder="请输入车行为id" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="预警程度" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择预警程度" clearable>
              <el-option v-for="dict in dict.type.tob_caution_status" :key="dict.value" :label="dict.label"
                :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
              v-hasPermi="['caution:cautionLog:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
              v-hasPermi="['caution:cautionLog:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
              v-hasPermi="['caution:cautionLog:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
              v-hasPermi="['caution:cautionLog:export']">导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="cautionList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="警示记录id" align="center" prop="cautionId" />
          <el-table-column label="检测点" align="center" prop="detectionId" :show-overflow-tooltip="true">
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
          <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true" />
          <el-table-column fixed="right" label="操作" align="center" class-name="small-padding fixed-width" >
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
    <!-- 添加或修改预警记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="检测点" prop="detectionId" >
                    <el-select v-model="form.detectionId" placeholder="请选择所管理的监测点" filterable
                        @change="handleDetectionChange">
                        <el-option v-for="item in detectionOptions" :key="item.detectionId"
                            :label="getDetectionName(item.detectionId)" :value="item.detectionId"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="预警程度" prop="status">
              <el-select v-model="form.status" placeholder="请选择预警程度">
                <el-option v-for="dict in dict.type.tob_caution_status" :key="dict.value" :label="dict.label"
                  :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
        <el-form-item label="车行为id" prop="behaviorId">
          <el-input v-model="form.behaviorId" placeholder="请输入车行为id" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
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
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        detectionId: [
          { required: true, message: "监测点id不能为空", trigger: "blur" }
        ],
        behaviorId: [
          { required: true, message: "车行为id不能为空", trigger: "blur" }
        ],
        warningEffect: [
          { required: true, message: "警示效果不能为空", trigger: "change" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ]
      }
    };
  },
  created() {
    this.getList();
    this.loadDetectionOptions(); // 加载检测点选项
    this.loadDistrictOptions(); // 加载地区选项
  },
  methods: {
    //加载地区选项
    loadDistrictOptions() {
      listDistrict().then(response => {
        this.districtOptions = response.data.map(item => ({
          districtId: item.districtId,
          districtName: item.districtName
        }));
      }).catch(error => {
        console.error("Failed to load district options:", error);
      });
    },
    // 加载检测点选项
    loadDetectionOptions() {
      listDetection().then(response => {
        this.detectionOptions = response.rows.map(item => ({
          detectionId: item.detectionId,
          districtId: item.districtId,
          detectionName: item.detectionName
        }));
      }).catch(error => {
        console.error("Failed to load detection options:", error);
      });
    },

        // 当检测点变更时触发，自动填充地区ID
        handleDetectionChange(newValue) {
      // 通过检测点ID找到对应的地区ID
      const selectedDetection = this.detectionOptions.find(item => item.detectionId === newValue);
      if (selectedDetection) {
        // 将地区ID填充到表单的districtId字段
        this.form.districtId = selectedDetection.districtId;
      }
    },    getDistrictName(districtId) {
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
          this.total = response.total;
        });

        this.buildTreeData();
        this.loading = false;
      } catch (error) {
        console.error('Failed to fetch data:', error);
        this.loading = false;
      }
    },
    buildTreeData() {
      const districtMap = new Map();

      this.districts.forEach(district => {
        districtMap.set(district.districtId, {
          ...district,
          label: district.districtName,
          nodeKey: `district-${district.districtId}`,
          children: []
        });
      });

      this.detections.forEach(detection => {
        const district = districtMap.get(detection.districtId);
        if (district) {
          district.children.push({
            label: detection.detectionName,
            nodeKey: `detection-${detection.detectionId}`,
            type: 'detection',
            ...detection
          });
        }
      });

      this.treeData = [];
      districtMap.forEach((node, districtId) => {
        if (node.parentId === null || node.parentId === 0) {
          this.treeData.push(node);
        } else {
          const parentNode = districtMap.get(node.parentId);
          if (parentNode) {
            parentNode.children.push(node);
          }
        }
      });
    },
    handleNodeClick(data, node) {
      const detectionIds = [];

      const collectDetectionIds = (node) => {
        if (node.type === 'detection') {
          detectionIds.push(node.detectionId);
        }
        if (node.children && node.children.length) {
          node.children.forEach(child => collectDetectionIds(child));
        }
      };

      collectDetectionIds(data);

      // 如果有多个 detectionId，循环发送请求并合并结果
      if (detectionIds.length > 1) {
        this.cautionList = [];
        detectionIds.forEach(async (id) => {
          this.queryParams.detectionId = id;
          const response = await listCaution(this.queryParams);
          this.cautionList.push(...response.rows);
        });
      } else {
        // 只有一个 detectionId，直接发送请求
        this.queryParams.detectionId = detectionIds[0];
        this.getList();
      }
    },

    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.cameraId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 查询预警记录列表 */
    // getList() {
    //   this.loading = true;
    //   listCaution(this.queryParams).then(response => {
    //     this.cautionList = response.rows;
    //     this.total = response.total;
    //     this.loading = false;
    //   });
    // },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        cautionId: null,
        detectionId: null,
        behaviorId: null,
        warningEffect: null,
        status: null,
        remark: null,
        createTime: null,
        updateTime: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.cautionId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加预警记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const cautionId = row.cautionId || this.ids
      getCaution(cautionId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改预警记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.cautionId != null) {
            updateCaution(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCaution(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const cautionIds = row.cautionId || this.ids;
      this.$modal.confirm('是否确认删除预警记录编号为"' + cautionIds + '"的数据项？').then(function () {
        return delCaution(cautionIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cigarette/caution/cautionLog/export', {
        ...this.queryParams
      }, `caution_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
