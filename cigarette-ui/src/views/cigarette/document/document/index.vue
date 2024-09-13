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
          <el-form-item label="原文件名" prop="fileName">
            <el-input v-model="queryParams.fileName" placeholder="请输入原文件名" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="上传后文件名" prop="uploadFileName">
            <el-input v-model="queryParams.uploadFileName" placeholder="请输入上传后文件名" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="转换pdf路径" prop="pdfPath">
            <el-input v-model="queryParams.pdfPath" placeholder="请输入转换pdf路径" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="转换pdf文件名" prop="pdfName">
            <el-input v-model="queryParams.pdfName" placeholder="请输入转换pdf文件名" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="预览URL" prop="previewUrl">
            <el-input v-model="queryParams.previewUrl" placeholder="请输入预览URL" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="监测区域id" prop="detectionId">
            <el-input v-model="queryParams.detectionId" placeholder="请输入监测区域id" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="地区id" prop="districtId">
            <el-input v-model="queryParams.districtId" placeholder="请输入地区id" clearable
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
              v-hasPermi="['cigarette/document:document:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
              v-hasPermi="['cigarette/document:document:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
              v-hasPermi="['cigarette/document:document:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
              v-hasPermi="['cigarette/document:document:export']">导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="documentList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="主键" align="center" prop="id" />
          <el-table-column label="原文件名" align="center" prop="fileName" />
          <el-table-column label="文件后缀" align="center" prop="fileType" />
          <el-table-column label="上传后文件路径" align="center" prop="uploadPath" />
          <el-table-column label="上传后文件名" align="center" prop="uploadFileName" />
          <el-table-column label="转换pdf路径" align="center" prop="pdfPath" />
          <el-table-column label="转换pdf文件名" align="center" prop="pdfName" />
          <el-table-column label="文档类型" align="center" prop="documentType" />
          <el-table-column label="备注" align="center" prop="remark" />
          <el-table-column label="预览URL" align="center" prop="previewUrl" />
          <el-table-column label="监测区域id" align="center" prop="detectionId" />
          <el-table-column label="地区id" align="center" prop="districtId" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['cigarette/document:document:edit']">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                v-hasPermi="['cigarette/document:document:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize"
          @pagination="getList" />
      </el-col>
    </el-row>
    <!-- 添加或修改文档对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="50%" append-to-body style="margin-top: 100px;">
      <el-form ref="form" :model="form" :rules="rules" label-width="140px">
        <el-row>
          <el-col :sapn="12">
            <el-form-item label="原文件名" prop="fileName">
          <el-input v-model="form.fileName" placeholder="请输入原文件名" />
        </el-form-item>
          </el-col>
          <el-col :sapn="12">
            <el-form-item label="上传后文件名" prop="uploadFileName">
          <el-input v-model="form.uploadFileName" placeholder="请输入上传后文件名" />
        </el-form-item>
          </el-col>
        </el-row>


        <el-form-item label="上传后文件路径" prop="uploadPath">
          <file-upload v-model="form.uploadPath" />
        </el-form-item>

        
        <el-row>
          <el-col :sapn="12">
            <el-form-item label="转换pdf路径" prop="pdfPath">
          <el-input v-model="form.pdfPath" placeholder="请输入转换pdf路径" />
        </el-form-item>
          </el-col>
          <el-col :sapn="12">
            <el-form-item label="转换pdf文件名" prop="pdfName">
          <el-input v-model="form.pdfName" placeholder="请输入转换pdf文件名" />
        </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :sapn="12">
            <el-form-item label="预览URL" prop="previewUrl">
          <el-input v-model="form.previewUrl" placeholder="请输入预览URL" />
        </el-form-item>
          </el-col>
          <el-col :sapn="12">
            <el-form-item label="转换pdf文件名" prop="pdfName">
          <el-input v-model="form.pdfName" placeholder="请输入转换pdf文件名" />
        </el-form-item>
          </el-col>
        </el-row>


        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>

        <el-form-item label="监测区域id" prop="detectionId">
          <el-input v-model="form.detectionId" placeholder="请输入监测区域id" />
        </el-form-item>
        <el-form-item label="地区id" prop="districtId">
          <el-input v-model="form.districtId" placeholder="请输入地区id" />
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
import { listDocument, getDocument, delDocument, addDocument, updateDocument } from "@/api/cigarette/document/document";
import { listDistrict } from "@/api/cigarette/detection/district";
import { listDetection } from "@/api/cigarette/detection/detection";

export default {
  name: "Document",
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
      // 文档表格数据
      documentList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        fileName: null,
        fileType: null,
        uploadPath: null,
        uploadFileName: null,
        pdfPath: null,
        pdfName: null,
        documentType: null,
        previewUrl: null,
        detectionId: null,
        districtId: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        fileName: [
          { required: true, message: "原文件名不能为空", trigger: "blur" }
        ],
        fileType: [
          { required: true, message: "文件后缀不能为空", trigger: "change" }
        ],
        uploadPath: [
          { required: true, message: "上传后文件路径不能为空", trigger: "blur" }
        ],
        uploadFileName: [
          { required: true, message: "上传后文件名不能为空", trigger: "blur" }
        ],
        pdfPath: [
          { required: true, message: "转换pdf路径不能为空", trigger: "blur" }
        ],
        pdfName: [
          { required: true, message: "转换pdf文件名不能为空", trigger: "blur" }
        ],
        documentType: [
          { required: true, message: "文档类型不能为空", trigger: "change" }
        ],
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ],
        createBy: [
          { required: true, message: "创建人不能为空", trigger: "blur" }
        ],
        updateTime: [
          { required: true, message: "更新时间不能为空", trigger: "blur" }
        ],
        updateBy: [
          { required: true, message: "更新人不能为空", trigger: "blur" }
        ],
        detectionId: [
          { required: true, message: "监测区域id不能为空", trigger: "blur" }
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
    async getList() {
      try {
        const districtsResponse = await listDistrict();
        this.districts = districtsResponse.data;

        const detectionsResponse = await listDetection();
        this.detections = detectionsResponse.rows;

        listDocument(this.queryParams).then(response => {
          this.documentList = response.rows;
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
    /** 查询文档列表 */
    // getList() {
    //   this.loading = true;
    //   listDocument(this.queryParams).then(response => {
    //     this.documentList = response.rows;
    //     this.total = response.total;
    //     this.loading = false;
    //   });
    // },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        fileName: null,
        fileType: null,
        uploadPath: null,
        uploadFileName: null,
        pdfPath: null,
        pdfName: null,
        documentType: null,
        createTime: null,
        createBy: null,
        updateTime: null,
        updateBy: null,
        remark: null,
        previewUrl: null,
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
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加文档";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getDocument(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改文档";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateDocument(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addDocument(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除文档编号为"' + ids + '"的数据项？').then(function () {
        return delDocument(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cigarette/document/document/export', {
        ...this.queryParams
      }, `document_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>