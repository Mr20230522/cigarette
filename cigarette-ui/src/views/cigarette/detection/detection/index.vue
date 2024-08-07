<template>
  <div class="app-container">
    <el-row :gutter="20">
      <!-- 地区数据 -->
      <el-col :span="4" :xs="24">
        <div class="head-container">
          <el-input v-model="districtName" placeholder="请输入地区名称" clearable size="small" prefix-icon="el-icon-search"
            style="margin-bottom: 20px" />
        </div>
        <div class="head-container">
          <el-tree :data="districtTreeOptions" :props="defaultProps" :expand-on-click-node="false"
            :filter-node-method="filterNode" ref="tree" node-key="id" default-expand-all highlight-current
            @node-click="handleNodeClickByDistrictId" />
        </div>
      </el-col>
      <!-- 监测点数据 -->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
          label-width="68px">
          <el-form-item label="监测点顺序" prop="orderNum">
            <el-input v-model="queryParams.orderNum" placeholder="请输入监测点顺序" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="监测点名称" prop="detectionName">
            <el-input v-model="queryParams.detectionName" placeholder="请输入监测点名称" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
              <el-option v-for="dict in dict.type.tob_dd_status" :key="dict.value" :label="dict.label"
                :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="删除标记" prop="delFlag">
            <el-select v-model="queryParams.delFlag" placeholder="请选择删除标记" clearable>
              <el-option v-for="dict in dict.type.tob_del_flag" :key="dict.value" :label="dict.label"
                :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="负责人" prop="responsibleId">
            <el-input v-model="queryParams.responsibleId" placeholder="请选择负责人" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
              v-hasPermi="['cigarette:detection:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
              v-hasPermi="['cigarette:detection:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
              v-hasPermi="['cigarette:detection:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
              v-hasPermi="['cigarette:detection:export']">导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="detectionList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="监测区域id" align="center" prop="detectionId" />
          <el-table-column label="监测点顺序" align="center" prop="orderNum" />
          <el-table-column label="监测点名称" align="center" prop="detectionName" />
          <!-- 监测点所属地区 -->
          <el-table-column label="监测点所属地区" align="center" prop="districtId">
            <template slot-scope="scope">
              <!-- 使用一个方法来通过districtId找到对应的districtName -->
              {{ getDistrictName(scope.row.districtId) }}
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" prop="status">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_dd_status" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="删除标记" align="center" prop="delFlag">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_del_flag" :value="scope.row.delFlag" />
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" prop="remark" />
          <el-table-column label="负责人" align="center" prop="responsibleId">
            <template slot-scope="scope">
              {{ getStaffName(scope.row.responsibleId) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['cigarette:detection:edit']">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                v-hasPermi="['cigarette:detection:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>
    <!-- 添加或修改监测区域对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="监测点顺序" prop="orderNum">
          <el-input v-model="form.orderNum" placeholder="请输入监测点顺序" />
        </el-form-item>
        <el-form-item label="监测点名称" prop="detectionName">
          <el-input v-model="form.detectionName" placeholder="请输入监测点名称" />
        </el-form-item>
        <!-- 添加下拉框以选择检测区域所属地区 -->
        <!-- <el-form-item label="所属地区" prop="districtId">
          <el-select v-model="form.districtId" placeholder="请选择所属地区" filterable>
            <el-option v-for="item in districtOptions" :key="item.districtId" :label="item.districtName"
              :value="item.districtId"></el-option>
          </el-select>
        </el-form-item> -->
        <el-form-item label="所属地区" prop="districtId">
          <treeselect v-model="form.districtId" :options="districtTreeOptions" placeholder="请选择所属地区" />
        </el-form-item>


        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio v-for="dict in dict.type.tob_dd_status" :key="dict.value" :label="dict.value">{{ dict.label
              }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="删除标记" prop="delFlag">
          <el-radio-group v-model="form.delFlag">
            <el-radio v-for="dict in dict.type.tob_del_flag" :key="dict.value" :label="dict.value">{{ dict.label
              }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <!-- 添加下拉框以选择负责人所对应的工作人员 -->
        <el-form-item label="负责人" prop="responsibleId">
          <el-select v-model="form.responsibleId" placeholder="请选择负责人" filterable>
            <el-option v-for="item in staffOptions" :key="item.staffId" :label="getStaffName(item.staffId)"
              :value="item.staffId"></el-option>
          </el-select>
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
import { listDetection, getDetection, delDetection, addDetection, updateDetection, listDetectionByDistrictId } from "@/api/cigarette/detection/detection";
import { listDistrict, districtTreeSelect } from "@/api/cigarette/detection/district"; // 导入地区列表接口
import { listStaff } from "@/api/cigarette/personnel/staff"; // 导入工作人员列表接口
import { listUser } from "@/api/system/user"; // 导入用户列表接口
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: "Detection",
  dicts: ['tob_del_flag', 'tob_dd_status'],
  components: { Treeselect },
  data() {
    return {
      districtOptions: [], // 用于存储地区选项的数组
      staffOptions: [], // 用于存储工作人员选项的数组
      userOptions: [], // 初始化用户选项     
      // 地区树选项
      districtTreeOptions: undefined,
      // 地区名称
      districtName: undefined,
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
      // 监测区域表格数据
      detectionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        orderNum: null,
        detectionName: null,
        status: null,
        delFlag: null,
        responsibleId: null,
        districtId: null
      },
      // 表单参数
      form: {},
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
        detectionName: [
          { required: true, message: "监测点名称不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        districtId: [
          { required: true, message: '所属地区不能为空', trigger: 'blur' }
        ],
      }
    };
  },
  watch: {
    // 根据名称筛选地区树
    districtName(val) {
      this.$refs.tree.filter(val);
    }
  },
  created() {
    this.getList();
    this.loadDistrictOptions(); // 加载地区选项
    this.loadStaffOptions(); // 加载工作人员选项
    this.loadUserOptions(); // 加载用户选项
    this.getDistrictTree()//加载地区树
  },
  methods: {
    //加载用户选项
    loadUserOptions() {
      listUser().then(response => {
        this.userOptions = response.rows.map(item => ({
          userId: item.userId,
          userName: item.userName
        }));
      }).catch(error => {
        console.error("Failed to load user options:", error);
      });
    },
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
    getDistrictName(districtId) {
      const district = this.districtOptions.find(item => item.districtId === districtId);
      return district ? district.districtName : '未知地区';
    },
    // 加载工作人员选项
    loadStaffOptions() {
      listStaff().then(response => {
        this.staffOptions = response.rows.map(item => ({
          staffId: item.staffId,
          userId: item.userId
        }));
      }).catch(error => {
        console.error("Failed to load staff options:", error);
      });
    },
    getStaffInfo(staffId) {
      return this.staffOptions.find(item => item.staffId === staffId) || {};
    },
    // 根据 responsibleId 获取用户姓名
    getStaffName(staffId) {
      const staffInfo = this.getStaffInfo(staffId);
      // 确保 staffInfo 中有 userId
      if (staffInfo.userId) {
        // 调用 getUserName 方法获取 userName
        return this.getUserName(staffInfo.userId);
      }
      return '未知工作人员';
    },
    // 通过 userId 获取 userName
    getUserName(userId) {
      // 使用 userOptions 数组来查找 userName
      const user = this.userOptions.find(item => item.userId === userId);
      return user ? user.userName : '未知用户名';
    },
    /** 查询监测区域列表 */
    getList() {
      this.loading = true;
      listDetection(this.queryParams).then(response => {
        this.detectionList = response.rows;
        this.total = response.total;
        this.loading = false;

      });
    },
    /** 查询监测区域列表 */
    getListByDistrictId() {
      this.loading = true;
      listDetectionByDistrictId(this.queryParams).then(response => {
        this.detectionList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询地区下拉树结构 */
    getDistrictTree() {
      districtTreeSelect().then(response => {
        this.districtTreeOptions = response.data;
      });
    },
    // 筛选节点
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点单击事件
    handleNodeClick(data) {
      this.queryParams.districtId = data.id;
      this.handleQuery();
    },
    // 节点单击事件
    handleNodeClickByDistrictId(data) {
      this.queryParams.districtId = data.id;
      this.handleQueryByDistrictId();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 搜索按钮操作 */
    handleQueryByDistrictId() {
      this.queryParams.pageNum = 1;
      this.getListByDistrictId();
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        detectionId: null,
        districtId: null,
        orderNum: null,
        districtName: null,
        status: null,
        delFlag: null,
        remark: null,
        createTime: null,
        updateTime: null,
        responsibleId: null
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
      this.ids = selection.map(item => item.detectionId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加监测区域";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const detectionId = row.detectionId || this.ids
      getDetection(detectionId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改监测区域";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.detectionId != null) {
            updateDetection(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDetection(this.form).then(response => {
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
      const detectionIds = row.detectionId || this.ids;
      this.$modal.confirm('是否确认删除监测区域编号为"' + detectionIds + '"的数据项？').then(function () {
        return delDetection(detectionIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cigarette/detection/export', {
        ...this.queryParams
      }, `detection_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>