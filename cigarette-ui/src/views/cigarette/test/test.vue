<template>
    <div>
      <div>
        <el-tree
          :data="treeData"
          node-key="nodeKey" 
          default-expand-all
          :props="defaultProps"
          @node-click="handleNodeClick">
        </el-tree>
      </div>
      <div>
        <el-table v-loading="loading" :data="cameraList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="摄像头id" align="center" prop="cameraId" />
          <el-table-column label="摄像头ip" align="center" prop="cameraIp" />
          <el-table-column label="摄像头型号" align="center" prop="cameraModel" />
          <el-table-column label="摄像头厂商" align="center" prop="cameraManufacturer" />
          <!-- 其他列... -->
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['camera:camera:edit']">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['camera:camera:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />
      </div>
    </div>
  </template>
  
  <script>
  import { listDistrict } from "@/api/cigarette/detection/district";
  import { listDetection } from "@/api/cigarette/detection/detection";
  import { listCamera } from "@/api/cigarette/detection/camera";
  
  export default {
    name: "test",
    dicts: ['sys_normal_disable', 'tob_driving_irection', 'tob_camera_type', 'tob_del_flag', 'tob_connection_type', 'tob_night_vision', 'tob_frame_rate', 'tob_resolution_ratio'],
    data() {
      return {
        ids: [],
        single: true,
        multiple: true,
        districts: [],
        detections: [],
        treeData: [],
        total: 0,
        cameraList: [],
        defaultProps: {
          children: 'children',
          label: 'label'
        },
        loading: true,
        queryParams: {
          detectionId: null,
          districtId: null,
        },
      };
    },
    created() {
      this.getList();
    },
    methods: {
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
    }
  };
  </script>
  
  <style scoped>
  /* 样式可以根据需要调整 */
  </style>
  