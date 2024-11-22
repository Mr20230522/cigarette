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
          <el-form-item label="视频名称" prop="videoName">
            <el-input v-model="queryParams.videoName" placeholder="请输入视频名称" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="摄像头ID" prop="cameraId">
            <el-select v-model="queryParams.cameraId" placeholder="请选择摄像头编号" clearable>
              <!-- <el-option v-for="camersId in camersIds" :key="camersId.Id" :label="camersId.label" :value="camersId.Id" /> -->
            </el-select>
          </el-form-item>
          <el-form-item label="创建时间" prop="createTime">
            <el-date-picker clearable v-model="queryParams.createTime" type="date" value-format="yyyy-MM-dd"
              placeholder="请选择创建时间">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
            <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
          </el-form-item>
        </el-form>

        <el-row :gutter="10" class="mb8">
          <el-col :span="1.5">
            <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd"
              v-hasPermi="['multimediaResource:video:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
              v-hasPermi="['multimediaResource:video:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
              v-hasPermi="['multimediaResource:video:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
              v-hasPermi="['multimediaResource:video:export']">导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="videoList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="视频ID" align="center" prop="videoId" />
          <el-table-column label="行为ID" align="center" prop="actionId" />
          <el-table-column label="视频url" align="center" prop="videoPath" min-width="180px">
            <template slot-scope="scope">
              <video v-if="scope.row.videoPath" width="180px" height="140px" controls>
                <source :src="scope.row.videoPath" type="video/mp4">
                Your browser does not support the video tag.
              </video>
            </template>
          </el-table-column>
          <el-table-column label="视频名称" align="center" prop="videoName" />
          <el-table-column label="摄像头id" align="center" prop="cameraId" />
          <el-table-column label="起始时间" align="center" prop="startTime" width="180" />
          <el-table-column label="结束时间" align="center" prop="endTime" width="180" />
          <el-table-column label="视频大小" align="center" prop="videoSize">
            <template slot-scope="scope">
              {{ scope.row.videoSize }}MB
            </template>
          </el-table-column>
          <el-table-column label="监测区域" align="center" prop="detectionId">
            <template slot-scope="scope">
              {{ getDetectionName(scope.row.detectionId) }}
            </template>
          </el-table-column>
          <el-table-column label="状态" align="center" prop="status">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_illegal_status" :value="scope.row.status" />
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" prop="remark" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" min-width="150px">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['multimediaResource:video:edit']">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                v-hasPermi="['multimediaResource:video:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>

    <!-- 添加或修改关键帧视频对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="视频" prop="videoPath">
              <el-upload class="avatar-uploader" :action="uploadUrl" :on-progress="uploadVideoProcess"
                :on-success="handleVideoSuccess" :before-upload="beforeUploadVideo" :show-file-list="false"
                :headers="headers">
                <video v-if="form.videoPath != '' && !videoFlag" v-bind:src="form.videoPath" class="avatar video-avatar"
                  controls="controls">
                  您的浏览器不支持视频播放
                </video>
                <i v-else-if="form.videoPath == '' && !videoFlag" class="el-icon-plus avatar-uploader-icon"></i>
                <el-progress v-if="videoFlag == true" type="circle" v-bind:percentage="videoUploadPercent"
                  style="margin-top: 7px"></el-progress>
              </el-upload>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="视频名称" prop="videoName">
              <el-input v-model="form.videoName" placeholder="请输入视频名称" />
            </el-form-item>
            <el-form-item label="视频大小" prop="videoSize">
              <el-input v-model="form.videoSize" placeholder="请输入视频大小" />
            </el-form-item>
            <el-form-item label="违法状态" prop="status">
              <el-select v-model="form.status" placeholder="请选择违法状态" clearable>
                <el-option v-for="dict in dict.type.tob_illegal_status" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="12">
            <el-form-item label="起始时间" prop="startTime">
              <el-time-select v-model="form.startTime" format="mm:ss" value-format="mm:ss" :picker-options="{
                start: '00:00',
                step: '00:01',
                end: '06:00'
              }" placeholder="选择时间">
              </el-time-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endTime">
              <el-time-select v-model="form.endTime" format="mm:ss" value-format="mm:ss" :picker-options="{
                start: '00:00',
                step: '00:01',
                end: '06:00'
              }" placeholder="选择时间">
              </el-time-select>
            </el-form-item></el-col>
        </el-row>
        <el-form-item label="搜索设备">
          <el-input v-model="searchInput" @input="filterCameras" placeholder="请输入用户名称、邮箱或电话号码" clearable
            suffix-icon="el-icon-search"></el-input>
          <el-scrollbar wrap-class="scrollbar-wrapper" style="max-height: auto;">
            <el-card class="camera-list">
              <el-row v-for="(camera, index) in filteredCameras" :key="index" class="camera-info"
                :class="{ 'bg-color': index % 2 === 1, 'selected': camera === selectedCamera }">
                <el-col :span="24">
                  <span @click="selectCamera(camera)" class="label" style="cursor:pointer;">设备编号:{{ camera.cameraId
                    }}&nbsp;&nbsp;设备IP:{{ camera.cameraIp }}&nbsp;&nbsp;监测区域:{{ camera.cameraDetectionId }}
                  </span>
                </el-col>
              </el-row>
            </el-card>
          </el-scrollbar>
        </el-form-item>
        <el-row>
          <el-col :span="12">
            <el-form-item label="设备IP" prop="cameraIp">
              <el-input disabled v-model="form.cameraIp" placeholder="设备IP" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="监控区域" prop="cameraDetectionId">
              <el-input disabled v-model="form.cameraDetectionId" placeholder="监控区域" />
            </el-form-item>
          </el-col>
        </el-row>

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
import { listVideo, getVideo, delVideo, addVideo, updateVideo } from "@/api/cigarette/multimediaResource/keyVideo";

import { listDistrict } from "@/api/cigarette/detection/district";
import { listDetection } from "@/api/cigarette/detection/detection";
import { listCamera } from "@/api/cigarette/detection/camera";
import { getToken } from "@/utils/auth";

export default {
  name: "Video",

  dicts: ['tob_illegal_status'],
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
      // 关键帧视频表格数据
      videoList: [],
      // 弹出层标题
      title: "",
      // 存储搜索信息
      searchInput: '',
      // 存储用户信息
      cameraIdList: [],
      // 存储根据搜索条件过滤后的用户列表数据
      filteredCameras: [],
      // 存储所选用户信息
      selectedCamera: null,
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        videoName: null,
        cameraId: null,
        detectionId: null,
        districtId: null,
        status: null,
        createTime: null
      },
      // 表单参数
      form: {
        startTime: null,
        endTime: null,
        videoPath: "",
      },
      uploadUrl: process.env.VUE_APP_BASE_API + "/cigarette/multimediaResource/keyVideo/uploadVideo", // 上传的图片服务器地址
      headers: {
        Authorization: "Bearer " + getToken(),
      },
      videoFlag: false,
      videoUploadPercent: 0,
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      districts: [],
      detections: [],
      detectionOptions: [],    //json数组，用于存储检测点选项
      treeData: [],
      pickerOptions: {
        selectableRange: '00:00 - 59:59', // 只允许选择分钟和秒
      },
      // 表单校验
      rules: {
        videoPath: [
          { required: true, message: "视频url不能为空", trigger: "blur" }
        ],
        videoName: [
          { required: true, message: "视频名称不能为空", trigger: "blur" }
        ],
        cameraId: [
          { required: true, message: "摄像头id不能为空", trigger: "change" }
        ],
        startTime: [
          { required: true, message: "起始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ]
      },
    };
  },
  created() {
    this.getList();
    this.loadDetectionOptions(); // 加载检测点选项
    this.loadDistrictOptions(); // 加载地区选项
    this.getCameraList();
  },
  mounted() {
    this.filteredCameras = [];

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
    // 根据 detectionId 获取监测点名字
    getDetectionName(detectionId) {
      const detection = this.detectionOptions.find(item => item.detectionId === detectionId)
      return detection ? detection.detectionName : '未知监测点';
    },
    /** 查询关键帧视频列表 */
    async getList() {
      this.loading = true;
      try {
        const districtsResponse = await listDistrict();
        this.districts = districtsResponse.data;

        const detectionsResponse = await listDetection();
        this.detections = detectionsResponse.rows;

        listVideo(this.queryParams).then(response => {
          this.videoList = response.rows;
          this.total = response.total;
          this.loading = false;
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
        this.vehicleBehaviorList = [];
        detectionIds.forEach(async (id) => {
          this.queryParams.detectionId = id;
          const response = await listVehicleBehaviorVo(this.queryParams);
          this.vehicleBehaviorList.push(...response.rows);
        });
      } else {
        // 只有一个 detectionId，直接发送请求
        this.queryParams.detectionId = detectionIds[0];
        this.getList();
      }
    },
    getCameraList() {
      this.loading = true;
      listCamera({
        pageNum: null,
        pageSize: 100000
      }).then(response => {
        console.log("response");
        console.log(response);
        // 获取到用户信息后，保存原始用户列表数据
        this.cameraIdList = response.rows.map(camera => {
          console.log("camera" + camera.detectionId);
          return {
            cameraDetectionId: camera.detectionId,
            cameraIp: camera.cameraIp,
            cameraId: camera.cameraId,
            districtId: camera.districtId
          };
        });
      }).catch(error => {
        console.error('Failed to fetch camera list:', error);
      })
        .finally(() => {
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
        videoId: null,
        actionId:null,
        videoPath: null,
        videoName: null,
        cameraId: null,
        startTime: null,
        endTime: null,
        videoSize: null,
        detectionId: null,
        districtId: null,
        remark: null,
        status: null,
        createTime: null
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
      this.ids = selection.map(item => item.videoId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加关键帧视频";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const videoId = row.videoId || this.ids
      getVideo(videoId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改关键帧视频";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.videoId != null) {
            updateVideo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addVideo(this.form).then(response => {
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
      const videoIds = row.videoId || this.ids;
      this.$modal.confirm('是否确认删除关键帧视频编号为"' + videoIds + '"的数据项？').then(function () {
        return delVideo(videoIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('multimediaResource/video/export', {
        ...this.queryParams
      }, `video_${new Date().getTime()}.xlsx`)
    },
    // 选择数据化进行数据填充
    filterCameras() {
      const searchInput = this.searchInput.toLowerCase().trim();
      if (!searchInput) {
        // 如果搜索条件为空，不显示任何用户
        this.filteredCameras = [];
        return;
      }
      this.filteredCameras = this.cameraIdList.filter(camera => {
        // 在用户名 邮箱和电话号码中进行搜索匹配
        return (
          camera.cameraDetectionId.toString().includes(searchInput) ||
          camera.cameraIp.toString().includes(searchInput) ||
          camera.cameraId.toString().includes(searchInput)

        );
      }).slice(0, 10);
    },
    // 搜索用户并筛选数据
    selectCamera(camera) {
      // 将所选用户信息存储到 selectedUser 变量中
      this.selectedCamera = camera;
      // 更新表单数据
      this.$set(this.form, "cameraDetectionId", camera.cameraDetectionId);
      this.$set(this.form, "cameraIp", camera.cameraIp);
      this.$set(this.form, "cameraId", camera.cameraId);
      this.$set(this.form, "detectionId", camera.cameraDetectionId);
      this.$set(this.form, "districtId", camera.districtId);
      this.form.cameraId = camera.cameraId;
      // 清空搜索输入框和搜索结果
      // this.searchInput = '';
      this.filteredCameras = [];
      this.filteredCameras.push(camera);
    },
    beforeUploadVideo(file) {
      const allowedTypes = [
        "video/mp4",
        "video/ogg",
        "video/flv",
        "video/avi",
        "video/wmv",
        "video/rmvb",
        "video/mov",
      ];
      if (!allowedTypes.includes(file.type)) {
        this.$message.error("请上传正确的视频格式");
        return false;
      }
      const fileSize = file.size / 1024 / 1024 < 50; // 控制视频大小不超过50MB
      if (!fileSize) {
        this.$message.error("视频大小不能超过50MB");
        return false;
      }
      console.log("beforeUploadVideo");
      console.log("视频符合要求");

      this.videoFlag = false;
    },
    uploadVideoProcess(event, file, fileList) {
      this.videoFlag = true;
      this.videoUploadPercent = file.percentage.toFixed(0) * 1;
    },
    handleVideoSuccess(res, file) {
      this.videoFlag = false;
      this.videoUploadPercent = 0;
      if (res.code === 200) {
        this.form.videoPath = res.data;
      } else {
        this.$message.error("视频上传失败，请重新上传！");
      }
    },
  },
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
