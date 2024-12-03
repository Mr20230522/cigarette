<template>
    <div class="app-container">




      <!-- 添加或修改关键帧图片对话框 -->
      <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="图片url" prop="image">
            <image-upload v-model="form.image" />
          </el-form-item>
          <el-form-item label="图片名称" prop="imageName">
            <el-input v-model="form.imageName" placeholder="请输入图片名称" />
          </el-form-item>
          <el-form-item label="坐标" prop="coordinate">
            <el-input v-model="form.coordinate" placeholder="请输入坐标" />
          </el-form-item>
          <el-form-item label="搜索用户">
            <el-input v-model="searchInput" @input="filterUsers" placeholder="请输入用户名称、邮箱或电话号码" clearable
              suffix-icon="el-icon-search"></el-input>
            <el-scrollbar wrap-class="scrollbar-wrapper" style="max-height: auto;">
              <el-card class="user-list">
                <el-row v-for="(user, index) in filteredUsers" :key="index" class="user-info"
                  :class="{ 'bg-color': index % 2 === 1, 'selected': user === selectedUser }">
                  <el-col :span="24">
                    <span @click="selectUser(user)" class="label" style="cursor:pointer;">用户名称:{{ user.username
                      }}&nbsp;&nbsp;电话号码:{{ user.phonenumber }}&nbsp;&nbsp;邮箱:{{ user.email }}
                    </span>
                  </el-col>
                </el-row>
              </el-card>
            </el-scrollbar>
          </el-form-item>

        <el-form-item label="违法状态" prop="status">
              <el-select v-model="queryParams.status" placeholder="请选择违法状态" clearable>
                <el-option v-for="dict in dict.type.tob_illegal_status" :key="dict.value" :label="dict.label"
                  :value="dict.value" />
              </el-select>
            </el-form-item>
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
  import { listUser, } from "@/api/system/user";


  export default {
    name: "Picture",
    dicts: ['tob_illegal_status'],

    data() {
      return {
        // 遮罩层
        loading: true,
        // 选中数组
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
        userIdList: [],
        // 存储根据搜索条件过滤后的用户列表数据
        filteredUsers: [],
        // 存储所选用户信息
        selectedUser: null,
        // 是否显示弹出层
        open: false,
        // 查询参数

        // 表单参数
        form: {},


      };
    },
    created() {
      this.getList();
      this.getUserList();
    },
     mounted() {
      this.filteredUsers = [];

    },
    methods: {
      /** 查询关键帧图片列表 */
      getList() {
        this.loading = true;
        listPicture(this.queryParams).then(response => {
          this.pictureList = response.rows;
          this.total = response.total;
          this.loading = false;
        });
      },
      getUserList() {
        this.loading = true;
        listUser({
          pageNum: null,
          pageSize: 100000
        }).then(response => {
          // 获取到用户信息后，保存原始用户列表数据
          this.userIdList = response.rows.map(user => {
            return {
              username: user.nickName,
              phonenumber: user.phonenumber,
              email: user.email,
              staffId:user.userId,
            };
          });
        }).catch(error => {
          console.error('Failed to fetch user list:', error);
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

    // 选择数据化进行数据填充
    filterUsers() {
        const searchInput = this.searchInput.toLowerCase().trim();
        if (!searchInput) {
          // 如果搜索条件为空，不显示任何用户
          this.filteredUsers = [];
          return;
        }
        this.filteredUsers = this.userIdList.filter(user => {
          // 在用户名 邮箱和电话号码中进行搜索匹配
          return (
            user.username.toLowerCase().includes(searchInput) ||
            user.email.toString().includes(searchInput) ||
            user.phonenumber.toString().includes(searchInput)

          );
        }).slice(0, 10);
      },
      // 搜索用户并筛选数据
      selectUser(user) {
        // 将所选用户信息存储到 selectedUser 变量中
        this.selectedUser = user;
        // 更新表单数据
        this.$set(this.form, "email", user.email);
        this.$set(this.form, "username", user.username);
        this.$set(this.form, "phone", user.phonenumber);
        this.form.staffId = user.staffId;
        // 清空搜索输入框和搜索结果
        this.searchInput = '';
        this.filteredUsers = [];
      },
    }
  };
  </script>
