<template>
  <div>
    <el-row :gutter="20">
      <!-- 地区数据 -->
      <el-col :span="4" :xs="24">
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
          <el-table-column label="摄像头id" align="center" prop="cameraId" width="80"/>
          <el-table-column label="traffic摄像头ID" align="center" prop="trafficCameraId" width="120"/>
          <el-table-column label="摄像头名称" align="center" prop="cameraName" width="150" :show-overflow-tooltip="true"/>
          <el-table-column label="摄像头ip" align="center" prop="cameraIp" width="130px" :show-overflow-tooltip="true"/>
          <el-table-column label="摄像头型号" align="center" prop="cameraModel" width="100":show-overflow-tooltip="true"/>
          <el-table-column label="摄像头厂商" align="center" prop="cameraManufacturer" width="100"/>
          <el-table-column label="监测点" align="center" prop="detectionId" :show-overflow-tooltip="true">
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
          <el-table-column label="经度" align="center" prop="longitude" min-width="120">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="longitudeAndLatitude(scope.row)">{{
                  scope.row.longitude }}</el-button>
            </template>
          </el-table-column>
          <el-table-column label="纬度" align="center" prop="latitude" min-width="120">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="longitudeAndLatitude(scope.row)">{{
                  scope.row.latitude }}</el-button>
            </template>
          </el-table-column>
          <el-table-column label="摄像头类型" align="center" prop="cameraType" width="100">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_camera_type" :value="scope.row.cameraType" />
            </template>
          </el-table-column>
          <el-table-column label="摄像头组号" align="center" prop="cameraGroupIndication" width="100"/>
          <el-table-column label="摄像头应用方向" align="center" prop="cameraApplicationType" width="120">
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
          <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true"/>
          <el-table-column label="所属地区" align="center" prop="districtId">
            <template slot-scope="scope">
              {{ getDistrictName(scope.row.districtId) }}
            </template>
          </el-table-column>
          <el-table-column fixed="right" label="操作" align="center" class-name="small-padding fixed-width" width="130px">
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

    <!-- 新增/修改表单 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <!-- 第一行：摄像头名称和traffic摄像头ID（关联下拉框） -->
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="摄像头名称" prop="cameraName">
              <el-select
                v-model="selectedCameraName"
                placeholder="请选择摄像头名称"
                filterable
                :loading="loadingCameraPairs"
                style="width: 100%"
                @change="handleCameraNameChange">
                <el-option
                  v-for="item in cameraNameOptions"
                  :key="item.cameraId"
                  :label="item.cameraName"
                  :value="item.cameraName">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="traffic摄像头ID" prop="trafficCameraId">
              <el-select
                v-model="form.trafficCameraId"
                placeholder="请选择traffic摄像头ID"
                filterable
                :loading="loadingCameraPairs"
                style="width: 100%"
                @change="handleTrafficCameraIdChange">
                <el-option
                  v-for="item in trafficCameraIdOptions"
                  :key="item.cameraId"
                  :label="item.cameraId.toString()"
                  :value="item.cameraId">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第二行：摄像头ip（原字段） -->
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

        <!-- 第三行：摄像头厂商（原字段） -->
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

        <!-- 第四行：分辨率和帧率（原字段） -->
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

        <!-- 第五行：夜视能力和连接方式（原字段） -->
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

        <!-- 第六行：安装日期和质保截止日期（原字段） -->
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

        <!-- 第七行：摄像头类型和摄像头组号（原字段） -->
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

        <!-- 第八行：摄像头应用方向和状态（原字段） -->
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

        <!-- 第九行：经纬度（原字段） -->
        <el-row>
          <el-col :span="4">
            <el-form-item>
              <el-button type="primary" plain @click="getLocation">获取经纬度</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item label="经度" prop="longitude">
              <el-input v-model="form.longitude" placeholder="请输入经度" />
            </el-form-item>
          </el-col>
          <el-col :span="9">
            <el-form-item label="纬度" prop="latitude">
              <el-input v-model="form.latitude" placeholder="请输入纬度" />
            </el-form-item>
          </el-col>
        </el-row>

        <!-- 第十行：备注（原字段） -->
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
import { listCamera, getCamera, delCamera, addCamera, updateCamera, listCameraByDistrictId ,simpleAddCamera} from "@/api/cigarette/detection/camera";
import { listDistrict } from "@/api/cigarette/detection/district";
import { listDetection } from "@/api/cigarette/detection/detection";
import { getAllCameraIdNamePairs } from "@/api/cigarette/trafficData/trafficData";

export default {
  name: "Camera",
  dicts: ['sys_normal_disable', 'tob_camera_orientation', 'tob_camera_type', 'tob_del_flag', 'tob_connection_type', 'tob_night_vision', 'tob_frame_rate', 'tob_resolution_ratio'],
  data() {
    return {
      districtOptions: [],
      districtTreeOptions: undefined,
      districtName: undefined,
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      cameraList: [],
      title: "",
      open: false,
      districts: [],
      detections: [],
      treeData: [],
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      detectionOptions: [],

      // 摄像头配对数据
      cameraPairOptions: [],
      loadingCameraPairs: false,
      selectedCameraName: null,

      queryParams: {
        pageNum: 1,
        pageSize: 10,
        trafficCameraId: null,
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
      },

      // 表单参数
      form: {
        cameraId: null,              // tob_camera表主键（自增）
        trafficCameraId: null,       // trafficdata表中的摄像头ID
        cameraName: null,
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
        remark: null,
        createTime: null,
        updateTime: null,
        districtId: null,
        cameraUrl: null
      },

      // 表单校验
      rules: {
        cameraName: [
          { required: true, message: "摄像头名称不能为空", trigger: "change" }
        ],
        trafficCameraId: [
          { required: true, message: "traffic摄像头ID不能为空", trigger: "change" }
        ],
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
        districtId: [
          { required: true, message: "地区id不能为空", trigger: "blur" }
        ]
      }
    };
  },

  computed: {
    // 摄像头名称选项（用于名称下拉框）
    cameraNameOptions() {
      return this.cameraPairOptions;
    },
    // traffic摄像头ID选项（用于ID下拉框）
    trafficCameraIdOptions() {
      return this.cameraPairOptions;
    }
  },

  created() {
    this.getList();
    this.loadDetectionOptions();
    this.loadDistrictOptions();
    this.loadCameraPairOptions();
  },

  methods: {
    // 加载摄像头配对数据
    async loadCameraPairOptions() {
      this.loadingCameraPairs = true;
      try {
        const response = await getAllCameraIdNamePairs();
        this.cameraPairOptions = response;
        console.log("加载摄像头配对数据:", response);
      } catch (error) {
        console.error("Failed to load camera pair options:", error);
        this.$modal.msgError("获取摄像头选项失败");
      } finally {
        this.loadingCameraPairs = false;
      }
    },

    // 摄像头名称变化时，自动填充对应的trafficCameraId
    handleCameraNameChange(selectedName) {
      const selected = this.cameraPairOptions.find(item => item.cameraName === selectedName);
      if (selected) {
        this.form.trafficCameraId = selected.cameraId;
        this.form.cameraName = selected.cameraName;
        console.log("选择摄像头名称:", selectedName, "→ trafficCameraId:", selected.cameraId);
      }
    },

    // traffic摄像头ID变化时，自动填充对应的名称
    handleTrafficCameraIdChange(selectedId) {
      const selected = this.cameraPairOptions.find(item => item.cameraId === selectedId);
      if (selected) {
        this.form.cameraName = selected.cameraName;
        this.form.trafficCameraId = selected.cameraId;
        console.log("选择traffic摄像头ID:", selectedId, "→ 名称:", selected.cameraName);
      }
    },

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

    handleDetectionChange(newValue) {
      const selectedDetection = this.detectionOptions.find(item => item.detectionId === newValue);
      if (selectedDetection) {
        this.form.districtId = selectedDetection.districtId;
      }
    },

    getDistrictName(districtId) {
      const district = this.districtOptions.find(item => item.districtId === districtId);
      return district ? district.districtName : '未知地区';
    },

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

      if (detectionIds.length > 1) {
        this.cameraList = [];
        detectionIds.forEach(async (id) => {
          this.queryParams.detectionId = id;
          const response = await listCamera(this.queryParams);
          this.cameraList.push(...response.rows);
        });
      } else {
        this.queryParams.detectionId = detectionIds[0];
        this.getList();
      }
    },

    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.cameraId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },

    cancel() {
      this.open = false;
      this.reset();
    },

    reset() {
      this.form = {
        cameraId: null,
        trafficCameraId: null,
        cameraName: null,
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
        remark: null,
        createTime: null,
        updateTime: null,
        districtId: null,
        cameraUrl: null
      };
      this.selectedCameraName = null;
      this.resetForm("form");
    },

    getDetectionName(detectionId) {
      const detection = this.detectionOptions.find(item => item.detectionId === detectionId);
      return detection ? detection.detectionName : '未知监测点';
    },

    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加摄像头";
      console.log("打开新增对话框，title=", this.title);
    },

    handleUpdate(row) {
      this.reset();
      const cameraId = row.cameraId || this.ids;
      console.log("执行 handleUpdate，cameraId:", cameraId);

      getCamera(cameraId).then(response => {
        this.form = response.data;
        this.title = "修改摄像头";
        // 如果有cameraName，设置选中的名称
        if (this.form.cameraName) {
          this.selectedCameraName = this.form.cameraName;
        }
        console.log("handleUpdate 中获取到的数据:", this.form);
        this.open = true;
      }).catch(error => {
        console.error("获取摄像头详情失败:", error);
        this.$modal.msgError("获取摄像头详情失败");
      });
    },

    submitForm() {
      console.log("========== 提交表单 ==========");
      console.log("当前标题 title:", this.title);
      console.log("当前表单数据:", this.form);

      this.$refs["form"].validate(valid => {
        if (valid) {
          const submitData = {
            ...this.form,
            detectionId: this.form.detectionId ? Number(this.form.detectionId) : null,
            districtId: this.form.districtId ? Number(this.form.districtId) : null,
            frameRate: this.form.frameRate ? Number(this.form.frameRate) : 25,
            cameraGroupIndication: this.form.cameraGroupIndication ? Number(this.form.cameraGroupIndication) : 1,
            longitude: this.form.longitude ? Number(this.form.longitude) : 0,
            latitude: this.form.latitude ? Number(this.form.latitude) : 0,
            trafficCameraId: this.form.trafficCameraId ? Number(this.form.trafficCameraId) : null
          };

          console.log("提交数据:", submitData);

          if (this.title === "修改摄像头") {
            updateCamera(submitData).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            }).catch(error => {
              console.error("修改失败:", error);
              this.$modal.msgError(error.response?.data?.msg || "修改失败");
            });
          } else {
            simpleAddCamera(submitData).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            }).catch(error => {
              console.error("新增失败:", error);
              this.$modal.msgError(error.response?.data?.msg || "新增失败");
            });
          }
        } else {
          console.log("表单校验失败");
        }
      });
    },

    handleDelete(row) {
      const cameraIds = row.cameraId || this.ids;
      this.$modal.confirm('是否确认删除摄像头编号为"' + cameraIds + '"的数据项？').then(function () {
        return delCamera(cameraIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    handleExport() {
      this.download('cigarette/detection/camera/export', {
        ...this.queryParams
      }, `camera_${new Date().getTime()}.xlsx`);
    },

    longitudeAndLatitude(row) {
      this.$router.push({
        name: 'showMapLocation',
        params: {
          paramLongitude: row.longitude,
          paramLatitude: row.latitude,
        }
      });
    },
  }
};
</script>

<style scoped>
/* 样式可以根据需要调整 */
</style>
