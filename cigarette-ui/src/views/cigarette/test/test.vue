<template>
  <div style="margin-left: 5%; width: 90%;">
    <el-card style="margin-top: 5%;margin-bottom: 5%;">
      <h2>自动化测试</h2>

      <el-button type="primary" plain icon="el-icon-plus" style="margin-bottom: 30px;" size="mini"
        @click="handleAdd">新增</el-button>
      <el-steps :active="1">
        <el-step title="车辆录入" icon="el-icon-edit"></el-step>
        <el-step title="驾驶人员录入" icon="el-icon-upload"></el-step>
        <el-step title="预警中控" icon="el-icon-picture"></el-step>
        <el-step title="记录执勤行为" icon="el-icon-picture-outline"></el-step>
      </el-steps>
      <el-button type="primary" style="margin-top: 30px;" disabled>测试值班行为</el-button>
    </el-card>
    <el-card>
      <h2>人工操作测试</h2>
      <el-row>

        <el-button type="primary" disabled>临时预警</el-button>

      </el-row>
    </el-card>
    <!-- 添加或修改车辆行为记录对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="60%" append-to-body>

      <el-button type="primary" plain @click="generateData()">生成模拟数据</el-button>

      <!-- 上传图片 -->
      <el-card>
        <el-form ref="form1" :model="form1" :rules="rules1" label-width="80px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="图片" prop="image">
                <image-upload v-model="form1.image" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="图片名称" prop="imageName">
                <el-input v-model="form1.imageName" placeholder="请输入图片名称" />
              </el-form-item>
              <el-form-item label="坐标" prop="coordinate">
                <el-input v-model="form1.coordinate" placeholder="请输入坐标" />
              </el-form-item>

              <el-form-item label="违法状态" prop="status">
                <el-select v-model="form1.status" placeholder="请选择违法状态" clearable>
                  <el-option v-for="dict in dict.type.tob_illegal_status" :key="dict.value" :label="dict.label"
                    :value="dict.value" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="搜索设备">
            <el-input v-model="searchInput1" @input="filterCameras" placeholder="请输入用户名称、邮箱或电话号码" clearable
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
                <el-input disabled v-model="form1.cameraIp" placeholder="设备IP" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="监控区域" prop="cameraDetectionId">
                <el-input disabled v-model="form1.cameraDetectionId" placeholder="监控区域" />
              </el-form-item>
            </el-col>
          </el-row>


          <el-form-item label="备注" prop="remark">
            <el-input v-model="form1.remark" type="textarea" placeholder="请输入内容" />
          </el-form-item>
        </el-form>
      </el-card>
      <!-- 上传视频 -->
      <el-card>
        <el-form ref="form2" :model="form2" :rules="rules2" label-width="80px">
          <el-row>
            <el-col :span="12">
              <el-form-item label="视频" prop="videoPath">
                <el-upload class="avatar-uploader" :action="uploadUrl" :on-progress="uploadVideoProcess"
                  :on-success="handleVideoSuccess" :before-upload="beforeUploadVideo" :show-file-list="false"
                  :headers="headers">
                  <video v-if="form2.videoPath != '' && !videoFlag" v-bind:src="form2.videoPath"
                    class="avatar video-avatar" controls="controls">
                    您的浏览器不支持视频播放
                  </video>
                  <i v-else-if="form2.videoPath == '' && !videoFlag" class="el-icon-plus avatar-uploader-icon"></i>
                  <el-progress v-if="videoFlag == true" type="circle" v-bind:percentage="videoUploadPercent"
                    style="margin-top: 7px"></el-progress>
                </el-upload>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="视频名称" prop="videoName">
                <el-input v-model="form2.videoName" placeholder="请输入视频名称" />
              </el-form-item>
              <el-form-item label="视频大小" prop="videoSize">
                <el-input v-model="form2.videoSize" placeholder="请输入视频大小" />
              </el-form-item>
              <el-form-item label="违法状态" prop="status">
                <el-select v-model="form2.status" placeholder="请选择违法状态" clearable>
                  <el-option v-for="dict in dict.type.tob_illegal_status" :key="dict.value" :label="dict.label"
                    :value="dict.value" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="起始时间" prop="startTime">
                <el-time-select v-model="form2.startTime" format="mm:ss" value-format="mm:ss" :picker-options="{
                  start: '00:00',
                  step: '00:01',
                  end: '06:00'
                }" placeholder="选择时间">
                </el-time-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="结束时间" prop="endTime">
                <el-time-select v-model="form2.endTime" format="mm:ss" value-format="mm:ss" :picker-options="{
                  start: '00:00',
                  step: '00:01',
                  end: '06:00'
                }" placeholder="选择时间">
                </el-time-select>
              </el-form-item></el-col>
          </el-row>
          <el-form-item label="搜索设备">
            <el-input v-model="searchInput1" @input="filterCameras" placeholder="请输入用户名称、邮箱或电话号码" clearable
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
                <el-input disabled v-model="form2.cameraIp" placeholder="设备IP" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="监控区域" prop="cameraDetectionId">
                <el-input disabled v-model="form2.cameraDetectionId" placeholder="监控区域" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="备注" prop="remark">
            <el-input v-model="form2.remark" type="textarea" placeholder="请输入内容" />
          </el-form-item>
        </el-form>
      </el-card>
      <el-card>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="搜索">
            <el-input v-model="searchInputCar" @input="filterCar" placeholder="请输入车辆编号或者车牌号" clearable
              suffix-icon="el-icon-search"></el-input>
            <el-scrollbar wrap-class="scrollbar-wrapper" style="max-height: 'auto';">
              <el-card class="user-list">
                <el-row v-for="(car, index) in filteredCar" :key="index" class="user-info"
                  :class="{ 'bg-color': index % 2 === 1, 'selected': car === selectedCar }">
                  <el-col :span="24">
                    <span @click="selectCar(car)" class="label" style="cursor:pointer;">车辆ID:{{ car.carId
                      }}&nbsp;&nbsp;车牌编号:{{ car.licensePlate }}</span>
                  </el-col>
                </el-row>
              </el-card>
            </el-scrollbar>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="车辆编号" prop="carId">
                <el-input :disabled="true" v-model="form.carId" placeholder="请输入车辆编号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">

              <el-form-item label="车牌编号" prop="name">
                <el-input :disabled="true" v-model="form.licensePlate" placeholder="请输入车牌编号" />
              </el-form-item>
            </el-col>
          </el-row>


          <el-row>
            <el-col :span="12">
              <el-form-item label="车型" prop="carTypeId">
                <el-select v-model="form.carTypeId" placeholder="请选择车型" clearable>
                  <el-option v-for="dict in dict.type.tob_vehicle_type" :key="dict.value" :label="dict.label"
                    :value="dict.value" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="车身颜色" prop="carColor">
                <el-input v-model="form.carColor" placeholder="请输入车身颜色" />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row>
            <el-col :span="12">
              <el-form-item label="嫌疑程度" prop="degreeSuspicion">
                <template>
                  <div class="block">
                    <el-slider v-model="form.degreeSuspicion" show-input>
                    </el-slider>
                  </div>
                </template>
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
          <el-form-item label="搜索">
            <el-input v-model="searchInput" @input="filterUsers" placeholder="请输入驾驶员名称、ID 或电话号码" clearable
              suffix-icon="el-icon-search"></el-input>
            <el-scrollbar wrap-class="scrollbar-wrapper" style="max-height: 'auto';">
              <el-card class="user-list">
                <el-row v-for="(user, index) in filteredUsers" :key="index" class="user-info"
                  :class="{ 'bg-color': index % 2 === 1, 'selected': user === selectedUser }">
                  <el-col :span="24">
                    <span @click="selectUser(user)" class="label" style="cursor:pointer;">用户ID:{{ user.id
                      }}&nbsp;&nbsp;用户名称:{{ user.username }}&nbsp;&nbsp;电话号码:{{ user.phonenumber }}
                    </span>
                  </el-col>
                </el-row>
              </el-card>
            </el-scrollbar>
          </el-form-item>
          <el-row>
            <el-col :span="12">
              <el-form-item label="车主姓名" prop="name">
                <el-input :disabled="true" v-model="form.name" placeholder="请输入车主姓名" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="电话号码" prop="phone">
                <el-input :disabled="true" v-model="form.phone" placeholder="请输入车主电话号码" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item label="车员编号" prop="driverId">
                <el-input :disabled="true" v-model="form.driverId" placeholder="请输入驾驶员编号" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="行驶方向" prop="drivingDirection">
                <el-select v-model="form.drivingDirection" placeholder="行驶方向" clearable>
                  <el-option v-for="dict in dict.type.tob_driving_irection" :key="dict.value" :label="dict.label"
                    :value="dict.value" />
                </el-select>
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
          <el-form-item label="备注" prop="remark">
            <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
          </el-form-item>
          <el-form-item label="同伙人" prop="accompliceId">
            <div style="text-align: center">
              <el-transfer style="text-align: left; display: inline-block;width: 100%; " v-model="value" filterable
                :left-default-checked="[2, 3]" :right-default-checked="[1]" :render-content="renderFunc"
                :titles="['人员库', '同伙人']" :button-texts="['到左边', '到右边']" :format="{
                  noChecked: '${total}',
                  hasChecked: '${checked}/${total}'
                }" @change="handleChange" :data="transferData">
              </el-transfer>
            </div>
          </el-form-item>
        </el-form>
        <div class="dialog-footer">
          <el-button type="primary" @click="submitForm">确 定</el-button>
          <el-button @click="cancel">取 消</el-button>
        </div>
      </el-card>
    </el-dialog>

  </div>
</template>

<script>
import { addVehicleBehavior, updateVehicleBehavior } from "@/api/cigarette/vehicle/vehicleBehavior";
import { listPerson } from "@/api/cigarette/personnel/person";
import { listVehicle } from "@/api/cigarette/vehicle/vehicle";
import { addPicture } from "@/api/cigarette/multimediaResource/picture";
import { listCamera } from "@/api/cigarette/detection/camera";
import { listVideo, getVideo, delVideo, addVideo, updateVideo } from "@/api/cigarette/multimediaResource/keyVideo";
import { listDistrict } from "@/api/cigarette/detection/district";
import { listDetection } from "@/api/cigarette/detection/detection";
import { addCaution } from "@/api/cigarette/caution/caution";
import { getToken } from "@/utils/auth";
export default {
  name: "VehicleBehavior",
  dicts: ['tob_illegal_status', 'sys_normal_disable', 'tob_driving_irection', 'tob_vehicle_type', 'tob_caution_status'],
  data() {

    return {
      // 添加图片
      form1: {},
      searchInput1: '',
      // 存储用户信息
      cameraIdList: [],
      // 存储根据搜索条件过滤后的用户列表数据
      filteredCameras: [],
      // 存储所选用户信息
      selectedCamera: null,
      rules1: {
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
      },
      // 添加视频
      form2: {
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
      rules2: {
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



      //添加车辆行为
      // 穿梭框属性
      transferData: [], // 这里将被动态数据填充
      value: [], // 这里存储已选择的key
      leftCheckedKeys: [2, 3], // 根据实际情况设置
      rightCheckedKeys: [1], // 根据实际情况设置
      renderFunc(h, option) {
        return h('span', option.key + ' - ' + option.label);
      },

      // 遮罩层
      loading: true,
      // 总条数
      total: 0,
      carTypeList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      submitCautionOpen: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        carId: null,
        carTypeId: null,
        carColor: null,
        degreeSuspicion: null,
        driverId: null,
        drivingDirection: null,
        illegalStatus: null,
        status: null,
        accompliceId: null
      },

      // 表单参数

      form: {
        accompliceId: []
      },


      cautionForm: {},
      // 存储搜索信息
      searchInput: '',

      // 存储用户信息
      userIdList: [],
      // 存储根据搜索条件过滤后的用户列表数据
      filteredUsers: [],
      // 存储所选用户信息
      selectedUser: null,
      // 存储车辆搜索信息
      searchInputCar: '',
      // 存储用户信息
      carIdList: [],
      // 存储根据搜索条件过滤后的用户列表数据
      filteredCar: [],
      // 存储所选用户信息
      selectedCar: null,
      defaultProps: {
        children: 'children',
        label: 'label'
      },
      districts: [],
      detections: [],
      detectionOptions: [],    //json数组，用于存储检测点选项
      treeData: [],
      // 表单校验
      rules: {
        carTypeId: [{
          required: true,
          message: "车型不能为空",
          trigger: "blur"
        }],
        degreeSuspicion: [{
          required: true,
          message: "嫌疑程度不能为空",
          trigger: "blur"
        }],
        drivingDirection: [{
          required: true,
          message: "行驶方向不能为空",
          trigger: "change"
        }],
        illegalStatus: [{
          required: true,
          message: "违法状态不能为空",
          trigger: "change"
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
    // 行为记录
    this.getList();
    this.loadDetectionOptions(); // 加载检测点选项
    this.loadDistrictOptions(); // 加载地区选项


    // 添加图片
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
    async getList() {
      try {
        const districtsResponse = await listDistrict();
        this.districts = districtsResponse.data;

        const detectionsResponse = await listDetection();
        this.detections = detectionsResponse.rows;
        this.loading = false;
      } catch (error) {
        console.error('Failed to fetch data:', error);
        this.loading = false;
      }
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.submitCautionOpen = false;
      this.reset();
    },
    // 表单重置
    reset() {
      // 行为记录
      this.form = {
        behaviorId: null,
        carId: null,
        carTypeId: null,
        carColor: null,
        degreeSuspicion: null,
        driverId: null,
        drivingDirection: null,
        illegalStatus: null,
        status: null,
        delFlag: null,
        remark: null,
        createTime: null,
        updateTime: null,
        accompliceId: null
      };
      this.resetForm("form");
      this.form1 = {
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
      this.resetForm("form1");
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      listPerson({
        pageNum: null,
        pageSize: 100000
      }).then(response => {
        console.log(response.rows);
        // 获取到用户信息后，保存原始用户列表数据
        this.userIdList = response.rows.map(user => {
          return {
            id: user.suspectId,
            username: user.name,
            phonenumber: user.phone
          };
        });
        this.transferData = response.rows.map(item => ({
          key: item.suspectId, // 确保这里的id是唯一的
          label: item.name + item.phone, // 假设你的数据有一个name属性
          disabled: item.disabled // 如果有disabled属性
        }));


      }).catch(error => {
        console.error('Failed to fetch user list:', error);
      });
      listVehicle({
        pageNum: null,
        pageSize: 100000
      }).then(response => {
        // 获取到用户信息后，保存原始用户列表数据
        this.carIdList = response.rows.map(car => {
          return {
            carId: car.carId,
            licensePlate: car.licensePlate,
          };
        });
      }).catch(error => {
        console.error('Failed to fetch user list:', error);
      });
      this.open = true;
      this.title = "添加车辆行为记录";
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.behaviorId != null) {
            updateVehicleBehavior(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
            });
          } else {
            addVehicleBehavior(this.form).then(response => {
              this.$modal.msgSuccess("行为记录新增成功");
            });
          }
          addPicture(this.form1).then(response => {
            this.$modal.msgSuccess("图片新增成功");
          });
          addVideo(this.form2).then(response => {
            this.$modal.msgSuccess("视频新增成功");
            this.open = false;
          });
        }
      });
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
      }).slice(0, 10);
    },
    // 搜索用户并筛选数据
    selectUser(user) {
      // 将所选用户信息存储到 selectedUser 变量中
      this.selectedUser = user;
      // 更新表单数据
      this.$set(this.form, "driverId", user.id);
      this.$set(this.form, "name", user.username);
      this.$set(this.form, "phone", user.phonenumber);
    },

    // 选择数据化进行数据填充
    filterCar() {
      const searchInputCar = this.searchInputCar.toLowerCase().trim();
      if (!searchInputCar) {
        // 如果搜索条件为空，不显示任何用户
        this.filteredCar = [];
        return;
      }
      this.filteredCar = this.carIdList.filter(car => {
        // 在用户名、ID和电话号码中进行搜索匹配
        return (
          car.licensePlate.toLowerCase().includes(searchInputCar) ||
          car.carId.toString().includes(searchInputCar)
        );
      }).slice(0, 10);
    },
    // 搜索用户并筛选数据
    selectCar(car) {
      // 将所选用户信息存储到 selectedCar 变量中
      this.selectedCar = car;
      // 更新表单数据
      this.$set(this.form, "carId", car.carId);
      this.$set(this.form, "licensePlate", car.licensePlate);
    },
    handleChange(value, direction, movedKeys) {
      this.form.accompliceId = value.join(',');
      console.log(value, direction, movedKeys);
    },
    addCautionFrom(row) {
      this.submitCautionOpen = true;
      this.cautionForm.detectionId = row.detectionId;
      this.cautionForm.districtId = row.districtId;
      this.cautionForm.behaviorId = row.behaviorId;

    },
    /** 提交按钮 */
    submitCautionForm() {

      this.$refs["cautionForm"].validate(valid => {
        if (valid) {
          addCaution(this.cautionForm).then(response => {
            this.$modal.msgSuccess("新增成功");
            this.submitCautionOpen = false;
          });
        }
      });
    },
    addCaseFrom(row) {
      this.$router.push({
        path: '/cigarette/personnel/caseInformation',

      });
    },


    // 添加图片
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
    // 选择数据化进行数据填充
    filterCameras() {
      const searchInput1 = this.searchInput1.toLowerCase().trim();
      if (!searchInput1) {
        // 如果搜索条件为空，不显示任何用户
        this.filteredCameras = [];
        return;
      }
      this.filteredCameras = this.cameraIdList.filter(camera => {
        // 在用户名 邮箱和电话号码中进行搜索匹配
        return (
          camera.cameraDetectionId.toString().includes(searchInput1) ||
          camera.cameraIp.toString().includes(searchInput1) ||
          camera.cameraId.toString().includes(searchInput1)

        );
      }).slice(0, 10);
    },
    // 搜索用户并筛选数据
    selectCamera(camera) {
      // 将所选用户信息存储到 selectedUser 变量中
      this.selectedCamera = camera;
      // 更新表单数据
      this.$set(this.form1, "cameraDetectionId", camera.cameraDetectionId);
      this.$set(this.form1, "cameraIp", camera.cameraIp);
      this.$set(this.form1, "cameraId", camera.cameraId);
      this.$set(this.form1, "detectionId", camera.cameraDetectionId);
      this.$set(this.form1, "districtId", camera.districtId);
      this.form1.cameraId = camera.cameraId;
      this.$set(this.form2, "cameraDetectionId", camera.cameraDetectionId);
      this.$set(this.form2, "cameraIp", camera.cameraIp);
      this.$set(this.form2, "cameraId", camera.cameraId);
      this.$set(this.form2, "detectionId", camera.cameraDetectionId);
      this.$set(this.form2, "districtId", camera.districtId);
      this.form2.cameraId = camera.cameraId;
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
        this.form2.videoPath = res.data;
      } else {
        this.$message.error("视频上传失败，请重新上传！");
      }
    },
    generateData() {
    // 添加图片
    // 图片名称
    this.form1.imageName= ['image1.jpg', 'image2.jpg', 'image3.jpg', 'image4.jpg'][Math.floor(Math.random() * 4)];
    // 坐标
    this.form1.coordinate=`(${Math.floor(Math.random() * 100)},${Math.floor(Math.random() * 100)})`;  // 随机生成坐标
    // 违法状态
    this.form1.status=['1', '2', '3'][Math.floor(Math.random() * 3)];  // 随机选择违法状态

    // 备注
    this.form1.remark='备注：' + Math.random().toString(36).substring(2, 15); // 生成随机备注

    // 添加视频
    // 视频名称
    this.form2.videoName = ['video1.MP4', 'video2.MP4', 'video3.MP4', 'video.MP4'][Math.floor(Math.random() * 4)];
    // 视频大小
    this.form2.videoSize=`${Math.floor(Math.random() * 100)}`;
    // 违法状态
    this.form2.status=['1', '2', '3'][Math.floor(Math.random() * 3)];  // 随机选择违法状态
    // 起始时间
    this.form2.startTime=['00:05', '00:10', '00:15', '00:20'][Math.floor(Math.random() * 4)];  // 随机选择违法状态
    // 结束时间" 
    this.form2.endTime=['00:45', '00:45', '00:50', '00:55'][Math.floor(Math.random() * 4)];  // 随机选择违法状态
    // 备注
    this.form2.remark='备注：' + Math.random().toString(36).substring(2, 15); // 生成随机备注

    // 添加行为记录
    // 车型
    this.form.carTypeId=['1', '2', '3'][Math.floor(Math.random() * 3)];
    // 车身颜色
    this.form.carColor=['红色', '白色', '蓝色', '黄色'][Math.floor(Math.random() * 4)];
    // 嫌疑程度
    this.form.degreeSuspicion=`(${Math.floor(Math.random() * (100 - 10 + 1)) + 10},${Math.floor(Math.random() * (100 - 10 + 1)) + 10})`;
    // 检测点
    this.form.detectionId=['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'][Math.floor(Math.random() * 10)];
    // 监测区域
    this.form.districtId=['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'][Math.floor(Math.random() * 10)];
    // 行驶方向
    this.form.drivingDirection=['0', '1'][Math.floor(Math.random() * 2)];
    // 违法状态
    this.form.illegalStatus=['1', '2', '3'][Math.floor(Math.random() * 3)];  // 随机选择违法状态
    // 状态
    this.form.status  =['0', '1'][Math.floor(Math.random() * 2)];  // 随机选择违法状态
    // 备注
    this.form.remark='备注：' + Math.random().toString(36).substring(5, 20); // 生成随机备注
    
  }
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
