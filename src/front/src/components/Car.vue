<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>订单管理</el-breadcrumb-item>
        <el-breadcrumb-item>装车</el-breadcrumb-item>
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
          <el-col :span="2" :offset="18" style="text-align:right">
            <el-form-item label-width="0px">
                <el-button type="success" icon="el-icon-circle-plus" v-show="order.status<7" @click="handleAdd()">新增</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="2" style="text-align:right">
            <el-form-item label-width="0px">
                <el-button type="warning" icon="el-icon-circle-plus" v-show="order.status<7" @click="handleNext()">提交</el-button>
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
      <el-table-column prop="id" label="#" min-width="70">
        <template slot-scope="scope">
          <el-tag size="medium">{{ scope.$index + 1 }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="license" label="车牌照" min-width="140">
        <template slot-scope="scope">{{ scope.row.license }}</template>
      </el-table-column>
      <el-table-column prop="standardId" label="载货类型" min-width="130">
        <template slot-scope="scope">{{ showPackageStandard(scope.row.standardId) }}</template>
      </el-table-column>
      <el-table-column prop="count" label="数量" min-width="100">
        <template slot-scope="scope">{{ scope.row.count }}</template>
      </el-table-column>
      <el-table-column label="操作" min-width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="text-align: right; margin-top: 5px">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page.sync="currentPage" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
      </el-pagination>
    </div>
    <el-dialog :title="dialogFormTitle" :visible="dialogFormVisible" width="50%" :before-close="handleClose">
      <el-form ref="form" :model="form">
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="车牌照" label-width="100px">
              <el-input v-model="form.license"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="货物选择" label-width="100px">
              <el-checkbox-group v-model="checkedType" @change="handleTypeChange" :disabled="order.status>7">
                <el-row :gutter="0">
                  <el-col v-for="item in packageStandard" :key="item.id" style="width: 50%;text-align: left;">
                    <el-checkbox :label="item.id">
                      {{item.standard}}(未装车：{{getRest(item.id)}})</el-checkbox>
                  </el-col>
                </el-row>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col v-for="item in formList" :key="item.type" :span="22">
            <el-form-item :label="'运载量(' + showPackageStandard(item.type) + ')'" label-width="200px">
              <el-input v-model="item.count"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary"  @click="isUpdate ? doUpdate() : doAdd()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      dialogFormTitle: "新增运货车",
      isUpdate: false,
      orderId: 0,
      dialogFormVisible: false,
      tableData: [],
      listData: [],
      currentPage: 1,
      pageSize: 6,
      total: 0,
      filterData: {},
      order: {},
      packageStandard: [],
      form: {
        license: "",
        count: "",
        orderId: this.orderId,
        standardId: ""
      },
      checkedType: [],
      formList: [],
      orderDetail: [],
      currentCar: -1
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
      this.total = this.listData.length;
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
        this.form = JSON.parse(JSON.stringify(row));
        this.isUpdate = true;
        this.dialogFormTitle = "更新运货车";
        this.dialogFormVisible = true;
        this.currentCar = index;
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
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
          this.currentCar = -1;
          done();
        })
        .catch(_ => {});
    },
    handleTypeChange(val) {
      let formLen = this.formList.length;
      let valLen = val.length;
      let newList = [];
      if (formLen > valLen) {
        val.forEach(element => {
          let i = 0;
          for (i; i < this.formList.length; i++) {
            if (this.formList[i].type == element) {
              newList.push(this.formList[i]);
            }
          }
        });
        this.formList = newList;
      } else {
        this.formList.push({
          count: "",
          type: val[valLen - 1]
        });
      }
      console.log(this.formList);
    },
    handleNext() {
      this.$confirm("确认提交？")
        .then(_ => {
          let tag = true;
          this.orderDetail.forEach(element => {
            tag = element.isFinish && tag;
          });
          if (tag) {
            this.order.status = 7;
            this.order.carTime = new Date().getTime();
            this.updateOrderStatus();
            setTimeout(() => {
              this.$router.push({
                name: "onBusiness"
              });
            }, 500);
          } else {
            let notifyStr = "";
            this.orderDetail.forEach(element => {
              if (element.isFinish == false) {
                notifyStr +=
                  '"' + this.showPackageStandard(element.type) + '" ';
              }
            });
            notifyStr += "未完成车辆分配";

            console.log(notifyStr);
            this.$notify({
              title: "有产品未装车",
              message: notifyStr,
              type: "warning",
              dangerouslyUseHTMLString: true
            });
          }
          done();
        })
        .catch(_ => {});
    },
    goBack() {
      this.$router.go(-1);
    },
    renderPackageStandard() {
      let params = {
        status: 1,
        type: 0
      };
      this.$api
        .post(this.$url.getPackageList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.orderDetail.forEach(element => {
              data.list.forEach(e => {
                if (element.type == e.id) {
                  this.packageStandard.push(e);
                }
              });
            });
          } else if (data.code == "NO_AUTHORITY") {
            this.packageStandard = [];
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
      let params = {
        orderId: this.orderId
      };
      this.$api
        .post(this.$url.getOrderDetail, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            data.list.forEach(element => {
              let tag = false;
              this.orderDetail.forEach(e => {
                if (e.type == element.outerStandard) {
                  e.count += element.outerCount;
                  tag = true;
                }
              });
              if (!tag) {
                this.orderDetail.push({
                  type: element.outerStandard,
                  count: element.outerCount,
                  isFinish: false
                });
              }
            });
            this.renderPackageStandard();
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
        })
        .catch(err => {
          this.$message({
            message: err.data.status + ": " + err.data.error,
            type: "error"
          });
        });
    },
    renderCar(rePage) {
      let params = {
        orderId: this.orderId
      };
      this.$api
        .post(this.$url.getCarInfo, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.listData = data.list;
            this.loadCheck();
            if (rePage) {
              this.initPagination(6);
            } else {
              if (
                this.listData.length % this.pageSize == 0 &&
                this.currentPage != 1
              ) {
                this.currentPage -= 1;
              }
              this.handleCurrentChange(this.currentPage);
            }
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
    handleAdd() {
      this.isUpdate = false;
      this.dialogFormTitle = "新增运货车";
      this.formList = [];
      this.checkedType = [];
      this.dialogFormVisible = true;
    },
    loadCheck() {
      this.orderDetail.forEach(element => {
        let all = element.count;
        this.listData.forEach(e => {
          if (e.standardId == element.type) {
            all -= e.count;
          }
        });
        if (all == 0) {
          element.isFinish = true;
        }
      });
    },
    getRest(type) {
      let all = 0;
      this.orderDetail.forEach(element => {
        if (element.type == type) {
          all = element.count;
        }
      });
      this.listData.forEach(element => {
        if (element.standardId == type) {
          all -= element.count;
        }
      });
      return all;
    },
    doAdd() {
      let i = 0;
      for (i; i < this.formList.length; i++) {
        let rest = this.getRest(this.formList[i].type) - this.formList[i].count;
        if (rest < 0) {
          this.$message({
            message: "分配数量超出总数",
            type: "error"
          });
          return;
        } else if (rest == 0) {
          this.orderDetail.forEach(element => {
            if (element.type == this.formList[i].type) {
              element.isFinish = true;
            }
          });
        }
      }
      this.dialogFormVisible = false;
      let tag = true;
      let j = 0;
      this.formList.forEach(element => {
        j++;
        let params = {
          license: this.form.license,
          count: element.count,
          orderId: this.orderId,
          standardId: element.type
        };
        this.$api
          .post(this.$url.addCar, params)
          .then(res => {
            let data = res.data;
            if (data == "SUCCESS") {
              tag = tag & true;
              if (tag && j == this.formList.length) {
                this.$message({
                  message: "添加成功",
                  type: "success"
                });
                this.renderCar(false);
              }
            } else {
              tag = false;
              this.$message({
                message: "部分添加失败， 失败原因：" + data,
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
      });
    },
    doUpdate() {
      this.dialogFormVisible = false;
      this.currentCar = -1;
      let params = this.form;
      this.$api
        .post(this.$url.updateCar, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "添加成功",
              type: "success"
            });
            this.renderCar(false);
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
    },
    doDelete(index, row) {
      let params = row;
      this.$api
        .post(this.$url.deleteCar, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "删除成功",
              type: "success"
            });
            this.renderCar(false);
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
    updateOrderStatus() {
      this.dialogFormVisible = false;
      let params = this.order;
      this.$api
        .post(this.$url.updateOrder, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "更新成功",
              type: "success"
            });
            this.renderOrder();
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
    },
    showDate(timestamp) {
      let date = new Date(timestamp);
      return date.toLocaleDateString();
    },
    showPackageStandard(id) {
      let show = "";
      this.packageStandard.forEach(element => {
        if (element.id == id) {
          show = element.standard;
        }
      });
      return show;
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.orderId = vm.$route.params.id;
      vm.renderOrder();
      vm.renderOrderDetail(); // renderPackageStandard in this one
      vm.renderCar(true);
    });
  }
};
</script>