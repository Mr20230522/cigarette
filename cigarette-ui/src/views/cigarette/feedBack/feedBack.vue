<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <!--<el-form-item label="反馈人ID" prop="creatorId">-->
      <!--  <el-input-->
      <!--    v-model="queryParams.creatorId"-->
      <!--    placeholder="请输入反馈人ID"-->
      <!--    clearable-->
      <!--    @keyup.enter.native="handleQuery"-->
      <!--  />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="反馈人姓名" prop="creatorNickName">-->
      <!--  <el-input-->
      <!--    v-model="queryParams.creatorNickName"-->
      <!--    placeholder="请输入反馈人姓名"-->
      <!--    clearable-->
      <!--    @keyup.enter.native="handleQuery"-->
      <!--  />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="反馈人账号" prop="creatorUserName">-->
      <!--  <el-input-->
      <!--    v-model="queryParams.creatorUserName"-->
      <!--    placeholder="请输入反馈人账号"-->
      <!--    clearable-->
      <!--    @keyup.enter.native="handleQuery"-->
      <!--  />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="反馈人手机号码" prop="creatorPhoneNumber">-->
      <!--  <el-input-->
      <!--    v-model="queryParams.creatorPhoneNumber"-->
      <!--    placeholder="请输入反馈人手机号码"-->
      <!--    clearable-->
      <!--    @keyup.enter.native="handleQuery"-->
      <!--  />-->
      <!--</el-form-item>-->
      <el-form-item label="反馈类型" prop="questionType">
        <el-select v-model="queryParams.questionType" placeholder="请选择反馈问题类型" clearable>
          <el-option
            v-for="dict in dict.type.question_type"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="所在页面" prop="inPage">
        <el-input
          v-model="queryParams.inPage"
          placeholder="请输入反馈所在页面"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="优先级" prop="priority">
        <el-select v-model="queryParams.priority" placeholder="请选择问题优先级" clearable>
          <el-option
            v-for="dict in dict.type.question_priority"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.question_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <!--<el-form-item label="解决人ID" prop="solverId">-->
      <!--  <el-input-->
      <!--    v-model="queryParams.solverId"-->
      <!--    placeholder="请输入解决人ID"-->
      <!--    clearable-->
      <!--    @keyup.enter.native="handleQuery"-->
      <!--  />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="解决人姓名" prop="solverNickName">-->
      <!--  <el-input-->
      <!--    v-model="queryParams.solverNickName"-->
      <!--    placeholder="请输入解决人姓名"-->
      <!--    clearable-->
      <!--    @keyup.enter.native="handleQuery"-->
      <!--  />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="解决人账号" prop="solverUserName">-->
      <!--  <el-input-->
      <!--    v-model="queryParams.solverUserName"-->
      <!--    placeholder="请输入解决人账号"-->
      <!--    clearable-->
      <!--    @keyup.enter.native="handleQuery"-->
      <!--  />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="解决人手机号码" prop="solverPhoneNumber">-->
      <!--  <el-input-->
      <!--    v-model="queryParams.solverPhoneNumber"-->
      <!--    placeholder="请输入解决人手机号码"-->
      <!--    clearable-->
      <!--    @keyup.enter.native="handleQuery"-->
      <!--  />-->
      <!--</el-form-item>-->
      <!--<el-form-item label="反馈时间" prop="createTime">-->
      <!--  <el-date-picker clearable-->
      <!--                  v-model="queryParams.createTime"-->
      <!--                  type="date"-->
      <!--                  value-format="yyyy-MM-dd"-->
      <!--                  placeholder="请选择反馈时间">-->
      <!--  </el-date-picker>-->
      <!--</el-form-item>-->
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
          v-hasPermi="['system:feedback:add']"
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
          v-hasPermi="['system:feedback:edit']"
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
          v-hasPermi="['system:feedback:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:feedback:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="feedbackList" @selection-change="handleSelectionChange">
      <el-table-column fixed="left" type="selection" align="center" />
      <el-table-column fixed="left" label="意见ID" align="center" prop="id" />
      <el-table-column fixed="left" min-width="100" label="反馈时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.solveTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="left" label="反馈类型" align="center" prop="questionType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.question_type" :value="scope.row.questionType"/>
        </template>
      </el-table-column>
      <el-table-column fixed="left" label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.question_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <!--(1: 功能异常, 2: 安全问题, 3: 体验问题, 4: 功能建议, 5: 其它)-->
      <el-table-column fixed="left" width="250" label="详情" align="center" prop="questionContent" />
      <el-table-column label="所在页面" align="center" prop="inPage" />
      <el-table-column label="截图" align="center" prop="filePaths" />
      <el-table-column label="优先级" align="center" prop="priority">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.question_priority" :value="scope.row.priority"/>
        </template>
      </el-table-column>
      <el-table-column label="反馈人ID" align="center" prop="creatorId" />
      <el-table-column label="反馈人姓名" align="center" prop="creatorNickName" />
      <el-table-column label="反馈人账号" align="center" prop="creatorUserName" />
      <el-table-column label="反馈人手机号码" align="center" prop="creatorPhoneNumber" />
      <el-table-column label="解决人ID" align="center" prop="solverId" />
      <el-table-column label="解决人姓名" align="center" prop="solverNickName" />
      <el-table-column label="解决人账号" align="center" prop="solverUserName" />
      <el-table-column label="解决人手机号码" align="center" prop="solverPhoneNumber" />
      <el-table-column label="解决反馈时间" align="center" prop="solveTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.solveTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="查看详情" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:feedback:edit']"
          >查看</el-button>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:feedback:edit']"
          >删除</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:feedback:remove']"
          >已解决</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:feedback:remove']"
          >不予解决</el-button>
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

    <!-- 添加或修改问题意见反馈对话框 -->
    <!-- 添加或修改问题意见反馈对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="80%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="反馈人ID" prop="creatorId">
          <el-input v-model="form.creatorId" placeholder="请输入反馈人ID" />
        </el-form-item>
        <el-form-item label="反馈人姓名" prop="creatorNickName">
          <el-input v-model="form.creatorNickName" placeholder="请输入反馈人姓名" />
        </el-form-item>
        <el-form-item label="反馈人账号" prop="creatorUserName">
          <el-input v-model="form.creatorUserName" placeholder="请输入反馈人账号" />
        </el-form-item>
        <el-form-item label="反馈人手机号码" prop="creatorPhoneNumber">
          <el-input v-model="form.creatorPhoneNumber" placeholder="请输入反馈人手机号码" />
        </el-form-item>
        <el-form-item label="反馈类型" prop="questionType">
          <el-select v-model="form.questionType" placeholder="请选择反馈类型">
            <el-option
              v-for="dict in dict.type.question_type"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="反馈问题/建议详情">
          <editor v-model="form.questionContent" :min-height="192"/>
        </el-form-item>
        <el-form-item label="反馈问题/建议所在页面" prop="inPage">
          <el-input v-model="form.inPage" placeholder="请输入反馈问题/建议所在页面" />
        </el-form-item>
        <el-form-item label="附件截图保存地址" prop="filePaths">
          <el-input v-model="form.filePaths" type="textarea" placeholder="请输入内容" />
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-select v-model="form.priority" placeholder="请选择优先级">
            <el-option
              v-for="dict in dict.type.question_priority"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option
              v-for="dict in dict.type.question_status"
              :key="dict.value"
              :label="dict.label"
              :value="parseInt(dict.value)"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="解决人ID" prop="solverId">
          <el-input v-model="form.solverId" placeholder="请输入解决人ID" />
        </el-form-item>
        <el-form-item label="解决人姓名" prop="solverNickName">
          <el-input v-model="form.solverNickName" placeholder="请输入解决人姓名" />
        </el-form-item>
        <el-form-item label="解决人账号" prop="solverUserName">
          <el-input v-model="form.solverUserName" placeholder="请输入解决人账号" />
        </el-form-item>
        <el-form-item label="解决人手机号码" prop="solverPhoneNumber">
          <el-input v-model="form.solverPhoneNumber" placeholder="请输入解决人手机号码" />
        </el-form-item>
        <el-form-item label="解决反馈时间" prop="solveTime">
          <el-date-picker clearable
                          v-model="form.solveTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择解决反馈时间">
          </el-date-picker>
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
import { listFeedback, getFeedback, delFeedback, addFeedback, updateFeedback } from "@/api/cigarette/feedback/feedback";
import {addData} from "@/api/system/dict/data";


export default {
  name: "Feedback",
  dicts: ['question_type', 'question_priority', 'question_status'],
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
      // 问题意见反馈表格数据
      feedbackList: [
        {
          id: 1,
          createTime: '2024-09-01 10:00:00',
          solveTime: '2024-09-05 12:00:00',
          questionType: 1,
          questionContent: '功能在特定条件下崩溃',
          inPage: '主页',
          filePaths: ['screenshot1.png'],
          priority: 3,
          status: 3,
          creatorId: 101,
          creatorNickName: '张三',
          creatorUserName: 'zhangsan',
          creatorPhoneNumber: '13800000001',
          solverId: 201,
          solverNickName: '李四',
          solverUserName: 'lisi',
          solverPhoneNumber: '13900000002'
        },
        {
          id: 2,
          createTime: '2024-09-02 11:00:00',
          solveTime: '2024-09-06 14:00:00',
          questionType: 2,
          questionContent: '安全漏洞需要修复',
          inPage: '设置页',
          filePaths: [],
          priority: 2,
          status: 1,
          creatorId: 102,
          creatorNickName: '王五',
          creatorUserName: 'wangwu',
          creatorPhoneNumber: '13700000003',
          solverId: 202,
          solverNickName: '赵六',
          solverUserName: 'zhaoliu',
          solverPhoneNumber: '13600000004'
        },
        {
          id: 3,
          createTime: '2024-09-03 09:30:00',
          solveTime: '2024-09-07 15:00:00',
          questionType: 3,
          questionContent: '用户体验不佳',
          inPage: '用户中心',
          filePaths: ['screenshot2.png'],
          priority: 1,
          status: 4,
          creatorId: 103,
          creatorNickName: '小明',
          creatorUserName: 'xiaoming',
          creatorPhoneNumber: '13500000005',
          solverId: 203,
          solverNickName: '小红',
          solverUserName: 'xiaohong',
          solverPhoneNumber: '13400000006'
        }
      ],

      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        creatorId: null,
        creatorNickName: null,
        creatorUserName: null,
        creatorPhoneNumber: null,
        questionType: null,
        questionContent: null,
        inPage: null,
        filePaths: null,
        priority: null,
        status: null,
        solverId: null,
        solverNickName: null,
        solverUserName: null,
        solverPhoneNumber: null,
        solveTime: null,
        createTime:null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        creatorId: [
          { required: true, message: "反馈人ID不能为空", trigger: "blur" }
        ],
        creatorUserName: [
          { required: true, message: "反馈人账号不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "反馈时间不能为空", trigger: "blur" }
        ],
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    addData,
    /** 查询问题意见反馈列表 */
    getList() {
      this.loading = true;
      listFeedback(this.queryParams).then(response => {
        // this.feedbackList = response.rows;
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
        id: null,
        creatorId: null,
        creatorNickName: null,
        creatorUserName: null,
        creatorPhoneNumber: null,
        createTime: null,
        questionType: null,
        questionContent: null,
        inPage: null,
        filePaths: null,
        priority: null,
        status: null,
        solverId: null,
        solverNickName: null,
        solverUserName: null,
        solverPhoneNumber: null,
        solveTime: null
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加问题意见反馈";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getFeedback(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改问题意见反馈";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateFeedback(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFeedback(this.form).then(response => {
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
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认删除问题意见反馈编号为"' + ids + '"的数据项？').then(function() {
        return delFeedback(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/feedback/export', {
        ...this.queryParams
      }, `feedback_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
