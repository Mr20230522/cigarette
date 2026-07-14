<template>
    <div class="app-container">
        <h1>短信设备管理</h1>
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
            label-width="68px">
            <el-form-item label="卡口信息" prop="identityCard">
                <el-input v-model="queryParams.identityCard" placeholder="请输入卡口信息" clearable
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
                <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
                    @click="handleDelete" v-hasPermi="['personnel:person:remove']">删除</el-button>
            </el-col>
            <el-col :span="1.5">
                <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                    v-hasPermi="['personnel:person:export']">导出</el-button>
            </el-col>
            <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="detectionList" @selection-change="handleSelectionChange">
            <el-table-column label="卡口编号" align="center" prop="detectionId" />
            <el-table-column label="卡口名称" align="center" prop="detectionName" min-width="120px"
                :show-overflow-tooltip="true" />
            <!-- <el-table-column label="卡口位置" align="center" prop="phone" min-width="120px" :show-overflow-tooltip="true" /> -->

            <el-table-column fixed="right" label="操作" align="center" class-name="small-padding fixed-width"
                min-width="120px">
                <template slot-scope="scope">
                    <el-button size="mini" type="text" icon="el-icon-edit" @click="handleManage(scope.row)"
                        v-hasPermi="['personnel:person:edit']">查看卡口设备</el-button>
                </template>
            </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
            :limit.sync="queryParams.pageSize" @pagination="getList" />

        <!-- 查看短信设备信息 -->
        <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
            <el-table v-loading="loading" :data="personList" @selection-change="handleSelectionChange">
                <el-table-column label="设备号码" align="center" prop="suspectId" />
                <el-table-column label="设备是否在线" align="center" prop="phone" min-width="120px"
                    :show-overflow-tooltip="true" />
                <el-table-column fixed="right" label="操作" align="center" class-name="small-padding fixed-width"
                    min-width="120px">
                    <template slot-scope="scope">
                        <el-button size="mini" type="text" icon="el-icon-edit" @click="handleManage(scope.row)"
                            v-hasPermi="['personnel:person:edit']">测试信息发送功能</el-button>
                    </template>
                </el-table-column>
            </el-table>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { listDetection, getDetection, delDetection, addDetection, updateDetection, listDetectionByDistrictId } from "@/api/cigarette/detection/detection";

export default {
    name: "Person",
    // dicts: ['tob_illegal_status', 'sys_show_hide', 'sys_normal_disable', 'sys_user_sex'],
    data() {
        return {
            // 遮罩层
            loading: true,
            // 选中数组
            ids: [],
            // 非单个禁用
            single: true,
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
                casesInvolved: null,
                picture: null
            },
            // 表单参数
            form: {},
            // 表单校验
            rules: {
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
            },
            // 监测区域表格数据
            detectionList: [],
        };
    },
    created() {
        this.getList();
    },
    methods: {
        /** 查询监测区域列表 */
        getList() {
            this.loading = true;
            listDetection(this.queryParams).then(response => {
                this.detectionList = response.rows;
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
                casesInvolved: null,
                picture: null
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
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加驾驶人员";
        },
        /** 按钮操作 */
        handleManage(row) {
            this.reset();
            const suspectId = row.suspectId || this.ids
            getPerson(suspectId).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "查看短信设备信息";
            });
        },
        /** 提交按钮 */
        submitForm() {
        },
        /** 删除按钮操作 */
        handleDelete(row) {
        },
    }
};
</script>
