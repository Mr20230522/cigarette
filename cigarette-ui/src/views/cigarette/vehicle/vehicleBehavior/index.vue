<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车id" prop="carId">
        <el-input
          v-model="queryParams.carId"
          placeholder="请输入车id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车型" prop="carTypeId">
        <el-input
          v-model="queryParams.carTypeId"
          placeholder="请输入车型"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车的颜色" prop="carColor">
        <el-input
          v-model="queryParams.carColor"
          placeholder="请输入车的颜色"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="嫌疑程度" prop="degreeSuspicion">
        <el-input
          v-model="queryParams.degreeSuspicion"
          placeholder="请输入嫌疑程度"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="驾驶人id" prop="driverId">
        <el-input
          v-model="queryParams.driverId"
          placeholder="请输入驾驶人id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="行驶方向" prop="drivingDirection">
        <el-select v-model="queryParams.drivingDirection" placeholder="请选择行驶方向" clearable>
          <el-option
            v-for="dict in dict.type.tob_driving_irection"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="违法状态" prop="illegalStatus">
        <el-select v-model="queryParams.illegalStatus" placeholder="请选择违法状态" clearable>
          <el-option
            v-for="dict in dict.type.tob_illegal_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="同伙id" prop="accompliceId">
        <el-input
          v-model="queryParams.accompliceId"
          placeholder="请输入同伙id"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['vehicle:vehicleBehavior:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['vehicle:vehicleBehavior:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['vehicle:vehicleBehavior:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['vehicle:vehicleBehavior:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vehicleBehaviorList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="车行为id" align="center" prop="behaviorId" />
      <el-table-column label="车id" align="center" prop="carId" />
      <el-table-column label="车型" align="center" prop="carTypeId" />
      <el-table-column label="车的颜色" align="center" prop="carColor" />
      <el-table-column label="嫌疑程度" align="center" prop="degreeSuspicion" />
      <el-table-column label="驾驶人id" align="center" prop="driverId" />
      <el-table-column label="行驶方向" align="center" prop="drivingDirection">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.tob_driving_irection" :value="scope.row.drivingDirection"/>
        </template>
      </el-table-column>
      <el-table-column label="违法状态" align="center" prop="illegalStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.tob_illegal_status" :value="scope.row.illegalStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="同伙id" align="center" prop="accompliceId" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['vehicle:vehicleBehavior:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['vehicle:vehicleBehavior:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    
    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 添加或修改车辆行为记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车id" prop="carId">
          <el-input v-model="form.carId" placeholder="请输入车id" />
        </el-form-item>
        <el-form-item label="车型" prop="carTypeId">
          <el-input v-model="form.carTypeId" placeholder="请输入车型" />
        </el-form-item>
        <el-form-item label="车的颜色" prop="carColor">
          <el-input v-model="form.carColor" placeholder="请输入车的颜色" />
        </el-form-item>
        <el-form-item label="嫌疑程度" prop="degreeSuspicion">
          <el-input v-model="form.degreeSuspicion" placeholder="请输入嫌疑程度" />
        </el-form-item>
        <el-form-item label="驾驶人id" prop="driverId">
          <el-input v-model="form.driverId" placeholder="请输入驾驶人id" />
        </el-form-item>
        <el-form-item label="行驶方向" prop="drivingDirection">
          <el-radio-group v-model="form.drivingDirection">
            <el-radio
              v-for="dict in dict.type.tob_driving_irection"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="违法状态" prop="illegalStatus">
          <el-radio-group v-model="form.illegalStatus">
            <el-radio
              v-for="dict in dict.type.tob_illegal_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.sys_normal_disable"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="删除标记" prop="delFlag">
          <el-input v-model="form.delFlag" placeholder="请输入删除标记" />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="同伙id" prop="accompliceId">
          <el-input v-model="form.accompliceId" placeholder="请输入同伙id" />
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
import { listVehicleBehavior, getVehicleBehavior, delVehicleBehavior, addVehicleBehavior, updateVehicleBehavior } from "@/api/cigarette/vehicle/vehicleBehavior";

export default {
  name: "VehicleBehavior",
  dicts: ['tob_illegal_status', 'sys_normal_disable', 'tob_driving_irection'],
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
      // 车辆行为记录表格数据
      vehicleBehaviorList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        carId: null,
        carTypeId: null,
        carColor: null,
        degreeSuspicion: null,
        driverId: null,
        drivingDirection: null,
        illegalStatus: null,
        status: null,
        accompliceId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        carId: [
          { required: true, message: "车id不能为空", trigger: "blur" }
        ],
        carTypeId: [
          { required: true, message: "车型不能为空", trigger: "blur" }
        ],
        degreeSuspicion: [
          { required: true, message: "嫌疑程度不能为空", trigger: "blur" }
        ],
        drivingDirection: [
          { required: true, message: "行驶方向不能为空", trigger: "change" }
        ],
        illegalStatus: [
          { required: true, message: "违法状态不能为空", trigger: "change" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
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
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询车辆行为记录列表 */
    getList() {
      this.loading = true;
      listVehicleBehavior(this.queryParams).then(response => {
        this.vehicleBehaviorList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        behaviorId: null,
        carId: null,
        carTypeId: null,
        carColor: null,
        degreeSuspicion: null,
        driverId: null,
        drivingDirection: null,
        illegalStatus: null,
        status: null,
        delFlag: null,
        remark: null,
        createTime: null,
        updateTime: null,
        accompliceId: null
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
      this.ids = selection.map(item => item.behaviorId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加车辆行为记录";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const behaviorId = row.behaviorId || this.ids
      getVehicleBehavior(behaviorId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改车辆行为记录";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.behaviorId != null) {
            updateVehicleBehavior(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVehicleBehavior(this.form).then(response => {
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
      const behaviorIds = row.behaviorId || this.ids;
      this.$modal.confirm('是否确认删除车辆行为记录编号为"' + behaviorIds + '"的数据项？').then(function() {
        return delVehicleBehavior(behaviorIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cigarette/vehicle/vehicleBehavior/export', {
        ...this.queryParams
      }, `vehicleBehavior_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
