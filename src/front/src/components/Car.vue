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
                <el-button type="success" icon="el-icon-circle-plus" v-show="order.status<6" @click="handleAdd()">新增</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="2" style="text-align:right">
            <el-form-item label-width="0px">
                <el-button type="warning" icon="el-icon-circle-plus" v-show="order.status<6" @click="handleNext()">提交</el-button>
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
              <el-tooltip placement="top" :disabled="authCheck()"
              :content="'产品' + order.totalPrice + '元 + 包装' + order.packagePrice + '元 + 装车' + order.carPrice + '元'">
                <span>{{ showPrice(parseFloat(order.totalPrice) + parseFloat(order.packagePrice) + parseFloat(order.carPrice)) }} 元</span>
             </el-tooltip>
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
      <el-table-column prop="count" label="数量" min-width="100">
        <template slot-scope="scope">{{ scope.row.count }}</template>
      </el-table-column>
      <el-table-column label="操作" min-width="180">
        <template slot-scope="scope">
          <el-button size="mini" type="" @click="handleEdit(scope.$index, scope.row)">详细信息</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)" :disabled="order.status > 5">删除</el-button>
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
            <el-form-item label="车牌照" label-width="100px">
              <el-input v-model="form.license"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="货物选择" label-width="100px">
              <el-checkbox-group v-model="checkedProduct" @change="handleDetailChange" :disabled="order.status>7">
                <el-row :gutter="0">
                  <el-col v-for="item in orderDetail" :key="item.id" style="width: 50%;text-align: left;">
                    <el-checkbox :label="item.id" :disabled="order.status > 5">
                      {{item.chineseName}}({{getRest(item.id, "not license")}}件 —— {{(item.productWeight / item.outerCount) * getRest(item.id, "not license")}}kg)</el-checkbox>
                  </el-col>
                </el-row>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col v-for="item in formList" :key="item.orderDetail" :span="22">
            <el-row :gutter="20">
              <el-col :span="24">
            <el-form-item :label="'运载量(' + showProductName(item.orderDetail) + ')'" label-width="200px">
              <el-input v-model="item.count" @change="handleCountChange(item.orderDetail, item.count)"></el-input>
            </el-form-item>
              </el-col>
              <el-col :span="12">
                <el-form-item label="装车方式" label-width="200px">
                  <el-select v-model="item.carType">
                    <el-option v-for="item in carTypeOption"
                    :key="item.value" :label="item.label" :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="重量" label-width="70px">
                  <span>{{ item.weight }} kg</span>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="运费" label-width="70px">
                  <span>{{ (item.weight / 1000 * getCarPrice(item.carType)).toFixed(2) }} 元</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" v-show="!isUpdate" @click="doAdd()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      dialogFormTitle: "新增运货车",
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
      form: {},
      checkedProduct: [],
      carTypeOption: [],
      formList: [],
      orderDetail: [],
      isUpdate: false
    };
  },
  methods: {
    initPagination(size) {
      let carData = [];
      this.listData.forEach(f => {
        let tag = false;
        carData.forEach(e => {
          if (e.license == f.license) {
            e.count += f.count;
            tag = true;
          }
        });
        if (!tag) {
          carData.push({ license: f.license, count: f.count });
        }
      });
      this.pageSize = size;
      this.currentPage = 1;
      this.total = carData.length;
      this.tableData = carData.slice(0, this.pageSize);
    },
    handleSizeChange(val) {
      this.initPagination(val);
    },
    handleCurrentChange(val) {
      this.currentPage = val;
      let carData = [];
      this.listData.forEach(f => {
        let tag = false;
        carData.forEach(e => {
          if (e.license == f.license) {
            e.count += f.count;
            tag = true;
          }
        });
        if (!tag) {
          carData.push({ license: f.license, count: f.count });
        }
      });
      this.total = carData.length;
      this.tableData = carData.slice(
        (val - 1) * this.pageSize,
        val * this.pageSize
      );
    },
    authCheck() {
      if (localStorage.getItem("ms_user") == null) {
        this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
        return;
      }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1) {
        return false;
      } else {
        return true;
      }
    },
    handleEdit(index, row) {
      if (localStorage.getItem("ms_user") == null) {
        this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
        return;
      }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1 || role == 5) {
        this.isUpdate = true;
        this.formList = [];
        this.checkedProduct = [];
        this.form.license = row.license;
        this.listData.forEach(element => {
          if (element.license == this.form.license) {
            this.checkedProduct.push(element.orderDetail);
            this.formList.push({
              orderDetail: element.orderDetail,
              count: element.count,
              weight: 0,
              carType: element.carType
            });
            this.handleCountChange(element.orderDetail, element.count);
          }
        });
        this.dialogFormTitle = "更新运货车";
        this.dialogFormVisible = true;
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
          this.isUpdate = false;
          done();
        })
        .catch(_ => {});
    },
    handleCountChange(id, val) {
      let weight = 0;
      this.orderDetail.forEach(element => {
        if (element.id == id) {
          weight = element.productWeight / element.outerCount;
        }
      });
      this.formList.forEach(element => {
        if (element.orderDetail == id) {
          element.weight = val * weight;
        }
      });
    },
    handleDetailChange(val) {
      let formLen = this.formList.length;
      let valLen = val.length;
      let newList = [];
      if (formLen > valLen) {
        val.forEach(element => {
          let i = 0;
          for (i; i < this.formList.length; i++) {
            if (this.formList[i].orderDetail == element) {
              newList.push(this.formList[i]);
            }
          }
        });
        this.formList = newList;
      } else {
        this.formList.push({
          orderDetail: val[valLen - 1],
          count: "",
          carType: "",
          weight: ""
        });
      }
    },
    handleNext() {
      this.$confirm("确认提交？")
        .then(_ => {
          let tag = true;
          let hint = "";
          this.orderDetail.forEach(e => {
            let allCount = e.outerCount;
            let allWeight = e.productWeight;
            let carPrice = 0.0;
            this.listData.forEach(f => {
              if (f.orderDetail == e.id) {
                allCount -= f.count;
                allWeight -= f.weight;
                carPrice += f.weight / 1000 * this.getCarPrice(f.carType);
              }
            });
            if (allCount != 0 || allWeight != 0) {
              tag = false;
              console.log(allCount, allWeight)
              hint = hint + e.chineseName + ", ";
            } else {
              e.carPrice = parseFloat(carPrice.toFixed(2));
            }
          });
          if (tag) {
            const loading = this.$loading({
              lock: true,
              text: "Loading",
              background: "rgba(255, 255, 255, 0.7)"
            });
            this.order.status = 6;
            this.order.carPrice = 0;
            this.orderDetail.forEach(element => {
              this.order.carPrice += element.carPrice;
            });
            this.order.carTime = new Date().getTime();
            this.updateOrderStatus();
            this.updateOrderDetails();
            setTimeout(() => {
              loading.close();
              this.$router.push({
                name: "onBusiness"
              });
            }, 800);
          } else {
            this.$notify({
              title: "请核实装车件数和重量",
              message: hint.substring(0, hint.length - 2) + " 未完成分配",
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
        standard: "",
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
    renderCarType() {
      let params = {
        status: 1
      };
      this.$api
        .post(this.$url.getCarTypeList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            data.list.forEach(element => {
              this.carTypeOption.push({
                label: element.name,
                value: element.id,
                price: element.price
              });
            });
          } else if (data.code == "NO_AUTHORITY") {
            this.carTypeOption = [];
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
            this.orderDetail = data.list;
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
      this.dialogFormTitle = "新增运货车";
      this.formList = [];
      this.form.license = "";
      this.checkedProduct = [];
      this.dialogFormVisible = true;
      this.isUpdate = false;
    },
    getRest(id, license) {
      let all = 0;
      this.orderDetail.forEach(element => {
        if (element.id == id) {
          all = element.outerCount;
        }
      });
      this.listData.forEach(element => {
        if (element.orderDetail == id && element.license != license) {
          all -= element.count;
        }
      });
      return all;
    },
    doAdd() {
      let i = 0;
      for (i; i < this.formList.length; i++) {
        let rest =
          this.getRest(this.formList[i].orderDetail, this.form.license) -
          this.formList[i].count;
        if (rest < 0) {
          this.$message({
            message: "分配数量超出总数",
            type: "error"
          });
          return;
        }
      }
      this.dialogFormVisible = false;
      let tag = true;
      let j = 0;
      this.formList.forEach(element => {
        j++;
        let params = {
          license: this.form.license,
          orderId: this.orderId,
          orderDetail: element.orderDetail,
          count: element.count,
          weight: element.weight,
          carType: element.carType
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
    doDelete(index, row) {
      let params = { license: row.license, orderId: this.orderId };
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
    updateOrderDetails() {
      this.orderDetail.forEach(element => {
        let params = element;
        this.$api
          .post(this.$url.updateOrderDetail, params)
          .then(res => {
            let data = res.data;
            if (data == "SUCCESS") {
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
      });
    },
    getCarPrice(type) {
      let price = 0;
      this.carTypeOption.forEach(element => {
        if (element.value == type) {
          price = element.price;
        }
      });
      return price;
    },
    showDate(timestamp) {
      let date = new Date(timestamp);
      return date.toLocaleDateString();
    },
    showPrice(num) {
      return JSON.parse(localStorage.getItem("ms_user")).role == 1 ? num : "-";
    },
    showProductName(id) {
      let show = "";
      this.orderDetail.forEach(element => {
        if (element.id == id) {
          show = element.chineseName;
        }
      });
      return show;
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.orderId = vm.$route.params.id;
      vm.renderOrder();
      vm.renderCarType();
      vm.renderOrderDetail(); // renderPackageStandard in this one
      vm.renderCar(true);
    });
  }
};
</script>