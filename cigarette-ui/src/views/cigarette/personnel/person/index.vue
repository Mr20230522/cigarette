<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="身份证ID" prop="identityCard">
        <el-input v-model="queryParams.identityCard" placeholder="请输入身份证ID" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="电话号码" prop="phone">
        <el-input v-model="queryParams.phone" placeholder="请输入电话号码" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="驾驶证号码" prop="suspectLicenseNumber">
        <el-input v-model="queryParams.suspectLicenseNumber" placeholder="请输入驾驶证号码" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="姓名" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入姓名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="年龄" prop="age">
        <el-input v-model="queryParams.age" placeholder="请输入年龄" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-input v-model="queryParams.gender" placeholder="请输入性别" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="出现次数" prop="occurrenceNumber">
        <el-input v-model="queryParams.occurrenceNumber" placeholder="请输入出现次数" clearable
          @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="违法状态" prop="illegalStatus">
        <el-select v-model="queryParams.illegalStatus" placeholder="请选择违法状态" clearable>
          <el-option v-for="dict in dict.type.tob_illegal_status" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="dict in dict.type.sys_show_hide" :key="dict.value" :label="dict.label"
            :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="涉案数" prop="casesInvolved">
        <el-input v-model="queryParams.casesInvolved" placeholder="请输入涉案数" clearable
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
          v-hasPermi="['personnel:person:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['personnel:person:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['personnel:person:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['personnel:person:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="personList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="驾驶人ID" align="center" prop="suspectId" />
      <el-table-column label="身份证ID" align="center" prop="identityCard" min-width="170px" />
      <el-table-column label="人脸特征编码" align="center" prop="faceFeature" min-width="120px">
        <template slot-scope="scope">
          <!-- 使用作用域数据 scope.row 来访问行数据 -->
          <el-button v-if="scope.row.faceFeature === null" type="warning" @click="handleNotRecorded(scope.row)">
            尚未录入
          </el-button>
          <el-button v-else type="success" @click="handleRecorded(scope.row)">
            已录入
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="电话号码" align="center" prop="phone" min-width="120px" />
      <el-table-column label="驾驶证号码" align="center" prop="suspectLicenseNumber" min-width="170px" />
      <el-table-column label="姓名" align="center" prop="name" min-width="120px" />
      <el-table-column label="年龄" align="center" prop="age" />
      <el-table-column label="性别" align="center" prop="gender">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_user_sex" :value="scope.row.gender" />
        </template>
      </el-table-column>
      <el-table-column label="地址" align="center" prop="address" min-width="140px" />
      <el-table-column label="出现次数" align="center" prop="occurrenceNumber" />
      <el-table-column label="违法状态" align="center" prop="illegalStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.tob_illegal_status" :value="scope.row.illegalStatus" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_show_hide" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="涉案数" align="center" prop="casesInvolved" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="120px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['personnel:person:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['personnel:person:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />

    <!-- 添加或修改驾驶人员对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="姓名" prop="name">
              <el-input v-model="form.name" placeholder="请输入姓名" />
            </el-form-item>


          </el-col>
          <el-col :span="12">
            <el-form-item label="电话号码" prop="phone">
              <el-input v-model="form.phone" placeholder="请输入电话号码" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="身份证ID" prop="identityCard">
              <el-input v-model="form.identityCard" placeholder="请输入身份证ID" />
            </el-form-item>

          </el-col>
          <el-col :span="12">
            <el-form-item label="驾驶证ID" prop="suspectLicenseNumber">
              <el-input v-model="form.suspectLicenseNumber" placeholder="请输入驾驶证号码" />
            </el-form-item>

          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">

            <el-form-item label="性别" prop="gender">
              <el-select v-model="form.gender" placeholder="请选择状态" clearable>
                <el-option v-for="dict in dict.type.sys_user_sex" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="年龄" prop="age">
              <el-input v-model="form.age" placeholder="请输入年龄" />
            </el-form-item>

          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">

            <el-form-item label="出现次数" prop="occurrenceNumber">
              <el-input v-model="form.occurrenceNumber" placeholder="请输入出现次数" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="涉案数" prop="casesInvolved">
              <el-input v-model="form.casesInvolved" placeholder="请输入涉案数" />
            </el-form-item>

          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">

            <el-form-item label="违法状态" prop="illegalStatus">
              <el-select v-model="form.illegalStatus" placeholder="请选择违法状态" clearable>
                <el-option v-for="dict in dict.type.tob_illegal_status" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择状态" clearable>
                <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>

          </el-col>
        </el-row>

        <el-form-item label="人脸特征编码" prop="faceFeature">
          <el-input v-model="form.faceFeature" placeholder="请输入人脸特征编码" />
        </el-form-item>


        <el-form-item label="地址" prop="address">
          <el-input v-model="form.address" placeholder="请输入地址" />
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
  import {
    listPerson,
    getPerson,
    delPerson,
    addPerson,
    updatePerson
  } from "@/api/cigarette/personnel/person";

  export default {
    name: "Person",
    dicts: ['tob_illegal_status', 'sys_show_hide', 'sys_normal_disable', 'sys_user_sex'],
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
        // 驾驶人员表格数据
        personList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          identityCard: null,
          faceFeature: null,
          phone: null,
          suspectLicenseNumber: null,
          name: null,
          age: null,
          gender: null,
          address: null,
          occurrenceNumber: null,
          illegalStatus: null,
          status: null,
          casesInvolved: null
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {
          identityCard: [{
            required: true,
            message: "身份证ID不能为空",
            trigger: "blur"
          }],
          name: [{
            required: true,
            message: "姓名不能为空",
            trigger: "blur"
          }],
          status: [{
            required: true,
            message: "状态不能为空",
            trigger: "change"
          }],
          delFlag: [{
            required: true,
            message: "删除标记不能为空",
            trigger: "blur"
          }],
          createTime: [{
            required: true,
            message: "创建时间不能为空",
            trigger: "blur"
          }],
          updateTime: [{
            required: true,
            message: "更新时间不能为空",
            trigger: "blur"
          }],
        }
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询驾驶人员列表 */
      getList() {
        this.loading = true;
        listPerson(this.queryParams).then(response => {
          this.personList = response.rows;
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
          suspectId: null,
          identityCard: null,
          faceFeature: null,
          phone: null,
          suspectLicenseNumber: null,
          name: null,
          age: null,
          gender: null,
          address: null,
          occurrenceNumber: null,
          illegalStatus: null,
          status: null,
          delFlag: null,
          remark: null,
          createTime: null,
          updateTime: null,
          casesInvolved: null
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
        this.ids = selection.map(item => item.suspectId)
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.reset();
        this.open = true;
        this.title = "添加驾驶人员";
      },
      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const suspectId = row.suspectId || this.ids
        getPerson(suspectId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改驾驶人员";
        });
      },
      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.suspectId != null) {
              updatePerson(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addPerson(this.form).then(response => {
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
        const suspectIds = row.suspectId || this.ids;
        this.$modal.confirm('是否确认删除驾驶人员编号为"' + suspectIds + '"的数据项？').then(function() {
          return delPerson(suspectIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {});
      },
      /** 导出按钮操作 */
      handleExport() {
        this.download('cigarette/personnel/person/export', {
          ...this.queryParams
        }, `person_${new Date().getTime()}.xlsx`)
      }
    }
  };
</script>
