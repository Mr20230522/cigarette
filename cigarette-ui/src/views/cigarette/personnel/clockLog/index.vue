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
          <el-form-item label="检测人员id" prop="inspectorId">
            <el-input v-model="queryParams.inspectorId" placeholder="请输入检测人员id" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="值班日期" prop="dutyDate">
            <el-date-picker clearable v-model="queryParams.dutyDate" type="date" value-format="yyyy-MM-dd"
              placeholder="请选择值班日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
            <el-date-picker clearable v-model="queryParams.startTime" type="date" value-format="yyyy-MM-dd"
              placeholder="请选择开始时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-date-picker clearable v-model="queryParams.endTime" type="date" value-format="yyyy-MM-dd"
              placeholder="请选择结束时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="出勤有误" prop="flag">
            <el-input v-model="queryParams.flag" placeholder="请输入出勤有误" clearable @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
              v-hasPermi="['personnel:clockLog:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
              v-hasPermi="['personnel:clockLog:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
              v-hasPermi="['personnel:clockLog:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
              v-hasPermi="['personnel:clockLog:export']">导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="logList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="出勤ID" align="center" prop="commutingId" />
          <el-table-column label="检测人员id" align="center" prop="inspectorId" />
          <el-table-column label="值班日期" align="center" prop="dutyDate" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.dutyDate, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="开始时间" align="center" prop="startTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="结束时间" align="center" prop="endTime" width="180">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="值班类型" align="center" prop="dutyType" />
          <el-table-column label="备注" align="center" prop="remark" />
          <el-table-column label="出勤有误" align="center" prop="flag">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_clock_correct" :value="scope.row.flag" />
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['personnel:clockLog:edit']">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                v-hasPermi="['personnel:clockLog:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>
    <!-- 添加或修改出勤记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="检测人员id" prop="inspectorId">
          <el-input v-model="form.inspectorId" placeholder="请输入检测人员id" />
        </el-form-item>
        <el-form-item label="值班日期" prop="dutyDate">
          <el-date-picker clearable v-model="form.dutyDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择值班日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable v-model="form.startTime" type="date" value-format="yyyy-MM-dd"
            placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable v-model="form.endTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="删除标记" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标记" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="出勤有误" prop="flag">
          <el-input v-model="form.flag" placeholder="请输入出勤有误" />
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
import { listLog, getLog, delLog, addLog, updateLog } from "@/api/cigarette/personnel/clockLog";
import { listDistrict } from "@/api/cigarette/detection/district";
import { listDetection } from "@/api/cigarette/detection/detection";

export default {
  name: "Log",
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
      // 出勤记录表格数据
      logList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        inspectorId: null,
        dutyDate: null,
        startTime: null,
        endTime: null,
        dutyType: null,
        flag: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        inspectorId: [
          { required: true, message: "检测人员id不能为空", trigger: "blur" }
        ],
        dutyDate: [
          { required: true, message: "值班日期不能为空", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
        dutyType: [
          { required: true, message: "值班类型不能为空", trigger: "change" }
        ],
        delFlag: [
          { required: true, message: "删除标记不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
        flag: [
          { required: true, message: "出勤有误不能为空", trigger: "blur" }
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
    async getList() {
      try {
        const districtsResponse = await listDistrict();
        this.districts = districtsResponse.data;

        const detectionsResponse = await listDetection();
        this.detections = detectionsResponse.rows;

        listLog(this.queryParams).then(response => {
          this.logList = response.rows;
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
        this.cameraList = [];
        detectionIds.forEach(async (id) => {
          this.queryParams.detectionId = id;
          const response = await listCamera(this.queryParams);
          this.cameraList.push(...response.rows);
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
    /** 查询出勤记录列表 */
    // getList() {
    //   this.loading = true;
    //   listLog(this.queryParams).then(response => {
    //     this.logList = response.rows;
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
        commutingId: null,
        inspectorId: null,
        dutyDate: null,
        startTime: null,
        endTime: null,
        dutyType: null,
        delFlag: null,
        remark: null,
        createTime: null,
        updateTime: null,
        flag: null
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
      this.ids = selection.map(item => item.commutingId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加出勤记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const commutingId = row.commutingId || this.ids
      getLog(commutingId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改出勤记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.commutingId != null) {
            updateLog(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addLog(this.form).then(response => {
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
      const commutingIds = row.commutingId || this.ids;
      this.$modal.confirm('是否确认删除出勤记录编号为"' + commutingIds + '"的数据项？').then(function () {
        return delLog(commutingIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cigarette/personnel/clockLog/export', {
        ...this.queryParams
      }, `log_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
