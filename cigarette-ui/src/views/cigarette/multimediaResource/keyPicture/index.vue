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
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="图片名称" prop="imageName">
        <el-input v-model="queryParams.imageName" placeholder="请输入图片名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="摄像头ID" prop="cameraId">
        <el-select v-model="queryParams.cameraId" placeholder="请选择摄像头编号" clearable>
          <el-option v-for="camersId in cameraIdList" :key="camersId.cameraId" :label="camersId.cameraId" :value="camersId.cameraId" />
        </el-select>
      </el-form-item>
      <el-form-item label="违法状态" prop="status">
            <el-select v-model="queryParams.status" placeholder="请选择违法状态" clearable>
              <el-option v-for="dict in dict.type.tob_illegal_status" :key="dict.value" :label="dict.label"
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
          v-hasPermi="['cigarette:picture:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
          v-hasPermi="['cigarette:picture:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
          v-hasPermi="['cigarette:picture:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
          v-hasPermi="['cigarette:picture:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="pictureList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="图片id" align="center" prop="keyPictureId" />
      <el-table-column label="图片url" align="center" prop="image" width="100">
        <template slot-scope="scope">
          <image-preview :src="scope.row.image" :width="50" :height="50" />
        </template>
      </el-table-column>
      <el-table-column label="图片名称" align="center" prop="imageName" :show-overflow-tooltip="true"/>
      <el-table-column label="摄像头id" align="center" prop="cameraId" />
      <el-table-column label="坐标" align="center" prop="coordinate" />

      <el-table-column label="违法状态" align="center" prop="status" >
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_illegal_status" :value="scope.row.status" />
            </template>
      </el-table-column>
      <el-table-column label="监测点" align="center" prop="detectionId" :show-overflow-tooltip="true">
            <template slot-scope="scope">
              {{ getDetectionName(scope.row.detectionId) }}
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" prop="remark" :show-overflow-tooltip="true"/>
      <el-table-column label="操作"fixed="right" align="center" class-name="small-padding fixed-width" min-width="150px">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
            v-hasPermi="['cigarette:picture:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
            v-hasPermi="['cigarette:picture:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
      @pagination="getList" />
    </el-col>
  </el-row>

    <!-- 添加或修改关键帧图片对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="图片" prop="image">
          <image-upload v-model="form.image" />
        </el-form-item>
          </el-col>
          <el-col :span="12">
        <el-form-item label="图片名称" prop="imageName">
          <el-input v-model="form.imageName" placeholder="请输入图片名称" />
        </el-form-item>
          <el-form-item label="坐标" prop="coordinate">
          <el-input v-model="form.coordinate" placeholder="请输入坐标" />
        </el-form-item>

        <el-form-item label="违法状态" prop="status">
            <el-select v-model="form.status" placeholder="请选择违法状态" clearable>
              <el-option v-for="dict in dict.type.tob_illegal_status" :key="dict.value" :label="dict.label"
                :value="dict.value" />
            </el-select>
          </el-form-item>
            </el-col>
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
import { listPicture, getPicture, delPicture, addPicture, updatePicture } from "@/api/cigarette/multimediaResource/picture";
import { listCamera  } from "@/api/cigarette/detection/camera";

import { listDistrict } from "@/api/cigarette/detection/district";
import { listDetection } from "@/api/cigarette/detection/detection";

export default {
  name: "Picture",
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
      // 关键帧图片表格数据
      pictureList: [],
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
        imageName: null,
        cameraId: null,
        status: null,
        detectionId: null,
        districtId: null
      },
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      districts: [],
      detections: [],
      detectionOptions: [],    //json数组，用于存储检测点选项
      treeData: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        image: [
          { required: true, message: "图片url不能为空", trigger: "blur" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        imageName: [
          { required: true, message: "图片名称不能为空", trigger: "blur" }
        ],
        cameraId: [
          { required: true, message: "摄像头id不能为空", trigger: "change" }
        ],
        status: [
          { required: true, message: "状态不能为空", trigger: "change" }
        ],
      }
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
    /** 查询关键帧图片列表 */
    async getList() {
      this.loading = true;
    try {
        const districtsResponse = await listDistrict();
        this.districts = districtsResponse.data;

        const detectionsResponse = await listDetection();
        this.detections = detectionsResponse.rows;

        listPicture(this.queryParams).then(response => {
        this.pictureList = response.rows;
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
          console.log("camera"+camera.detectionId);
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
        keyPictureId: null,
        image: null,
        createTime: null,
        imageName: null,
        cameraId: null,
        coordinate: null,
        remark: null,
        status: null,
        detectionId: null,
        districtId: null
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
      this.ids = selection.map(item => item.keyPictureId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加关键帧图片";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const keyPictureId = row.keyPictureId || this.ids
      getPicture(keyPictureId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改关键帧图片";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.keyPictureId != null) {
            updatePicture(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addPicture(this.form).then(response => {
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
      const keyPictureIds = row.keyPictureId || this.ids;
      this.$modal.confirm('是否确认删除关键帧图片编号为"' + keyPictureIds + '"的数据项？').then(function () {
        return delPicture(keyPictureIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cigarette/picture/export', {
        ...this.queryParams
      }, `picture_${new Date().getTime()}.xlsx`)
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
