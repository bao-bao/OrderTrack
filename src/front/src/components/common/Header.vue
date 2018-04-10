<template>
    <div class="header">
        <div class="logo">订单管理系统</div>
        <div class="user-info">
            <el-dropdown trigger="click" @command="handleCommand">
                <span class="el-dropdown-link">
                    <img class="user-logo" src="../../../static/img/img.jpg">
                    {{name}}
                </span>
                <el-dropdown-menu slot="dropdown">
                    <el-dropdown-item command="updateSelf">更新密码</el-dropdown-item>
                    <el-dropdown-item command="loginout">退出</el-dropdown-item>
                </el-dropdown-menu>
            </el-dropdown>
        </div>
        
    <el-dialog title="修改密码" :visible="dialogFormVisible" width="30%" :before-close="handleClose">
      <el-form ref="form" :model="form">
        <el-row :gutter="20">
            <el-col :span="22">
              <el-form-item label="旧密码" label-width="80px">
                <el-input v-model="form.oldPassword"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="22">
              <el-form-item label="新密码" label-width="80px">
                <el-input v-model="form.newPassword"></el-input>
              </el-form-item>
            </el-col>
            <el-col :span="22">
              <el-form-item label="确认密码" label-width="80px">
                <el-input v-model="form.confirmPassword"></el-input>
              </el-form-item>
            </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="doUpdate()">确 定</el-button>
      </div>
    </el-dialog>
    </div>
</template>
<script>
export default {
  data() {
    return {
      name: "undefined",
      dialogFormVisible: false,
      form: {
        oldPassword: "",
        newPassword: "",
        confirmPassword: "",
        user: JSON.parse(localStorage.getItem("ms_user"))
      }
    };
  },
  methods: {
    username() {
      let user = JSON.parse(localStorage.getItem("ms_user"));
      let username = user.name;
      this.name = username == null ? "undefined" : username;
    },
    handleCommand(command) {
      if (command == "loginout") {
        localStorage.removeItem("ms_user");
        this.$router.push("/login");
      }
      if (command == "updateSelf") {
        this.dialogFormVisible = true;
      }
    },
    handleClose(done) {
      this.$confirm("确认关闭？")
        .then(_ => {
          this.dialogFormVisible = false;
          done();
        })
        .catch(_ => {});
    },
    doUpdate() {
      let params = this.form;
      this.$api
        .post(this.$url.updateSelf, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "更新成功",
              type: "success"
            });
            this.dialogFormVisible = false;
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
    }
  },
  mounted: function() {
    this.username();
  }
};
</script>
<style scoped>
.header {
  position: relative;
  box-sizing: border-box;
  width: 100%;
  height: 70px;
  font-size: 22px;
  line-height: 70px;
  color: #fff;
}
.header .logo {
  float: left;
  width: 250px;
  text-align: center;
}
.user-info {
  float: right;
  padding-right: 50px;
  font-size: 16px;
  color: #fff;
}
.user-info .el-dropdown-link {
  position: relative;
  display: inline-block;
  padding-left: 50px;
  color: #fff;
  cursor: pointer;
  vertical-align: middle;
}
.user-info .user-logo {
  position: absolute;
  left: 0;
  top: 15px;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.el-dropdown-menu__item {
  text-align: center;
}
</style>
