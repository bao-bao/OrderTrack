<template>
    <div class="login-wrap">
        <div class="ms-title">后台管理系统</div>
        <div class="ms-login">
            <el-form :model="ruleForm" :rules="rules" ref="ruleForm" label-width="0px" class="demo-ruleForm">
                <el-form-item prop="username">
                    <el-input v-model="ruleForm.username" placeholder="username"></el-input>
                </el-form-item>
                <el-form-item prop="password">
                    <el-input type="password" placeholder="password" v-model="ruleForm.password" @keyup.enter.native="submitForm('ruleForm')"></el-input>
                </el-form-item>
                <div class="login-btn">
                    <el-button type="primary" :loading="isLoading" @click="submitForm('ruleForm')">登录</el-button>
                </div>
                <p style="font-size:12px;line-height:30px;color:#999;">Tips : 默认密码123456</p>
            </el-form>
        </div>
    </div>
</template>

<script>
export default {
  data: function() {
    return {
      isLoading: false,
      ruleForm: {
        username: "",
        password: ""
      },
      rules: {
        username: [
          { required: true, message: "请输入用户名", trigger: "blur" }
        ],
        password: [{ required: true, message: "请输入密码", trigger: "blur" }]
      }
    };
  },
  methods: {
    submitForm(formName) {
      const self = this;
      self.isLoading = true;
      self.$refs[formName].validate(valid => {
        if (valid) {
          self.$api
            .post(self.$url.login, self.ruleForm)
            .then(res => {
              let data = res.data;
              if (data.code == "SUCCESS") {
                localStorage.setItem(
                  "ms_user",
                  JSON.stringify(data.currentUser)
                );
                self.$router.push("/index");
                this.$message({
                  message: "登录成功",
                  type: "success"
                });
              } else if (data.code == "INVALID_USERNAME") {
                this.$message({
                  message: "用户名不存在",
                  type: "error"
                });
              } else if (data.code == "WRONG_PASSWORD") {
                this.$message({
                  message: "密码错误",
                  type: "error"
                });
              }
            })
            .catch(err => {
              this.$message({
                message: err.data.status + ": " + err.data.error,
                type: "error"
              });
            });
        } else {
          this.$message({
            message: "参数错误",
            type: "error"
          });
          return false;
        }
      });
      self.isLoading = false;
    }
  }
};
</script>

<style scoped>
.login-wrap {
  position: relative;
  width: 100%;
  height: 100%;
}
.ms-title {
  position: absolute;
  top: 50%;
  width: 100%;
  margin-top: -230px;
  text-align: center;
  font-size: 30px;
  color: #fff;
}
.ms-login {
  position: absolute;
  left: 50%;
  top: 50%;
  width: 300px;
  height: 160px;
  margin: -150px 0 0 -190px;
  padding: 40px;
  border-radius: 5px;
  background: #fff;
}
.login-btn {
  text-align: center;
}
.login-btn button {
  width: 100%;
  height: 36px;
}
</style>