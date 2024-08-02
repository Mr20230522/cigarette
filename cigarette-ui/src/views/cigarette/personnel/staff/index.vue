<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="用户id" prop="userId">
        <el-input
          v-model="queryParams.userId"
          placeholder="请输入用户id"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['personnel:staff:add']"
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
          v-hasPermi="['personnel:staff:edit']"
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
          v-hasPermi="['personnel:staff:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['personnel:staff:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="staffList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="工作人员编号" align="center" prop="staffId" />
      <el-table-column label="用户id" align="center" prop="userId" />
      <el-table-column label="人脸特征编码" align="center" prop="faceFeature" >
         <template slot-scope="scope">
                <!-- 使用作用域数据 scope.row 来访问行数据 -->
                <el-button 
                  v-if="scope.row.faceFeature === null"
                  type="warning"
                  @click="handleNotRecorded(scope.row)">
                  尚未录入
                </el-button>
                <el-button 
                  v-else
                  type="success"
                  @click="handleRecorded(scope.row)">
                  已录入
                </el-button>
              </template>
              </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['personnel:staff:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['personnel:staff:remove']"
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

    <!-- 添加或修改工作人员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">

        <el-form-item label="搜索"  >
                 <el-input v-model="searchInput" @input="filterUsers" placeholder="请输入用户名称、ID 或电话号码" clearable suffix-icon="el-icon-search"></el-input>
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
        <el-form-item label="用户编号" prop="userId">
          <el-input :disabled="true"  v-model="form.userId" placeholder="请在上方搜索人员" />
        </el-form-item>
        <el-form-item label="用户名称" prop="userId">
          <el-input :disabled="true"  v-model="form.userName" placeholder="请在上方搜索人员" />
        </el-form-item>
        <el-form-item label="用户电话号码" prop="userId">
          <el-input :disabled="true"  v-model="form.phoneNumber" placeholder="请在上方搜索人员" />
        </el-form-item>
        <el-form-item label="人脸特征编码" prop="faceFeature">
          <el-input v-model="form.faceFeature" placeholder="请输入人脸特征编码" />
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
import { listStaff, getStaff, delStaff, addStaff, updateStaff } from "@/api/cigarette/personnel/staff";
import { listUser, getUser} from "@/api/system/user";

export default {
  name: "Staff",
  dicts: ['sys_normal_disable'],
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
      // 工作人员表格数据
      staffList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userId: null,
        faceFeature: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 搜索人
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
        userId: [
          { required: true, message: "用户id不能为空", trigger: "blur" }
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
        ]
      }
    };
  },
  created() {
    this.getList();
  },
  mounted() {
     this.filteredUsers = [];
  },
  methods: {
    /** 查询工作人员列表 */
    getList() {
      this.loading = true;
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
      listStaff(this.queryParams).then(response => {
        this.staffList = response.rows;
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
        staffId: null,
        userId: null,
        faceFeature: null,
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
      this.ids = selection.map(item => item.staffId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加工作人员";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const staffId = row.staffId || this.ids
      getStaff(staffId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改工作人员";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.staffId != null) {
            updateStaff(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addStaff(this.form).then(response => {
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
      const staffIds = row.staffId || this.ids;
      this.$modal.confirm('是否确认删除工作人员编号为"' + staffIds + '"的数据项？').then(function() {
        return delStaff(staffIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cigarette/personnel/staff/export', {
        ...this.queryParams
      }, `staff_${new Date().getTime()}.xlsx`)
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
        this.$set(this.form, "userId", user.id);
        this.$set(this.form, "userName", user.username);
        this.$set(this.form, "phoneNumber", user.phonenumber);
    },
              // 处理 "尚未录入" 按钮的点击事件
     handleNotRecorded(row) {
          // 可以在这里添加跳转逻辑
          console.log('跳转到录入人脸特征的页面', row);
        },
                  // 处理 "已录入" 按钮的点击事件
        handleRecorded(row) {
          // 可以在这里添加查看人脸特征的逻辑
          console.log('查看人脸特征', row);
        },
        // 其他方法...
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
