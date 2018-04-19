<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item><i class="el-icon-date"></i> 职员管理</el-breadcrumb-item>
        <el-breadcrumb-item>职员列表</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div>
      <el-form ref="filterData" :model="filterData">
        <el-row :gutter="0">
          <el-col :span="5">
            <el-form-item label-width="0px">
              <el-input v-model="filterData.name">
                <template slot="prepend">用户名</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="职位" label-width="60px">
              <el-select v-model="filterData.role">
                <el-option v-for="item in roleOption"
                :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" style="margin-left:20px"
            icon="el-icon-search" @click="renderUser()">搜索</el-button>
          </el-col>
          <el-col :span="4" :offset="5" style="text-align:right">
            <el-button type="warning" icon="el-icon-circle-plus" @click="addUser()">新增</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <el-table show-header border :data="tableData" style="width: 100%; text-align: center">
      <el-table-column prop="id" label="#" min-width="50">
        <template slot-scope="scope">
          <el-tag size="medium">{{ scope.row.id }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="name" label="用户名" min-width="180">
        <template slot-scope="scope">{{ scope.row.name }}</template>
      </el-table-column>
      <el-table-column prop="role" label="职位" min-width="150">
        <template slot-scope="scope">{{ showRole(scope.row.role) }}</template>
      </el-table-column>
      <el-table-column prop="isActive" label="是否在职" min-width="100">
        <template slot-scope="scope"><i :class="[ scope.row.isActive ? 'fa fa-check icon-b' : 'fa fa-times icon-a' ]"></i></template>
      </el-table-column>
      <el-table-column label="操作" min-width="140">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="text-align: right; margin-top: 5px">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" 
      :current-page.sync="currentPage" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
      </el-pagination>
    </div>
    <el-dialog :title="dialogFormTitle" :visible="dialogFormVisible" width="30%" :before-close="handleClose">
      <el-form ref="form" :model="form">
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="用户名" label-width="80px">
              <el-input v-model="form.name"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="职位" label-width="80px">
              <el-select v-model="form.role">
                <el-option v-for="item in roleEditOption"
                :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="是否在职" label-width="80px">
              <el-switch v-model="form.isActive"></el-switch>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="isUpdate == true ? doUpdate() : doAdd()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      isUpdate: false,
      dialogFormVisible: false,
      dialogFormTitle: "新增人员信息",
      tableData: [],
      listData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      filterData: {
        name: "",
        role: 0
      },
      roleOption: [
        { label: "全部", value: 0 },
        { label: "管理员", value: 1 },
        { label: "经理", value: 2 },
        { label: "工作班组", value: 3 }
      ],
      roleEditOption: [
        { label: "管理员", value: 1 },
        { label: "经理", value: 2 },
        { label: "工作班组", value: 3 }
      ],
      form: {}
    };
  },
  methods: {
    initPagination(size) {
      this.pageSize = size;
      this.currentPage = 1;
      this.total = this.listData.length;
      this.tableData = this.listData.slice(
        (this.currentPage - 1) * size,
        this.currentPage * size
      );
    },
    handleSizeChange(val) {
      this.initPagination(val);
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      this.tableData = this.listData.slice(
        (val - 1) * this.pageSize,
        val * this.pageSize
      );
    },
    handleEdit(index, row) {
      this.isUpdate = true;
      this.dialogFormTitle = "编辑人员信息";
      this.form = JSON.parse(JSON.stringify(row));
      this.dialogFormVisible = true;
    },
    handleDelete(index, row) {
      this.$confirm("确认删除？")
        .then(_ => {
          this.doDelete(index, row);
        })
        .catch(_ => {});
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          this.dialogFormVisible = false;
          done();
        })
        .catch(_ => {});
    },
    renderUser() {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        background: "rgba(255, 255, 255, 0.7)"
      });
      let params = this.filterData;
      this.$api
        .post(this.$url.getUserList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.listData = data.list;
            this.initPagination(10);
          } else {
            this.$message({
              message: "查询失败， 失败原因：" + data.code,
              type: "error"
            });
          }
          loading.close();
        })
        .catch(err => {
          this.$message({
            message: JSON.stringify(err.data),
            type: "error"
          });
          loading.close();
        });
    },
    addUser() {
      this.isUpdate = false;
      this.dialogFormTitle = "新增人员信息";
      this.form = {
        name: "",
        role: 3,
        isActive: true
      };
      this.dialogFormVisible = true;
    },
    doAdd() {
      this.dialogFormVisible = false;
      let params = this.form;
      this.$api
        .post(this.$url.addUser, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "添加成功",
              type: "success"
            });
            this.renderUser();
          } else if (data == "USERNAME_EXIST") {
            this.$message({
              message: "添加失败， 用户已存在",
              type: "error"
            });
          } else {
            this.$message({
              message: "添加失败， 失败原因：" + data,
              type: "error"
            });
          }
        })
        .catch(err => {
          this.$message({
            message: err,
            type: "error"
          });
        });
    },
    doUpdate() {
      this.dialogFormVisible = false;
      let params = this.form;
      this.$api
        .post(this.$url.updateUser, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "更新成功",
              type: "success"
            });
            this.renderUser();
          } else {
            this.$message({
              message: "更新失败， 失败原因：" + data,
              type: "error"
            });
          }
        })
        .catch(err => {
          this.$message({
            message: err,
            type: "error"
          });
        });
    },
    doDelete(index, row) {
      let params = row;
      this.$api
        .post(this.$url.deleteUser, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "删除成功",
              type: "success"
            });
            this.renderUser();
          } else {
            this.$message({
              message: "删除失败， 失败原因：" + data,
              type: "error"
            });
          }
        })
        .catch(err => {
          this.$message({
            message: err,
            type: "error"
          });
        });
    },
    showRole(role) {
      let label = "";
      this.roleOption.forEach(element => {
        if (element.value == role) {
          label = element.label;
        }
      });
      return label;
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.renderUser();
    });
  }
};
</script>