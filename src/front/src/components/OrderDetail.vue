<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>订单管理</el-breadcrumb-item>
        <el-breadcrumb-item>订单详情</el-breadcrumb-item>
        <el-breadcrumb-item>{{orderId}}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div>
      <el-form ref="filterData" :model="filterData">
        <el-row :gutter="20">
          <el-col :span="2">
            <el-form-item label-width="0px">
                <el-button type="primary" icon="el-icon-arrow-left" @click="goBack()">返回</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="20">
            <el-form-item label-width="0px">
              <el-steps :active="order.status" finish-status="success" align-center>
                <el-step title="准备" :description="showDateTime(order.createTime)"></el-step>
                <el-step title="接单" :description="showDateTime(order.takeTime)"></el-step>
                <el-step title="分配" :description="showDateTime(order.divisionTime)"></el-step>
                <el-step title="提货" :description="showDateTime(order.pickUpTime)"></el-step>
                <el-step title="验收" :description="showDateTime(order.checkTime)"></el-step>
                <el-step title="完成" :description="showDateTime(order.finishTime)"></el-step>
              </el-steps>
            </el-form-item>
          </el-col>
          <el-col :span="2" style="text-align:right">
            <el-form-item label-width="0px">
                <el-button type="warning" icon="el-icon-circle-plus" :disabled="isFinish" @click="addOrderDetail()">新增</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <div style="padding-bottom: 20px">
      <el-card>
        <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="客户名称">
              <span>{{ order.customName }}</span>
            </el-form-item>
            <el-form-item label="总价值">
              <span>{{ order.totalPrice }} 元</span>
            </el-form-item>
            <el-form-item label="要求交货期">
              <span>{{ showDate(order.deliveryDate) }}</span>
            </el-form-item>
            <el-form-item label="净重合计">
              <span>{{ order.totalWeight }} kg</span>
            </el-form-item>
            <el-form-item label="内包装数量">
              <span>{{ order.totalSmall }} 件</span>
            </el-form-item>
            <el-form-item label="外包装数量">
              <span>{{ order.totalBig }} 件</span>
            </el-form-item>
          </el-form>
      </el-card>
    </div>
    <el-table show-header border :data="tableData" style="width: 100%; text-align: center">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="内包装规格">
              <span>{{ props.row.innerStandard }}</span>
            </el-form-item>
            <el-form-item label="数量">
              <span>{{ props.row.innerCount }} 件</span>
            </el-form-item>
            <el-form-item label="味道">
              <span>{{ props.row.smell }}</span>
            </el-form-item>
            <el-form-item label="外包装规格">
              <span>{{ props.row.outerStandard }}</span>
            </el-form-item>
            <el-form-item label="件数">
              <span>{{ props.row.outerCount }} 件</span>
            </el-form-item>
            <el-form-item label="水果贴">
              <span>{{ props.row.fruitSticker }}</span>
            </el-form-item>
            <el-form-item label="备注" style="width: 100%">
              <span>{{ props.row.remark }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="#" min-width="70">
        <template slot-scope="scope">
          <el-tag size="medium">{{ scope.$index + 1 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="chineseName" label="中文名称" min-width="180">
        <template slot-scope="scope">{{ scope.row.chineseName }}</template>
      </el-table-column>
      <el-table-column prop="productPrice" label="产品总价" min-width="130">
        <template slot-scope="scope">{{ scope.row.productPrice }} 元</template>
      </el-table-column>
      <el-table-column prop="productWeight" label="产品净重" min-width="130">
        <template slot-scope="scope">{{ scope.row.productWeight }} kg</template>
      </el-table-column>
      <el-table-column prop="productStandard" label="产品规格" min-width="130">
        <template slot-scope="scope">{{ scope.row.productStandard }}</template>
      </el-table-column>
      <el-table-column prop="additive" label="额外添加" min-width="160" show-overflow-tooltip>
        <template slot-scope="scope">{{ showAdditive(scope.row.extra) }}</template>
      </el-table-column>
      <el-table-column label="操作" min-width="160">
        <template slot-scope="scope">
          <el-button size="mini" :disabled="isFinish" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
          <el-button size="mini" :disabled="isFinish" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="text-align: right; margin-top: 5px">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" 
      :current-page.sync="currentPage" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
      </el-pagination>
    </div>
    <el-dialog :title="dialogFormTitle" :visible="dialogFormVisible" width="70%" :before-close="handleClose">
      <el-form ref="form" :model="form">
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="中文名称" prop="chineseName" label-width="100px">
              <el-autocomplete :fetch-suggestions="querySearch" v-model="form.chineseName" @select="productSelect">
                <i class="el-icon-edit el-input__icon" slot="suffix"></i>
                <template slot-scope="props">
                  <div>{{ props.item.show }}</div>
                </template>
              </el-autocomplete>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="产品净重" prop="productWeight" label-width="100px">
              <el-input v-model="form.productWeight"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="产品规格" prop="productStandard" label-width="100px">
              <el-input v-model="form.productStandard"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="内包装规格" prop="innerStandard" label-width="100px">
              <el-input v-model="form.innerStandard"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="数量" prop="innerCount" label-width="100px">
              <el-input v-model="form.innerCount"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="外包装规格" prop="outerStandard" label-width="100px">
              <el-input v-model="form.outerStandard"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="件数" prop="outerCount" label-width="100px">
              <el-input v-model="form.outerCount"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="味道" label-width="100px">
              <el-input v-model="form.smell"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="水果贴" label-width="100px">
              <el-input v-model="form.fruitSticker"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="包装情况" prop="packType" label-width="100px">
              <el-select v-model="form.packType">
                <el-option v-for="item in workRateOption"
                :key="item.id" :label="showPackLabel(item)" :value="item.id">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="批量导入" label-width="100px" v-show="!isUpdate">
              <el-input v-model="mutiple"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="额外添加" label-width="100px">
              <el-checkbox-group v-model="form.extra" @change="handleAdditiveChange">
                <el-checkbox v-for="additive in additiveOption" :label="additive" :key="additive">
                  {{additive}}</el-checkbox>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="备注" label-width="100px">
                <el-input type="textarea" v-model="form.remark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary"  @click="isUpdate == true ? doUpdate() : doAdd()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      isFinish: false,
      orderId: 0,
      isUpdate: false,
      dialogFormVisible: false,
      dialogFormTitle: "新增订单内容",
      tableData: [],
      listData: [],
      currentPage: 1,
      pageSize: 6,
      total: 0,
      filterData: {
        contractId: "",
        customName: "",
        daterange: null
      },
      mutiple: "",
      allAdditive: [],
      additiveOption: [],
      workRateOption: [],
      workRateTypeOption: [
        { label: "袋", value: 1 },
        { label: "纸箱", value: 2 },
        { label: "托盘", value: 3 },
        { label: "桶", value: 4 },
        { label: "其他", value: 5 }
      ],
      order: {},
      product: [],
      additivePrice: 0,
      form: {},
      rule: {
        chineseName: [
          { required: true, message: "请输入中文名称", trigger: "blur" }
        ],
        productWeight: [
          { required: true, message: "请输入产品净重", trigger: "blur" }
        ],
        productStandard: [
          { required: true, message: "请输入销售合同号", trigger: "blur" }
        ],
        innerStandard: [
          { required: true, message: "请输入内包装规格", trigger: "blur" }
        ],
        innerCount: [
          { required: true, message: "请输入内包装数量", trigger: "blur" }
        ],
        outerStandard: [
          { required: true, message: "请输入外包装规格", trigger: "blur" }
        ],
        outerCount: [
          { required: true, message: "请输入外包装件数", trigger: "blur" }
        ],
        packType: [
          { required: true, message: "请选择包装情况", trigger: "blur" }
        ]
      }
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
      if (localStorage.getItem("ms_user") == null) {
        this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
        return;
      }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1) {
        this.isUpdate = true;
        this.dialogFormTitle = "编辑订单内容";
        this.form = JSON.parse(JSON.stringify(row));
        this.form.extra = JSON.parse(this.form.extra);
        this.handleAdditiveChange(this.form.extra);
        this.dialogFormVisible = true;
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    handleDelete(index, row) {
      if (localStorage.getItem("ms_user") == null) {
        this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
        return;
      }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1) {
        this.$confirm("确认删除？")
          .then(_ => {
            this.doDelete(index, row);
          })
          .catch(_ => {});
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
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
    handleAdditiveChange(val) {
      this.additivePrice = 0;
      val.forEach(element => {
        this.allAdditive.forEach(e => {
          if (e.name == element) {
            this.additivePrice += e.price;
          }
        });
      });
    },
    goBack() {
      if (this.status != 0) {
        this.$router.push("/index");
      } else {
        this.$router.go(-1);
      }
    },
    querySearch(queryString, cb) {
      var products = this.product;
      var results = queryString
        ? products.filter(this.createFilter(queryString))
        : products;
      results.forEach(element => {
        element.show = element.name + " - " + element.price;
        element.value = element.name;
      });
      cb(results);
    },
    createFilter(queryString) {
      return product => {
        return (
          product.name.toLowerCase().indexOf(queryString.toLowerCase()) === 0
        );
      };
    },
    productSelect(item) {
      this.form.piecePrice = item.price;
      this.form.name = item.name;
    },
    renderProduct() {
      let params = {
        status: 1
      };
      this.$api
        .post(this.$url.getProductList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.product = data.list;
          } else if (data.code == "NO_AUTHORITY") {
            this.product = [];
          } else {
            this.$message({
              message: "查询失败， 失败原因：" + data.code,
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
    },
    rendeWorkRate() {
      let params = {
        status: 1
      };
      this.$api
        .post(this.$url.getWorkRateList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.workRateOption = data.list;
          } else if (data.code == "NO_AUTHORITY") {
            this.workRateOption = [];
          } else {
            this.$message({
              message: "查询失败， 失败原因：" + data.code,
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
    },
    renderOrder() {
      let params = {
        orderId: this.orderId
      };
      this.$api
        .post(this.$url.getOrderInfo, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.order = data.order;
            this.order.totalPrice = this.order.totalPrice.toFixed(2);
            this.isFinish = this.order.status == 5;
          } else {
            this.$message({
              message: "查询失败， 失败原因：" + data.code,
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
    },
    renderOrderDetail() {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        background: "rgba(255, 255, 255, 0.7)"
      });
      let params = {
        orderId: this.orderId
      };
      this.$api
        .post(this.$url.getOrderDetail, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.listData = data.list;
            this.initPagination(6);
            this.renderOrder();
          } else if (data.code == "NO_AUTHORITY") {
            this.$message({
              message: "无权限操作",
              type: "error"
            });
            this.$router.go(-1);
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
            message: err.data.status + ": " + err.data.error,
            type: "error"
          });
          loading.close();
        });
    },
    renderAdditive() {
      let params = {
        status: 2
      };
      this.$api
        .post(this.$url.getAdditiveList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.allAdditive = data.list;
            this.additiveOption = [];
            data.list.forEach(element => {
              this.additiveOption.push(element.name);
            });
          } else if (data.code == "NO_AUTHORITY") {
            this.additiveOption = [];
            this.allAdditive = [];
          } else {
            this.$message({
              message: "查询失败， 失败原因：" + data.code,
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
    },
    addOrderDetail() {
      if (localStorage.getItem("ms_user") == null) {
        this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
        return;
      }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1) {
        this.isUpdate = false;
        this.dialogFormTitle = "新增订单内容";
        this.form = {
          orderId: this.orderId,
          chineseName: "",
          productPrice: "",
          productWeight: "",
          productStandard: "",
          piecePrice: "",
          innerStandard: "",
          innerCount: "",
          outerStandard: "",
          outerCount: "",
          smell: "",
          extra: [],
          packType: this.workRateOption[0].id,
          fruitSticker: "",
          additive: [],
          remark: "",
          isFinish: false
        };
        this.mutiple = "";
        this.dialogFormVisible = true;
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    doAdd() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.innerCount == "") {
            this.form.innerCount = 0;
          }
          if (this.form.outerCount == "") {
            this.form.outerCount = 0;
          }
          if (this.form.productPrice == "") {
            this.form.productPrice = 0;
          }
          if (this.form.productWeight == "") {
            this.form.productWeight = 0;
          }
          if (this.form.piecePrice == "") {
            this.form.piecePrice = 0;
          }
          this.dialogFormVisible = false;
          this.form.productPrice =
            (this.form.piecePrice + this.additivePrice) *
            this.form.productWeight;
          this.form.extra = JSON.stringify(this.form.extra);
          let params = {};
          let url = "";
          if (this.mutiple != "") {
            url = this.$url.addOrderDetailBatch;
            params = {
              detail: this.form,
              count: this.mutiple
            };
          } else {
            url = this.$url.addOrderDetail;
            params = this.form;
          }
          this.$api
            .post(url, params)
            .then(res => {
              let data = res.data;
              if (data == "SUCCESS") {
                this.$message({
                  message: "添加成功",
                  type: "success"
                });
                this.renderOrderDetail();
              } else if (data == "NO_AUTHORITY") {
                this.$message({
                  message: "无权限操作",
                  type: "error"
                });
                this.$router.go(-1);
              } else {
                this.$message({
                  message: "添加失败， 失败原因：" + data,
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
            message: "请输入必填项",
            type: "warning"
          });
          return false;
        }
      });
      this.mutiple = "";
    },
    doUpdate() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.dialogFormVisible = false;
          this.form.productPrice =
            (this.form.piecePrice + this.additivePrice) *
            this.form.productWeight;
          this.form.extra = JSON.stringify(this.form.extra);
          let params = this.form;
          this.$api
            .post(this.$url.updateOrderDetail, params)
            .then(res => {
              let data = res.data;
              if (data == "SUCCESS") {
                this.$message({
                  message: "更新成功",
                  type: "success"
                });
                this.renderOrderDetail();
              } else if (data == "NO_AUTHORITY") {
                this.$message({
                  message: "无权限操作",
                  type: "error"
                });
                this.$router.go(-1);
              } else {
                this.$message({
                  message: "更新失败， 失败原因：" + data,
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
            message: "请输入必填项",
            type: "warning"
          });
          return false;
        }
      });
    },
    doDelete(index, row) {
      let params = row;
      this.$api
        .post(this.$url.deleteOrderDetail, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "删除成功",
              type: "success"
            });
            this.renderOrderDetail();
          } else if (data == "NO_AUTHORITY") {
            this.$message({
              message: "无权限操作",
              type: "error"
            });
            this.$router.go(-1);
          } else {
            this.$message({
              message: "删除失败， 失败原因：" + data,
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
    },
    showDate(timestamp) {
      let date = new Date(timestamp);
      return date.toLocaleDateString();
    },
    showDateTime(timestamp) {
      if (timestamp == null) {
        return "";
      }
      let date = new Date(timestamp);
      return this.dateFormat(date, "yyyy-MM-dd HH:mm:ss");
    },
    showAdditive(str) {
      let show = "";
      let arr = JSON.parse(str);
      if (arr.length == 0) {
        return "无";
      } else {
        arr.forEach(element => {
          show += element + ",";
        });
        return show.substring(0, show.length - 1);
      }
    },
    showPackLabel(item) {
      let str = item.standard + "kg";
      this.workRateTypeOption.forEach(element => {
        if (element.value == item.type) {
          str += element.label;
        }
      });
      return str;
    },
    dateFormat(date, fmt) {
      var o = {
        "M+": date.getMonth() + 1, //月份
        "d+": date.getDate(), //日
        "h+": date.getHours() % 12 == 0 ? 12 : date.getHours() % 12, //小时
        "H+": date.getHours(), //小时
        "m+": date.getMinutes(), //分
        "s+": date.getSeconds(), //秒
        "q+": Math.floor((date.getMonth() + 3) / 3), //季度
        S: date.getMilliseconds() //毫秒
      };
      if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(
          RegExp.$1,
          (date.getFullYear() + "").substr(4 - RegExp.$1.length)
        );
      }
      for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
          fmt = fmt.replace(
            RegExp.$1,
            RegExp.$1.length == 1
              ? o[k]
              : ("00" + o[k]).substr(("" + o[k]).length)
          );
        }
      }
      return fmt;
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.orderId = vm.$route.params.id;
      vm.renderAdditive();
      vm.renderProduct();
      vm.rendeWorkRate();
      vm.renderOrderDetail();
    });
  }
};
</script>

<style>
.el-autocomplete {
  width: 100%;
}
.el-step.is-center .el-step__description {
  padding-left: 0;
  padding-right: 0;
}
</style>