<template>
  <div class="app-container">
    <el-card>
      <div slot="header" class="clearfix">
        <span>因子管理</span>
        <el-button style="float: right;" type="primary" size="small" @click="handleAdd">新增因子</el-button>
      </div>

      <el-table :data="factorList" border>
        <el-table-column prop="factorkey" label="因子键" align="center" />
        <el-table-column prop="factorvalue" label="因子值" align="center" />
        <el-table-column prop="description" label="描述" align="center" />
        <el-table-column label="操作" align="center" width="260">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" @click="handleTrigger(scope.row.factorKey)">触发值管理</el-button>
            <el-button size="mini" type="text" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px">
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="因子键">
          <el-input v-model="form.factorKey" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="因子值">
          <el-input-number v-model="form.factorValue" :min="0" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="open=false">取 消</el-button>
        <el-button type="primary" @click="submitForm">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
//import { listFactor, addFactor, updateFactor, delFactor } from '@/api/factor'
import request from '@/utils/request'

// 查询因子列表
export function listFactor(query) {
  return request({
    url: '/factor/list',
    method: 'get',
    params: query
  })
}

// 获取因子详细
export function getFactor(id) {
  return request({
    url: '/factor/' + id,
    method: 'get'
  })
}

// 新增因子
export function addFactor(data) {
  return request({
    url: '/factor',
    method: 'post',
    data: data
  })
}

// 修改因子
export function updateFactor(data) {
  return request({
    url: '/factor',
    method: 'put',
    data: data
  })
}

// 删除因子
export function delFactor(ids) {
  return request({
    url: '/factor/' + ids,
    method: 'delete'
  })
}

export default {
  name: 'Factor',
  data() {
    return {
      factorList: [],
      open: false,
      title: '',
      form: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      listFactor().then(res => {
        this.factorList = res.rows
        console.log('因子列表:', this.factorList)
      })
    },
    handleAdd() {
      this.form = {}
      this.title = '新增因子'
      this.open = true
    },
    handleEdit(row) {
      this.form = Object.assign({}, row)
      this.title = '编辑因子'
      this.open = true
    },
    handleTrigger(factorKey) {
      this.$router.push({ path: '/factor/trigger', query: { factorKey } })
    },
    handleDelete(row) {
      this.$confirm('确定删除该因子吗？', '提示').then(() => {
        delFactor(row.id).then(() => {
          this.getList()
        })
      })
    },
    submitForm() {
      if (this.form.id) {
        updateFactor(this.form).then(() => {
          this.open = false
          this.getList()
        })
      } else {
        addFactor(this.form).then(() => {
          this.open = false
          this.getList()
        })
      }
    }
  }
}
</script>
