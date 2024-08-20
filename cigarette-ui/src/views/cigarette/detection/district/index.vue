<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="地区名称" prop="districtName">
        <el-input v-model="queryParams.districtName" placeholder="请输入地区名称" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="负责人" prop="leader">
        <el-input v-model="queryParams.leader" placeholder="请输入负责人" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="联系电话" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入联系电话" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="dict in dict.type.tob_dd_status" :key="dict.value" :label="dict.label"
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
          v-hasPermi="['cigarette:district:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="info" plain icon="el-icon-sort" size="mini" @click="toggleExpandAll">展开/折叠</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-if="refreshTable" v-loading="loading" :data="districtList" row-key="districtId"
      :default-expand-all="isExpandAll" :tree-props="{ children: 'children', hasChildren: 'hasChildren' }">
      <el-table-column label="地区名称" align="center" prop="districtName" />
      <el-table-column label="显示顺序" align="center" prop="orderNum" />
      <el-table-column label="负责人" align="center" prop="leader" />
      <el-table-column label="联系电话" align="center" prop="phone" />
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.tob_dd_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['cigarette:district:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-plus" @click="handleAdd(scope.row)"
            v-hasPermi="['cigarette:district:add']">新增</el-button>
          <el-button v-if="scope.row.parentId !== 0" size="mini" type="text" icon="el-icon-delete"
            @click="handleDelete(scope.row)" v-hasPermi="['cigarette:district:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 添加或修改地区管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="地区名称" prop="districtName">
          <el-input v-model="form.districtName" placeholder="请输入地区名称" />
        </el-form-item>
        <el-form-item label="搜索">
          <el-input v-model="searchInput" @input="filterUsers" placeholder="请输入用户名称、邮箱或电话号码" clearable
            suffix-icon="el-icon-search"></el-input>
          <el-scrollbar wrap-class="scrollbar-wrapper" style="max-height: auto;">
            <el-card class="user-list">
              <el-row v-for="(user, index) in filteredUsers" :key="index" class="user-info"
                :class="{ 'bg-color': index % 2 === 1, 'selected': user === selectedUser }">
                <el-col :span="24">
                  <span @click="selectUser(user)" class="label" style="cursor:pointer;">用户名称:{{ user.username
                    }}&nbsp;&nbsp;电话号码:{{ user.phonenumber }}&nbsp;&nbsp;邮箱:{{ user.email }}
                  </span>
                </el-col>
              </el-row>
            </el-card>
          </el-scrollbar>
        </el-form-item>

        <el-form-item label="负责人" prop="leader">
          <el-input :disabled="true" v-model="form.leader" placeholder="请在上方搜索人员" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input :disabled="true" v-model="form.phone" placeholder="请在上方搜索人员" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input :disabled="true" v-model="form.email" placeholder="请在上方搜索人员" />
        </el-form-item>
        <el-form-item label="显示顺序" prop="orderNum">
          <el-input-number v-model="form.orderNum" controls-position="right" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status">
            <el-option v-for="dict in dict.type.tob_dd_status" :key="dict.value" :label="dict.label"
              :value="dict.value">
            </el-option>
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
import { listDistrict, getDistrict, delDistrict, addDistrict, updateDistrict } from "@/api/cigarette/detection/district";
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import {
  listUser,
} from "@/api/system/user";

export default {
  name: "District",
  dicts: ['tob_del_flag', 'tob_dd_status'],
  components: {
    Treeselect
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 显示搜索条件
      showSearch: true,
      // 地区管理表格数据
      districtList: [],
      // 地区管理树选项
      districtOptions: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否展开，默认全部展开
      isExpandAll: true,
      // 重新渲染表格状态
      refreshTable: true,
      searchInput: '',
      userIdList: [],
      filteredUsers: [],
      selectedUser: null,
      // 查询参数
      queryParams: {
        districtId: null,
        districtName: null,
        leader: null,
        phone: null,
        email: null,
        orderNum: null,
        status: null,
        remark: null,
      },
      // 表单参数
      form: {
        // 搜索人
        // 存储搜索信息
        searchInput: '',
        // 存储用户信息
        userIdList: [],
        // 存储根据搜索条件过滤后的用户列表数据
        filteredUsers: [],
        // 存储所选用户信息
        selectedUser: null,
      },
      // 表单校验
      rules: {
        districtName: [
          { required: true, message: "地区名称不能为空", trigger: "blur" }
        ],
        orderNum: [
          { required: true, message: "显示顺序不能为空", trigger: "blur" }
        ],
        leader: [
          { required: true, message: "负责人不能为空", trigger: "blur" }
        ],
        phone: [
          { required: true, message: "联系电话不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
      }
    };
  },
  created() {
    this.getList();
    this.getUserList();
  },
  mounted() {
    this.filteredUsers = [];
  },
  methods: {
    /** 查询工作人员列表 */
    getUserList() {
      this.loading = true;
      listUser({
        pageNum: null,
        pageSize: 100000
      }).then(response => {
        // 获取到用户信息后，保存原始用户列表数据
        this.userIdList = response.rows.map(user => {
          return {
            username: user.nickName,
            phonenumber: user.phonenumber,
            email: user.email,
          };
        });
      }).catch(error => {
        console.error('Failed to fetch user list:', error);
      })
        .finally(() => {
          this.loading = false;
        });

      listStaff(this.queryParams).then(response => {
        this.staffList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    /** 查询地区管理列表 */
    getList() {
      this.loading = true;
      listDistrict(this.queryParams).then(response => {
        this.districtList = this.handleTree(response.data, "districtId", "parentId");
        this.loading = false;
      });
    },
    /** 转换地区管理数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.districtId,
        label: node.districtName,
        children: node.children
      };
    },
    /** 查询地区管理下拉树结构 */
    getTreeselect() {
      listDistrict().then(response => {
        this.districtOptions = [];
        const data = { districtId: 0, districtName: '顶级节点', children: [] };
        data.children = this.handleTree(response.data, "districtId", "parentId");
        this.districtOptions.push(data);
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
        districtId: null,
        parentId: null,
        ancestors: null,
        districtName: null,
        orderNum: null,
        leader: null,
        phone: null,
        email: null,
        status: null,
        createTime: null,
        updateTime: null,
        remark: null,
        userId: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.getList();
      this.getUserList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    /** 新增按钮操作 */
    handleAdd(row) {
      this.reset();
      this.getTreeselect();
      if (row != null && row.districtId) {
        this.form.parentId = row.districtId;
      } else {
        this.form.parentId = 0;
      }
      this.open = true;
      this.title = "添加地区管理";
    },
    /** 展开/折叠操作 */
    toggleExpandAll() {
      this.refreshTable = false;
      this.isExpandAll = !this.isExpandAll;
      this.$nextTick(() => {
        this.refreshTable = true;
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.getTreeselect();
      if (row != null) {
        this.form.parentId = row.parentId;
      }
      getDistrict(row.districtId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改地区管理";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.districtId != null) {
            updateDistrict(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDistrict(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除地区管理编号为"' + row.districtId + '"的数据项？').then(function () {
        return delDistrict(row.districtId);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
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
        // 在用户名 邮箱和电话号码中进行搜索匹配
        return (
          user.username.toLowerCase().includes(searchInput) ||
          user.email.toString().includes(searchInput) ||
          user.phonenumber.toString().includes(searchInput)
        );
      }).slice(0, 10);
    },
    // 搜索用户并筛选数据
    selectUser(user) {
      // 将所选用户信息存储到 selectedUser 变量中
      this.selectedUser = user;
      // 更新表单数据
      this.$set(this.form, "email", user.email);
      this.$set(this.form, "leader", user.username);
      this.$set(this.form, "phone", user.phonenumber);
      // 清空搜索输入框和搜索结果
      this.searchInput = '';
      this.filteredUsers = [];
    },
  }
};
</script>
<style>
.bg-color {
  background-color: #f0f0f0;
}

.selected {
  background-color: #d0e8f2;
  /* 天蓝色背景 */
}
</style>
