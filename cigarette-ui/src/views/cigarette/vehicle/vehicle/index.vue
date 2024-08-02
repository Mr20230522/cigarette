<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="车牌" prop="licensePlate">
        <el-input
          v-model="queryParams.licensePlate"
          placeholder="请输入车牌"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="车型" prop="carTypeId">
        <el-select v-model="queryParams.carTypeId" placeholder="请选择车型" clearable clearable @keyup.enter.native="handleQuery">
          <el-option
            v-for="carType in carTypeList"
            :key="carType.carTypeId"
            :label="carType.carTypeName"
            :value="carType.carTypeId"

          />
        </el-select>
      </el-form-item>
      <el-form-item label="车身颜色" prop="carColor">
        <el-input
          v-model="queryParams.carColor"
          placeholder="请输入车身颜色"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['vehicle:vehicle:add']"
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
          v-hasPermi="['vehicle:vehicle:edit']"
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
          v-hasPermi="['vehicle:vehicle:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['vehicle:vehicle:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="vehicleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="车辆编号" align="center" prop="carId"  />
      <el-table-column label="车牌编号" align="center" prop="licensePlate"  min-width="120px"/>
      <el-table-column label="车牌类型" align="center" prop="licensePlateType"  min-width="160px" >
        <template slot-scope="scope">
          <dict-tag :options="dict.type.tob_licenseplatetype" :value="scope.row.licensePlateType"/>
        </template>
      </el-table-column>
      <el-table-column label="车型" align="center" prop="carTypeId" :formatter="formatCarTypeName">
      </el-table-column>
      <el-table-column label="车身颜色" align="center" prop="carColor" />
      <el-table-column label="出现次数" align="center" prop="occurrenceNumber" />
      <el-table-column label="涉案数" align="center" prop="casesInvolved" />
      <el-table-column label="车主id" align="center" prop="carOwnerId" />
      <el-table-column label="车主姓名" align="center" prop="name" />
      <el-table-column label="车辆购置日期" align="center" prop="vehiclePurchaseDate" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.vehiclePurchaseDate, '{y}-{m}-{d}') }}</span>
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
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width"  min-width="120px">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['vehicle:vehicle:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['vehicle:vehicle:remove']"
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

    <!-- 添加或修改车辆对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="车牌编号" prop="licensePlate">
          <el-input v-model="form.licensePlate" placeholder="请输入车牌" />
        </el-form-item>
        <el-form-item label="车牌类型" prop="licensePlateType">
          <el-select v-model="form.licensePlateType" placeholder="请选择车牌类型" clearable>
            <el-option
              v-for="licenseplatetype in dict.type.tob_licenseplatetype"
              :key="licenseplatetype.value"
              :label="licenseplatetype.label"
              :value="licenseplatetype.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="车型" prop="carTypeId">
          <el-select v-model="form.carTypeId" placeholder="请选择车型" clearable>
            <el-option
              v-for="carType in carTypeList"
              :key="carType.carTypeId"
              :label="carType.carTypeName"
              :value="carType.carTypeId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="车身颜色" prop="carColor">
          <el-input v-model="form.carColor" placeholder="请输入车身颜色" />
        </el-form-item>

        <el-form-item label="搜索"  >
                 <el-input v-model="searchInput" @input="filterUsers" placeholder="请输入驾驶员名称、ID 或电话号码" clearable suffix-icon="el-icon-search"></el-input>
                 <el-scrollbar wrap-class="scrollbar-wrapper" style="max-height: 'auto';">
                 <el-card class="user-list">
                       <el-row v-for="(user, index) in filteredUsers" :key="index" class="user-info" :class="{ 'bg-color': index % 2 === 1,'selected': user === selectedUser }">
                         <el-col :span="24">
                             <span @click="selectUser(user)" class="label" style="cursor:pointer;">用户ID:{{ user.id }}&nbsp;&nbsp;用户名称:{{ user.username }}&nbsp;&nbsp;电话号码:{{ user.phonenumber }}  </span>
                         </el-col>
                       </el-row>
                     </el-card>
                 </el-scrollbar>
        </el-form-item>
        <el-form-item label="车主id" prop="carOwnerId">
          <el-input :disabled="true" v-model="form.carOwnerId" placeholder="请输入车主id" />
        </el-form-item>
        <el-form-item label="车主姓名" prop="name">
          <el-input :disabled="true" v-model="form.name" placeholder="请输入车主姓名" />
        </el-form-item>
        <el-form-item label="车主电话号码" prop="phone">
          <el-input :disabled="true" v-model="form.phone" placeholder="请输入车主电话号码" />
        </el-form-item>
        <el-form-item label="车辆购置日期" prop="vehiclePurchaseDate">
          <el-date-picker clearable
            v-model="form.vehiclePurchaseDate"
            type="date"
            value-format="yyyy-MM-dd"
            placeholder="请选择车辆购置日期">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="违法状态" prop="illegalStatus">
          <el-select v-model="form.illegalStatus" placeholder="请选择违法状态" clearable>
            <el-option
            v-for="dict in dict.type.tob_illegal_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态" clearable>
            <el-option
            v-for="dict in dict.type.sys_normal_disable"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
            />
          </el-select>
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
import { listVehicle, getVehicle, delVehicle, addVehicle, updateVehicle } from "@/api/cigarette/vehicle/vehicle";
import { listCarType } from "@/api/cigarette/vehicle/carType";
import { listUser, getUser} from "@/api/system/user";
export default {
  name: "Vehicle",
  dicts: ['tob_illegal_status', 'sys_normal_disable','tob_licenseplatetype'],
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
      // 车辆表格数据
      vehicleList: [],
      carTypeList:[],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        licensePlate: null,
        licensePlateType: null,
        carTypeId: null,
        carColor: null,
        occurrenceNumber: null,
        casesInvolved: null,
        carOwnerId: null,
        vehiclePurchaseDate: null,
        illegalStatus: null,
        status: null,
      },
      // 表单参数
      form: {},
       // 存储搜索信息
      searchInput:'',
      // 存储用户信息
      userIdList: [],
      // 存储根据搜索条件过滤后的用户列表数据
      filteredUsers: [],
      // 存储所选用户信息
      selectedUser: null,
      // 表单校验
      rules: {
        licensePlate: [
          { required: true, message: "车牌不能为空", trigger: "blur" }
        ],
        licensePlateType: [
          { required: true, message: "车牌类型不能为空", trigger: "change" }
        ],
        carTypeId: [
          { required: true, message: "车型不能为空", trigger: "blur" }
        ],
        carColor: [
          { required: true, message: "车身颜色不能为空", trigger: "blur" }
        ],
        carOwnerId: [
          { required: true, message: "车主id不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询车辆列表 */
    getList() {
      this.loading = true;
      listVehicle(this.queryParams).then(response => {
        this.vehicleList = response.rows;
        this.total = response.total;
      });
      listCarType(this.queryParams).then(response2 => {
        this.carTypeList = response2.rows;
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
        carId: null,
        licensePlate: null,
        licensePlateType: null,
        carTypeId: null,
        carColor: null,
        occurrenceNumber: null,
        casesInvolved: null,
        carOwnerId: null,
        vehiclePurchaseDate: null,
        illegalStatus: null,
        status: null,
        delFlag: null,
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
      this.ids = selection.map(item => item.carId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      listUser({ pageNum: null, pageSize: 100000 }).then(response => {
        // 获取到用户信息后，保存原始用户列表数据
        this.userIdList = response.rows.map(user => {
          return {
            id: user.userId,
            username: user.nickName,
            phonenumber: user.phonenumber
          };
        });
      }).catch(error => {
        console.error('Failed to fetch user list:', error);
      });
      this.open = true;
      this.title = "添加车辆";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const carId = row.carId || this.ids
      getVehicle(carId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改车辆";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.carId != null) {
            updateVehicle(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVehicle(this.form).then(response => {
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
      const carIds = row.carId || this.ids;
      this.$modal.confirm('是否确认删除车辆编号为"' + carIds + '"的数据项？').then(function() {
        return delVehicle(carIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cigarette/vehicle/vehicle/export', {
        ...this.queryParams
      }, `vehicle_${new Date().getTime()}.xlsx`)
    },
    // 将carTypeId转化显示为carTypeName
    formatCarTypeName(row) {
      // 根据 carTypeId 找到对应的 carTypeName
      const carType = this.getCarTypeByCarTypeId(row.carTypeId);
      return carType ? carType.carTypeName : '';
    },
    // 根据 carTypeId 获取 carType 对象
    getCarTypeByCarTypeId(carTypeId) {
      // 这里假设 carTypeList 是一个包含所有车型信息的数组
      // 您需要确保 carTypeList 已经在 data 中定义，并且包含 carTypeId 和 carTypeName
      return this.carTypeList.find(carType => carType.carTypeId === carTypeId);
    },
    // 选择数据化进行数据填充
    filterUsers() {
        const searchInput = this.searchInput.toLowerCase().trim();
        if (!searchInput) {
            // 如果搜索条件为空，不显示任何用户
            this.filteredUsers = [];
            return;
        }
        this.filteredUsers = this.userIdList.filter(user => {
            // 在用户名、ID和电话号码中进行搜索匹配
            return (
                user.username.toLowerCase().includes(searchInput) ||
                user.id.toString().includes(searchInput) ||
                user.phonenumber.toString().includes(searchInput)
            );
        }) .slice(0, 10);
    },
    // 搜索用户并筛选数据
    selectUser(user) {
        // 将所选用户信息存储到 selectedUser 变量中
        this.selectedUser = user;
        // 更新表单数据
        this.$set(this.form, "carOwnerId", user.id);
        this.$set(this.form, "name", user.username);
        this.$set(this.form, "phone", user.phonenumber);
    },
  }
};
</script>

<style>
  .bg-color {
    background-color: #f0f0f0;
  }
  .selected {
    background-color: #d0e8f2; /* 天蓝色背景 */
  }
</style>
