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
                    <el-form-item label="摄像头型号" prop="cameraModel">
                        <el-input v-model="queryParams.cameraModel" placeholder="请输入摄像头型号" clearable
                            @keyup.enter.native="handleQuery" />
                    </el-form-item>
                    <el-form-item label="摄像头厂商" prop="cameraManufacturer">
                        <el-input v-model="queryParams.cameraManufacturer" placeholder="请输入摄像头厂商" clearable
                            @keyup.enter.native="handleQuery" />
                    </el-form-item>
                    <el-form-item label="监测点名称" prop="detectionName">
                        <el-input v-model="queryParams.detectionName" placeholder="请输入监测点名称" clearable
                            @keyup.enter.native="handleQuery" />
                    </el-form-item>
                    <el-form-item label="安装日期" prop="installationDate">
                        <el-date-picker clearable v-model="queryParams.installationDate" type="date"
                            value-format="yyyy-MM-dd" placeholder="请选择安装日期">
                        </el-date-picker>
                    </el-form-item>
                    <el-form-item label="摄像头类型" prop="cameraType">
                        <el-select v-model="queryParams.cameraType" placeholder="请选择摄像头类型" clearable>
                            <el-option v-for="dict in dict.type.tob_camera_type" :key="dict.value" :label="dict.label"
                                :value="dict.value" />
                        </el-select>
                    </el-form-item>

                    <el-form-item label="摄像头组号" prop="cameraGroupIndication">
                        <el-input v-model="queryParams.cameraGroupIndication" placeholder="请输入摄像头组号" clearable
                            @keyup.enter.native="handleQuery" />
                    </el-form-item>
                    <el-form-item label="摄像头应用方向" prop="cameraApplicationType">
                        <el-select v-model="queryParams.cameraApplicationType" placeholder="请选择摄像头应用方向" clearable>
                            <el-option v-for="dict in dict.type.tob_driving_irection" :key="dict.value"
                                :label="dict.label" :value="dict.value" />
                        </el-select>
                    </el-form-item>
                    <el-form-item label="状态" prop="status">
                        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
                            <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value"
                                :label="dict.label" :value="dict.value" />
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
                            v-hasPermi="['camera:camera:add']">新增</el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single"
                            @click="handleUpdate" v-hasPermi="['camera:camera:edit']">修改</el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple"
                            @click="handleDelete" v-hasPermi="['camera:camera:remove']">删除</el-button>
                    </el-col>
                    <el-col :span="1.5">
                        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
                            v-hasPermi="['camera:camera:export']">导出</el-button>
                    </el-col>
                    <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
                </el-row>

                <el-table v-loading="loading" :data="cameraList" @selection-change="handleSelectionChange">
                    <el-table-column type="selection" width="55" align="center" />
                    <el-table-column label="摄像头id" align="center" prop="cameraId" />
                    <el-table-column label="摄像头ip" align="center" prop="cameraIp" />
                    <el-table-column label="摄像头型号" align="center" prop="cameraModel" />
                    <el-table-column label="摄像头厂商" align="center" prop="cameraManufacturer" />
                    <el-table-column label="监测点" align="center" prop="detectionId">
                        <template slot-scope="scope">
                            {{ getDetectionName(scope.row.detectionId) }}
                        </template>
                    </el-table-column>
                    <el-table-column label="分辨率" align="center" prop="resolutionRatio">
                        <template slot-scope="scope">
                            <dict-tag :options="dict.type.tob_resolution_ratio" :value="scope.row.resolutionRatio" />
                        </template>
                    </el-table-column>
                    <el-table-column label="帧率" align="center" prop="frameRate">
                        <template slot-scope="scope">
                            <dict-tag :options="dict.type.tob_frame_rate" :value="scope.row.frameRate" />
                        </template>
                    </el-table-column>
                    <el-table-column label="夜视能力" align="center" prop="nightVision">
                        <template slot-scope="scope">
                            <dict-tag :options="dict.type.tob_night_vision" :value="scope.row.nightVision" />
                        </template>
                    </el-table-column>
                    <el-table-column label="连接方式" align="center" prop="connectionType" />
                    <el-table-column label="安装日期" align="center" prop="installationDate" width="180">
                        <template slot-scope="scope">
                            <span>{{ parseTime(scope.row.installationDate, '{y}-{m}-{d}') }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="质保截止日期" align="center" prop="guaranteePeriod" width="180">
                        <template slot-scope="scope">
                            <span>{{ parseTime(scope.row.guaranteePeriod, '{y}-{m}-{d}') }}</span>
                        </template>
                    </el-table-column>
                    <el-table-column label="经度" align="center" prop="longitude" />
                    <el-table-column label="纬度" align="center" prop="latitude" />
                    <el-table-column label="摄像头类型" align="center" prop="cameraType">
                        <template slot-scope="scope">
                            <dict-tag :options="dict.type.tob_camera_type" :value="scope.row.cameraType" />
                        </template>
                    </el-table-column>
                    <el-table-column label="摄像头组号" align="center" prop="cameraGroupIndication" />
                    <el-table-column label="摄像头应用方向" align="center" prop="cameraApplicationType">
                        <template slot-scope="scope">
                            <dict-tag :options="dict.type.tob_camera_orientation"
                                :value="scope.row.cameraApplicationType" />
                        </template>
                    </el-table-column>
                    <el-table-column label="状态" align="center" prop="status">
                        <template slot-scope="scope">
                            <dict-tag :options="dict.type.sys_normal_disable" :value="scope.row.status" />
                        </template>
                    </el-table-column>
                    <el-table-column label="删除标记" align="center" prop="delFlag">
                        <template slot-scope="scope">
                            <dict-tag :options="dict.type.tob_del_flag" :value="scope.row.delFlag" />
                        </template>
                    </el-table-column>
                    <el-table-column label="备注" align="center" prop="remark" />
                    <el-table-column label="所属地区" align="center" prop="districtId">
                        <template slot-scope="scope">
                            <!-- 使用一个方法来通过districtId找到对应的districtName -->
                            {{ getDistrictName(scope.row.districtId) }}
                        </template>
                    </el-table-column>
                    <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                        <template slot-scope="scope">
                            <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                                v-hasPermi="['camera:camera:edit']">修改</el-button>
                            <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                                v-hasPermi="['camera:camera:remove']">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
                    :limit.sync="queryParams.pageSize" @pagination="getList" />
            </el-col>
        </el-row>
        <!-- 表单 -->
        <el-dialog :title="title" :visible.sync="open" width="800px" append-to-body>
            <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="摄像头ip" prop="cameraIp">
                            <el-input v-model="form.cameraIp" placeholder="请输入摄像头ip" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="摄像头型号" prop="cameraModel">
                            <el-input v-model="form.cameraModel" placeholder="请输入摄像头型号" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="摄像头厂商" prop="cameraManufacturer">
                            <el-input v-model="form.cameraManufacturer" placeholder="请输入摄像头厂商" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="检测点" prop="detectionId">
                            <el-select v-model="form.detectionId" placeholder="请选择所管理的监测点" filterable
                                @change="handleDetectionChange">
                                <el-option v-for="item in detectionOptions" :key="item.detectionId"
                                    :label="getDetectionName(item.detectionId)" :value="item.detectionId"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="分辨率" prop="resolutionRatio">
                            <el-select v-model="form.resolutionRatio" placeholder="请选择摄像头分辨率">
                                <el-option v-for="dict in dict.type.tob_resolution_ratio" :key="dict.value"
                                    :label="dict.label" :value="dict.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="帧率" prop="frameRate">
                            <el-select v-model="form.frameRate" placeholder="请选择摄像头帧率">
                                <el-option v-for="dict in dict.type.tob_frame_rate" :key="dict.value"
                                    :label="dict.label" :value="dict.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="夜视能力" prop="nightVision">
                            <el-select v-model="form.nightVision" placeholder="请选择夜视能力">
                                <el-option v-for="dict in dict.type.tob_night_vision" :key="dict.value"
                                    :label="dict.label" :value="dict.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="连接方式" prop="connectionType">
                            <el-input v-model="form.connectionType" placeholder="请输入连接方式" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="安装日期" prop="installationDate">
                            <el-date-picker clearable v-model="form.installationDate" type="date"
                                value-format="yyyy-MM-dd" placeholder="请选择安装日期">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="质保截止日期" prop="guaranteePeriod">
                            <el-date-picker clearable v-model="form.guaranteePeriod" type="date"
                                value-format="yyyy-MM-dd" placeholder="请选择质保截止日期">
                            </el-date-picker>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="7">
                        <el-form-item label="经度" prop="longitude">
                            <el-input v-model="form.longitude" placeholder="请输入经度" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="7">
                        <el-form-item label="纬度" prop="latitude">
                            <el-input v-model="form.latitude" placeholder="请输入纬度" />
                        </el-form-item>
                    </el-col>
                    <el-col :span="2">
                        <el-form-item>
                            <el-button type="primary" plain @click="getLocation">获取经纬度</el-button>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="摄像头类型" prop="cameraType">
                            <el-select v-model="form.cameraType" placeholder="请选择摄像头类型">
                                <el-option v-for="dict in dict.type.tob_camera_type" :key="dict.value"
                                    :label="dict.label" :value="dict.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="摄像头组号" prop="cameraGroupIndication">
                            <el-input v-model="form.cameraGroupIndication" placeholder="请输入摄像头组号" />
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="摄像头应用方向" prop="cameraApplicationType">
                            <el-select v-model="form.cameraApplicationType" placeholder="请选择摄像头应用方向">
                                <el-option v-for="dict in dict.type.tob_camera_orientation" :key="dict.value"
                                    :label="dict.label" :value="dict.value"></el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <el-form-item label="状态" prop="status">
                            <el-select v-model="form.status">
                                <el-option v-for="dict in dict.type.sys_normal_disable" :key="dict.value"
                                    :label="dict.label" :value="dict.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                </el-row>
                <el-row :gutter="20">
                    <el-col :span="12">
                        <el-form-item label="删除标记" prop="delFlag">
                            <el-select v-model="form.delFlag" placeholder="请选择删除标记">
                                <el-option v-for="dict in dict.type.tob_del_flag" :key="dict.value" :label="dict.label"
                                    :value="dict.value">
                                </el-option>
                            </el-select>
                        </el-form-item>
                    </el-col>
                    <el-col :span="12">
                        <!-- <el-form-item label="监测点所属地区ID" prop="districtId">
              <el-input v-model="form.districtId" :disabled="true" placeholder="选择监测点以填入所属地区ID" />
            </el-form-item> -->
                        <el-form-item label="备注" prop="remark">
                            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
                        </el-form-item>
                    </el-col>
                </el-row>

            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { listCamera, getCamera, delCamera, addCamera, updateCamera, listCameraByDistrictId } from "@/api/cigarette/detection/camera";
import { listDistrict } from "@/api/cigarette/detection/district";
import { listDetection } from "@/api/cigarette/detection/detection";

export default {
    name: "Camera",
    dicts: ['sys_normal_disable', 'tob_camera_orientation', 'tob_camera_type', 'tob_del_flag', 'tob_connection_type', 'tob_night_vision', 'tob_frame_rate', 'tob_resolution_ratio'],
    data() {
        return {
            districtOptions: [], // 用于存储地区选项的数组
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
            // 摄像头表格数据
            cameraList: [],
            // 弹出层标题
            title: "",
            // 是否显示弹出层
            open: false,
            districts: [],
            detections: [],
            treeData: [],
            defaultProps: {
                children: 'children',
                label: 'label'
            },
            loading: true,
            // 查询参数
            queryParams: {
                pageNum: 1,
                pageSize: 10,
                cameraIp: null,
                cameraModel: null,
                cameraManufacturer: null,
                detectionId: null,
                resolutionRatio: null,
                frameRate: null,
                nightVision: null,
                connectionType: null,
                installationDate: null,
                guaranteePeriod: null,
                longitude: null,
                latitude: null,
                cameraType: null,
                cameraGroupIndication: null,
                cameraApplicationType: null,
                status: null,
                detectionName: null,
                districtId: null,
                delFlag: null,
            },
            // 表单参数
            form: {
                longitude: '',
                latitude: ''
            },
            // 表单校验
            rules: {
                cameraIp: [
                    { required: true, message: "摄像头ip不能为空", trigger: "blur" }
                ],
                cameraModel: [
                    { required: true, message: "摄像头型号不能为空", trigger: "blur" }
                ],
                cameraManufacturer: [
                    { required: true, message: "摄像头厂商不能为空", trigger: "blur" }
                ],
                detectionId: [
                    { required: true, message: "监测点名称不能为空", trigger: "blur" }
                ],
                resolutionRatio: [
                    { required: true, message: "分辨率不能为空", trigger: "blur" }
                ],
                frameRate: [
                    { required: true, message: "帧率不能为空", trigger: "blur" }
                ],
                nightVision: [
                    { required: true, message: "夜视能力不能为空", trigger: "blur" }
                ],
                connectionType: [
                    { required: true, message: "连接方式不能为空", trigger: "change" }
                ],
                installationDate: [
                    { required: true, message: "安装日期不能为空", trigger: "blur" }
                ],
                guaranteePeriod: [
                    { required: true, message: "质保截止日期不能为空", trigger: "blur" }
                ],
                longitude: [
                    { required: true, message: "经度不能为空", trigger: "blur" }
                ],
                latitude: [
                    { required: true, message: "纬度不能为空", trigger: "blur" }
                ],
                cameraType: [
                    { required: true, message: "摄像头类型不能为空", trigger: "change" }
                ],
                cameraGroupIndication: [
                    { required: true, message: "摄像头组号不能为空", trigger: "blur" }
                ],
                cameraApplicationType: [
                    { required: true, message: "摄像头应用方向不能为空", trigger: "change" }
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
                districtId: [
                    { required: true, message: "地区id不能为空", trigger: "blur" }
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
        },
        getDistrictName(districtId) {
            const district = this.districtOptions.find(item => item.districtId === districtId);
            return district ? district.districtName : '未知地区';
        },
        //获取经纬度
        getLocation() {
            if (navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(
                    position => {
                        this.form.longitude = position.coords.longitude;
                        this.form.latitude = position.coords.latitude;
                    },
                    error => {
                        console.error('Error getting location:', error);
                        alert('无法获取当前位置，请检查定位服务是否启用。');
                    }
                );
            } else {
                alert('您的浏览器不支持定位功能');
            }
        },
        async getList() {
            try {
                const districtsResponse = await listDistrict();
                this.districts = districtsResponse.data;

                const detectionsResponse = await listDetection();
                this.detections = detectionsResponse.rows;

                listCamera(this.queryParams).then(response => {
                    this.cameraList = response.rows;
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
        // 取消按钮
        cancel() {
            this.open = false;
            this.reset();
        },
        // 表单重置
        reset() {
            this.form = {
                cameraId: null,
                cameraIp: null,
                cameraModel: null,
                cameraManufacturer: null,
                detectionId: null,
                resolutionRatio: null,
                frameRate: null,
                nightVision: null,
                connectionType: null,
                installationDate: null,
                guaranteePeriod: null,
                longitude: null,
                latitude: null,
                cameraType: null,
                cameraGroupIndication: null,
                cameraApplicationType: null,
                status: null,
                delFlag: null,
                remark: null,
                createTime: null,
                updateTime: null,
                districtId: null
            };
            this.resetForm("form");
        },
        // 根据 detectionId 获取监测点名字
        getDetectionName(detectionId) {
            const detection = this.detectionOptions.find(item => item.detectionId === detectionId)
            return detection ? detection.detectionName : '未知监测点';
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
            this.ids = selection.map(item => item.cameraId)
            this.single = selection.length !== 1
            this.multiple = !selection.length
        },
        /** 新增按钮操作 */
        handleAdd() {
            this.reset();
            this.open = true;
            this.title = "添加摄像头";
        },
        /** 修改按钮操作 */
        handleUpdate(row) {
            this.reset();
            const cameraId = row.cameraId || this.ids
            getCamera(cameraId).then(response => {
                this.form = response.data;
                this.open = true;
                this.title = "修改摄像头";
            });
        },
        /** 提交按钮 */
        submitForm() {
            this.$refs["form"].validate(valid => {
                if (valid) {
                    if (this.form.cameraId != null) {
                        updateCamera(this.form).then(response => {
                            this.$modal.msgSuccess("修改成功");
                            this.open = false;
                            this.getList();
                        });
                    } else {
                        addCamera(this.form).then(response => {
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
            const cameraIds = row.cameraId || this.ids;
            this.$modal.confirm('是否确认删除摄像头编号为"' + cameraIds + '"的数据项？').then(function () {
                return delCamera(cameraIds);
            }).then(() => {
                this.getList();
                this.$modal.msgSuccess("删除成功");
            }).catch(() => { });
        },
        /** 导出按钮操作 */
        handleExport() {
            this.download('cigarette/detection/camera/export', {
                ...this.queryParams
            }, `camera_${new Date().getTime()}.xlsx`)
        }
    }
};
</script>

<style scoped>
/* 样式可以根据需要调整 */
</style>