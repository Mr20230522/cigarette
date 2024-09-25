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
      <!-- 案件数据 -->
      <el-col :span="20" :xs="24">
        <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
          label-width="68px">
          <el-form-item label="案件代码" prop="caseCode">
            <el-input v-model="queryParams.caseCode" placeholder="请输入案件代码" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="案件名称" prop="caseName">
            <el-input v-model="queryParams.caseName" placeholder="请输入案件名称" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>


          <el-form-item label="案值" prop="caseValue">
            <el-input v-model="queryParams.caseValue" placeholder="请输入案值" clearable @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="案发地点" prop="incidentLocation">
            <el-input v-model="queryParams.incidentLocation" placeholder="请输入案发地点" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="检查人员姓名" prop="staffName">
            <el-input v-model="queryParams.staffrName" placeholder="请输入案件检查人员姓名" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="录入人员姓名" prop="caseUserName">
            <el-input v-model="queryParams.caseUserName" placeholder="请输入案件录入人员姓名" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="违法人员姓名" prop="driverName">
            <el-input v-model="queryParams.driverName" placeholder="请输入违法人员姓名" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="违法车辆车牌" prop="licensePlate">
            <el-input v-model="queryParams.licensePlate" placeholder="请输入违法车辆车牌" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="主要案由" prop="mainCharge">
            <el-input v-model="queryParams.mainCharge" placeholder="请输入主要案由" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="次要案由" prop="secondaryCharge">
            <el-input v-model="queryParams.secondaryCharge" placeholder="请输入次要案由" clearable
              @keyup.enter.native="handleQuery" />
          </el-form-item>
          <el-form-item label="案件状态" prop="caseStatus">
            <el-select v-model="queryParams.caseStatus" placeholder="请选择案件状态" clearable>
              <el-option v-for="dict in dict.type.tob_case_status" :key="dict.value" :label="dict.label"
                :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="案件来源" prop="caseSource">
            <el-select v-model="queryParams.caseSource" placeholder="请选择案件来源" clearable>
              <el-option v-for="dict in dict.type.tob_case_source" :key="dict.value" :label="dict.label"
                :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="案件类型" prop="caseType">
            <el-select v-model="queryParams.caseType" placeholder="请选择案件类型" clearable>
              <el-option v-for="dict in dict.type.tob_case_type" :key="dict.value" :label="dict.label"
                :value="dict.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="严重程度" prop="degreeOfCase">
            <el-select v-model="queryParams.degreeOfCase" placeholder="请选择案件严重程度" clearable>
              <el-option v-for="dict in dict.type.tob_degree_case" :key="dict.value" :label="dict.label"
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
              v-hasPermi="['case:caseInformation:add']">新增</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate"
              v-hasPermi="['case:caseInformation:edit']">修改</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete"
              v-hasPermi="['case:caseInformation:remove']">删除</el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button type="warning" plain icon="el-icon-download" size="mini" @click="handleExport"
              v-hasPermi="['case:caseInformation:export']">导出</el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="caseInformationList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center" />
          <el-table-column label="案件编号" align="center" prop="caseId" />
          <el-table-column label="案件代码" align="center" prop="caseCode" width="120" />
          <el-table-column label="案件名称" align="center" prop="caseName" width="120" />
          <el-table-column label="立案时间" align="center" prop="caseStartDate" width="120">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.caseStartDate, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="结案时间" align="center" prop="caseEndDate" width="120">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.caseEndDate, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="案值" align="center" prop="caseValue" width="100" />
          <el-table-column label="案发地点" align="center" prop="incidentLocation" width="140" />
          <el-table-column label="违法人员姓名" align="center" prop="driverName" width="140" />
          <el-table-column label="车辆行为编号" align="center" prop="behaviorId" width="140" />
          <el-table-column label="违法车辆车牌" align="center" prop="licensePlate" width="140" />
          <el-table-column label="案件来源" align="center" prop="caseSource" width="140">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_case_source" :value="scope.row.caseSource" />
            </template>
          </el-table-column>
          <el-table-column label="案件类型" align="center" prop="caseType" width="140">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_case_type" :value="scope.row.caseType" />
            </template>
          </el-table-column>
          <el-table-column label="案件状态" align="center" prop="caseStatus" width="140">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_case_status" :value="scope.row.caseStatus" />
            </template>
          </el-table-column>
          <el-table-column label="案件严重程度" align="center" prop="degreeOfCase" width="140">
            <template slot-scope="scope">
              <dict-tag :options="dict.type.tob_degree_case" :value="scope.row.degreeOfCase" />
            </template>
          </el-table-column>
          <el-table-column label="主要案由" align="center" prop="mainCharge" width="180" />
          <el-table-column label="次要案由" align="center" prop="secondaryCharge" width="180" />
          <el-table-column label="案情描述" align="center" prop="mainCaseDetails" width="220" />
          <el-table-column label="录入日期" align="center" prop="entryDate" width="120">
            <template slot-scope="scope">
              <span>{{ parseTime(scope.row.entryDate, '{y}-{m}-{d}') }}</span>
            </template>
          </el-table-column>
          <el-table-column label="监测点" align="center" prop="detectionId">
            <template slot-scope="scope">
              {{ getDetectionName(scope.row.detectionId) }}
            </template>
          </el-table-column>
          <el-table-column label="备注" align="center" prop="remark" width="100" />
          <el-table-column label="检查人员编号" align="center" prop="staffId" width="100" />
          <el-table-column label="检查人员姓名" align="center" prop="staffName" width="100" />
          <el-table-column label="录入人员编号" align="center" prop="caseUserId" width="100" />
          <el-table-column label="录入人员姓名" align="center" prop="caseUserName" width="100" />
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
            <template slot-scope="scope">
              <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)"
                v-hasPermi="['case:caseInformation:edit']">修改</el-button>
              <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)"
                v-hasPermi="['case:caseInformation:remove']">删除</el-button>
            </template>
          </el-table-column>
        </el-table>

        <pagination v-show="total > 0" :total="total" :page.sync="queryParams.pageNum"
          :limit.sync="queryParams.pageSize" @pagination="getList" />
      </el-col>
    </el-row>
    <!-- 添加或修改案件信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="70%" append-to-body :close-on-click-modal="false">
      <el-row>
        <el-card>
          <el-col :span="16">
            <el-card>
              <el-form ref="form" :model="form" :rules="rules" label-width="110px">
                <el-form-item label="搜索车辆行为">
                  <el-input v-model="searchInputBehavior" @input="filterBehaviors"
                    placeholder="请输入车辆行为编号、车辆编号、驾驶人员编号、车牌编号、驾驶人员姓名" clearable suffix-icon="el-icon-search"></el-input>
                  <el-scrollbar wrap-class="scrollbar-wrapper" style="max-height: 'auto';">
                    <el-card class="user-list">
                      <el-row v-for="(behavior, index) in filteredBehaviors" :key="index" class="user-info"
                        :class="{ 'bg-color': index % 2 === 1, 'selected': behavior === selectedBehavior }">
                        <el-col :span="24">
                          <span @click="selectBehavior(behavior)" class="label" style="cursor:pointer;">行为ID:{{
                            behavior.behaviorId }}&nbsp;&nbsp;车辆编号:{{ behavior.carId }}&nbsp;&nbsp;车牌编号:{{
                            behavior.licensePlate }}&nbsp;&nbsp;驾驶员ID:{{ behavior.driverId }}&nbsp;&nbsp;驾驶员姓名:{{
                            behavior.driverName }}
                          </span>
                        </el-col>
                      </el-row>
                    </el-card>
                  </el-scrollbar>
                </el-form-item>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="违法人员姓名" prop="driverName">
                      <el-input :disabled="true" v-model="form.driverName" placeholder="违法人员姓名" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="违法车牌编号" prop="licensePlate">
                      <el-input :disabled="true" v-model="form.licensePlate" placeholder="违法车辆车牌编号" />
                    </el-form-item>
                  </el-col>

                </el-row>
              </el-form>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card>
              <el-form ref="form" :model="form" :rules="rules" label-width="110px">
                <el-form-item label="案件代码" prop="caseCode">
                  <el-input v-model="form.caseCode" clearable placeholder="请输入案件代码" />
                </el-form-item>
                <el-form-item label="案件名称" prop="caseName">
                  <el-input v-model="form.caseName" clearable placeholder="请输入案件名称" />
                </el-form-item>
                <el-form-item label="立案时间" prop="caseStartDate">
                  <el-date-picker clearable v-model="form.caseStartDate" type="date" value-format="yyyy-MM-dd"
                    placeholder="请选择立案时间">
                  </el-date-picker>
                </el-form-item>
                <el-form-item label="结案时间" prop="caseEndDate">
                  <el-date-picker clearable v-model="form.caseEndDate" type="date" value-format="yyyy-MM-dd"
                    placeholder="请选择结案时间">
                  </el-date-picker>
                </el-form-item>
                <el-form-item label="案值" prop="caseValue">
                  <el-input v-model="form.caseValue" clearable placeholder="请输入案值" />
                </el-form-item>
                <el-form-item label="录入日期" prop="entryDate">
                  <el-date-picker clearable v-model="form.entryDate" type="date" value-format="yyyy-MM-dd"
                    placeholder="请选择录入日期">
                  </el-date-picker>
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>
        </el-card>
      </el-row>
      <el-row>
        <el-card>
          <el-col :span="16">
            <el-card>
              <el-form ref="form" :model="form" :rules="rules" label-width="110px">
                <el-form-item label="搜索检测人员">
                  <el-input v-model="searchInput1" @input="filterUsers1" placeholder="请输入检测人员名称、编号 或电话号码" clearable
                    suffix-icon="el-icon-search"></el-input>
                  <el-scrollbar wrap-class="scrollbar-wrapper" style="max-height: 'auto';">
                    <el-card class="user-list">
                      <el-row v-for="(user1, index) in filteredUsers1" :key="index" class="user-info"
                        :class="{ 'bg-color': index % 2 === 1, 'selected': user1 === selectedUser1 }">
                        <el-col :span="24">
                          <span @click="selectUser1(user1)" class="label" style="cursor:pointer;">用户ID:{{ user1.id
                            }}&nbsp;&nbsp;用户名称:{{ user1.username }}&nbsp;&nbsp;电话号码:{{ user1.phonenumber }}
                          </span>
                        </el-col>
                      </el-row>
                    </el-card>
                  </el-scrollbar>
                </el-form-item>
                <el-row>
                  <el-col :span="12">
                    <el-form-item label="检测人员编号" prop="staffId">
                      <el-input :disabled="true" v-model="form.staffId" placeholder="案件检测人员编号" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="检测人员姓名" prop="name1">
                      <el-input :disabled="true" v-model="form.name1" placeholder="案件检测人员姓名" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="人员电话号码" prop="phone1">
                  <el-input :disabled="true" v-model="form.phone1" placeholder="案件检测人员电话号码" />
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card>
              <el-form ref="form" :model="form" :rules="rules" label-width="110px">
                <el-form-item label="案件状态" prop="caseStatus">
                  <el-select v-model="form.caseStatus" placeholder="请选择案件状态" clearable>
                    <el-option v-for="dict in dict.type.tob_case_status" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </el-select>
                </el-form-item>
                <el-form-item label="案件来源" prop="caseSource">
                  <el-select v-model="form.caseSource" placeholder="请选择案件来源" clearable>
                    <el-option v-for="dict in dict.type.tob_case_source" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </el-select>
                </el-form-item>

                <el-form-item label="案件类型" prop="caseType">
                  <el-select v-model="form.caseType" placeholder="请选择案件类型" clearable>
                    <el-option v-for="dict in dict.type.tob_case_type" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </el-select>
                </el-form-item>
                <el-form-item label="案件严重程度" prop="degreeOfCase">
                  <el-select v-model="form.degreeOfCase" placeholder="请选择案件严重程度" clearable>
                    <el-option v-for="dict in dict.type.tob_degree_case" :key="dict.value" :label="dict.label"
                      :value="dict.value" />
                  </el-select>
                </el-form-item>

              </el-form>
            </el-card>
          </el-col>
        </el-card>
      </el-row>


      <el-row>
        <el-card>
          <el-col :span="16">
            <el-card>
              <el-form ref="form" :model="form" :rules="rules" label-width="110px">
                <el-form-item label="搜索录入人员">
                  <el-input v-model="searchInput" @input="filterUsers" placeholder="请输入录入人员名称、编号 或电话号码" clearable
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
                    <el-form-item label="录入人员编号" prop="caseUserId">
                      <el-input :disabled="true" v-model="form.caseUserId" placeholder="案件录入人员编号" />
                    </el-form-item>
                  </el-col>
                  <el-col :span="12">
                    <el-form-item label="录入人员姓名" prop="name">
                      <el-input :disabled="true" v-model="form.name" placeholder="案件录入人员姓名" />
                    </el-form-item>
                  </el-col>
                </el-row>
                <el-form-item label="人员电话号码" prop="phone">
                  <el-input :disabled="true" v-model="form.phone" placeholder="案件录入人员电话号码" />
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card>
              <el-form ref="form" :model="form" :rules="rules" label-width="110px">
                <!-- <el-form-item label="案发地点" prop="incidentLocation">
                  <el-input clearable v-model="form.incidentLocation" type="textarea"
                    :autosize="{ minRows: 3, maxRows: 5}" placeholder="请输入案发地点" />
                </el-form-item> -->
                <el-form-item label="检测点" prop="detectionId">
                  <el-select v-model="form.detectionId" placeholder="请选择所管理的监测点" filterable
                    @change="handleDetectionChange">
                    <el-option v-for="item in detectionOptions" :key="item.detectionId"
                      :label="getDetectionName(item.detectionId)" :value="item.detectionId"></el-option>
                  </el-select>
                </el-form-item>
                <el-form-item label="案发地点" prop="incidentLocation">
                  <el-cascader v-model="selectedRegion" :options="pcaTextArr" placeholder="请选择地区" clearable
                    @change="handleRegionChange" />
                  <el-input v-model="form.incidentDetail" placeholder="请输入详细地址" clearable />
                </el-form-item>
                <el-form-item label="备注" prop="remark">
                  <el-input clearable v-model="form.remark" type="textarea" :autosize="{ minRows: 3, maxRows: 5 }"
                    placeholder="请输入备注" />
                </el-form-item>
              </el-form>
            </el-card>
          </el-col>
        </el-card>
      </el-row>
      <el-row>
        <el-card>
          <el-form ref="form" :model="form" :rules="rules" label-width="110px">
            <el-row>
              <el-col :span="12">
                <el-form-item label="主要案由" prop="mainCharge">
                  <el-input v-model="form.mainCharge" type="textarea" :autosize="{ minRows: 2, maxRows: 5 }"
                    placeholder="请输入主要案由" />
                </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="次要案由" prop="secondaryCharge">
                  <el-input v-model="form.secondaryCharge" type="textarea" :autosize="{ minRows: 2, maxRows: 5 }"
                    placeholder="请输入次要案由" />
                </el-form-item>
              </el-col>
            </el-row>
            <el-row>
              <div style="margin-left: 48%;margin-bottom: 10px;">
                <el-button type="primary" @click="generateCaseDetails(form)">生 成</el-button>
              </div>
            </el-row>

            <el-form-item label="案情描述" prop="mainCaseDetails">
              <el-input v-model="form.mainCaseDetails" type="textarea" :autosize="{ minRows: 3, maxRows: 6 }"
                placeholder="请输入内容" />
            </el-form-item>
          </el-form>
        </el-card>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCaseInformation,
  listCaseInformationVo,
  getCaseInformation,
  delCaseInformation,
  addCaseInformation,
  updateCaseInformation
} from "@/api/cigarette/case/caseInformation";
import {
  listUser
} from "@/api/system/user";
import {
  listVehicleBehaviorVo
} from "@/api/cigarette/vehicle/vehicleBehavior";
import { listDistrict } from "@/api/cigarette/detection/district";
import { listDetection } from "@/api/cigarette/detection/detection";
import { pcaTextArr } from "element-china-area-data";

export default {
  name: "CaseInformation",
  dicts: ['tob_case_status', 'tob_case_source', 'tob_case_type', 'tob_degree_case'],
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
      // 案件信息表格数据
      caseInformationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      pcaTextArr, // 地区数据
      selectedRegion: [], // 选中的地区
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        caseCode: null,
        caseName: null,
        caseStartDate: null,
        caseEndDate: null,
        caseValue: null,
        incidentLocation: null,// 案发地点
        incidentDetail: null, // 详细地址
        behaviorId: null,
        caseSource: null,
        caseType: null,
        caseStatus: null,
        mainCharge: null,
        secondaryCharge: null,
        mainCaseDetails: null,
        entryDate: null,
        caseUserId: null,
        licensePlate: null,
        driverName: null,
      },
      // 表单参数
      form: {},
      // 存储搜索信息
      searchInput: '',
      // 存储用户信息
      userIdList: [],
      // 存储根据搜索条件过滤后的用户列表数据
      filteredUsers: [],
      // 存储所选用户信息
      selectedUser: null,
      // 存储搜索信息
      searchInput1: '',
      // 存储根据搜索条件过滤后的用户列表数据
      filteredUsers1: [],
      // 存储所选用户信息
      selectedUser1: null,
      // 存储搜索信息
      searchInputBehavior: '',
      // 存储用户信息
      behaviorIdList: [],
      // 存储根据搜索条件过滤后的用户列表数据
      filteredBehaviors: [],
      // 存储所选用户信息
      selectedBehavior: null,
      districts: [],
      detections: [],
      detectionOptions: [],    //json数组，用于存储检测点选项
      treeData: [],
      defaultProps: {
        children: "children",
        label: "label"
      },
      // 表单校验
      rules: {
        caseCode: [{
          required: true,
          message: "案件代码不能为空",
          trigger: "blur"
        }],
        caseName: [{
          required: true,
          message: "案件名称不能为空",
          trigger: "blur"
        }],
        caseStartDate: [{
          required: true,
          message: "立案时间不能为空",
          trigger: "blur"
        }],
        caseEndDate: [{
          required: true,
          message: "结案时间不能为空",
          trigger: "blur"
        }],
        caseValue: [{
          required: true,
          message: "案值不能为空",
          trigger: "blur"
        }],
        incidentLocation: [{
          required: true,
          message: "案发地点不能为空",
          trigger: "blur"
        }],
        behaviorId: [{
          required: true,
          message: "违法车辆id不能为空",
          trigger: "blur"
        }],
        caseSource: [{
          required: true,
          message: "案件来源ID不能为空",
          trigger: "change"
        }],
        caseType: [{
          required: true,
          message: "案件类型不能为空",
          trigger: "change"
        }],
        caseStatus: [{
          required: true,
          message: "案件状态不能为空",
          trigger: "change"
        }],
        mainCharge: [{
          required: true,
          message: "主要案由不能为空",
          trigger: "blur"
        }],
        secondaryCharge: [{
          required: true,
          message: "次要案由不能为空",
          trigger: "blur"
        }],
        mainCaseDetails: [{
          required: true,
          message: "案情描述不能为空",
          trigger: "blur"
        }],
        entryDate: [{
          required: true,
          message: "录入日期不能为空",
          trigger: "blur"
        }],
      }
    };
  },
  created() {
    this.getList();
    this.loadDetectionOptions(); // 加载检测点选项
    this.loadDistrictOptions(); // 加载地区选项
  },
  methods: {
    handleRegionChange(value) {
      // 当地区选择变化时，更新 form.incidentLocation
      this.form.incidentLocation = value.join(' ');
    },

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

        listCaseInformationVo(this.queryParams).then(response => {
          this.caseInformationList = response.rows;
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
        this.caseInformationList = [];
        detectionIds.forEach(async (id) => {
          this.queryParams.detectionId = id;
          const response = await listCaseInformationVo(this.queryParams);
          this.caseInformationList.push(...response.rows);
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
    /** 查询案件信息列表 */
    // getList() {
    //   this.loading = true;
    //   listCaseInformationVo(this.queryParams).then(response => {
    //     this.caseInformationList = response.rows;
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
        caseId: null,
        caseCode: null,
        caseName: null,
        caseStartDate: null,
        caseEndDate: null,
        caseValue: null,
        incidentLocation: null,
        behaviorId: null,
        caseSource: null,
        caseType: null,
        caseStatus: null,
        mainCharge: null,
        secondaryCharge: null,
        mainCaseDetails: null,
        entryDate: null,
        delFlag: null,
        remark: null,
        caseUserId: null,
        staffId: null,
        degreeOfCase: null,
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
      this.ids = selection.map(item => item.caseId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      listVehicleBehaviorVo({
        pageNum: null,
        pageSize: 100000
      }).then(response => {
        // 获取到用户信息后，保存原始用户列表数据
        this.BehaviorIdList = response.rows.map(behavior => {
          return {
            behaviorId: behavior.behaviorId,
            driverId: behavior.driverId,
            carId: behavior.carId,
            driverName: behavior.driverName,
            licensePlate: behavior.licensePlate
          };
        });
      }).catch(error => {
        console.error('Failed to fetch user list:', error);
      });
      listUser({
        pageNum: null,
        pageSize: 100000
      }).then(response => {
        // 获取到用户信息后，保存原始用户列表数据
        this.userIdList = response.rows.map(user => {
          return {
            id: user.userId,
            username: user.nickName,
            phonenumber: user.phonenumber
          };
        });
      }).catch(error => {
        console.error('Failed to fetch user list:', error);
      });
      this.open = true;
      this.title = "添加案件信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const caseId = row.caseId || this.ids
      getCaseInformation(caseId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改案件信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          // 再次确保在提交前合并地区和详细地址
          this.form.incidentLocation = `${this.selectedRegion.join(' ')} ${this.form.incidentDetail}`;
          console.log("this.form");
          console.log(this.form);
          if (this.form.caseId != null) {
            updateCaseInformation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCaseInformation(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.caseId != null) {
            updateCaseInformation(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addCaseInformation(this.form).then(response => {
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
      const caseIds = row.caseId || this.ids;
      this.$modal.confirm('是否确认删除案件信息编号为"' + caseIds + '"的数据项？').then(function () {
        return delCaseInformation(caseIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => { });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('cigarette/case/caseInformation/export', {
        ...this.queryParams
      }, `caseInformation_${new Date().getTime()}.xlsx`)
    },
    // 筛选用户信息
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
      this.$set(this.form, "caseUserId", user.id);
      this.$set(this.form, "name", user.username);
      this.$set(this.form, "phone", user.phonenumber);
    },
    // 筛选用户信息
    filterUsers1() {
      const searchInput1 = this.searchInput1.toLowerCase().trim();
      if (!searchInput1) {
        // 如果搜索条件为空，不显示任何用户
        this.filteredUsers1 = [];
        return;
      }
      this.filteredUsers1 = this.userIdList.filter(user1 => {
        // 在用户名、ID和电话号码中进行搜索匹配
        return (
          user1.username.toLowerCase().includes(searchInput1) ||
          user1.id.toString().includes(searchInput1) ||
          user1.phonenumber.toString().includes(searchInput1)
        );
      }).slice(0, 10);
    },
    // 搜索用户并筛选数据
    selectUser1(user1) {
      // 将所选用户信息存储到 selectedUser1变量中
      this.selectedUser1 = user1;
      // 更新表单数据
      this.$set(this.form, "staffId", user1.id);
      this.$set(this.form, "name1", user1.username);
      this.$set(this.form, "phone1", user1.phonenumber);
    },
    // 筛选行为信息
    filterBehaviors() {
      const searchInputBehavior = this.searchInputBehavior.toString().trim();
      if (!searchInputBehavior) {
        // 如果搜索条件为空，不显示任何用户
        this.filteredBehaviors = [];
        return;
      }
      this.filteredBehaviors = this.BehaviorIdList.filter(behavior => {
        // 在用户名、ID和电话号码中进行搜索匹配
        return (
          behavior.behaviorId.toString().includes(searchInputBehavior) ||
          behavior.driverId.toString().includes(searchInputBehavior) ||
          behavior.carId.toString().includes(searchInputBehavior) ||
          behavior.driverName.toString().includes(searchInputBehavior) ||
          behavior.licensePlate.toString().includes(searchInputBehavior)
        );
      }).slice(0, 10);
    },
    // 搜索行为并筛选数据
    selectBehavior(behavior) {
      // 将所选用户信息存储到 selectedBehavior 变量中
      this.selectedBehavior = behavior;
      // 更新表单数据
      this.$set(this.form, "behaviorId", behavior.behaviorId);
      this.$set(this.form, "licensePlate", behavior.licensePlate);
      this.$set(this.form, "driverName", behavior.driverName);
    },
    /** 提交按钮 */
    generateCaseDetails(from) {
      // 组合成一段话
      const generatedText = `Hello, ${firstName} ${lastName}! Welcome to our service.`;
      this.$set(this.form, "mainCaseDetails", generatedText);
    },
  }
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
