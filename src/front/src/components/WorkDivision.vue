<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item>订单管理</el-breadcrumb-item>
        <el-breadcrumb-item>分工</el-breadcrumb-item>
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
          <el-col :span="2" :offset="20" style="text-align:right">
            <el-form-item label-width="0px">
                <el-button type="warning" icon="el-icon-circle-plus" v-show="order.status<5" @click="handleNext()">提交</el-button>
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
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="外包装规格">
              <span>{{ showPackageStandard(props.row.outerStandard) }}</span>
            </el-form-item>
            <el-form-item label="件数">
              <span>{{ props.row.outerCount }} 件</span>
            </el-form-item>
            <el-form-item label="味道">
              <span>{{ props.row.smell }}</span>
            </el-form-item>
            <el-form-item label="水果贴" style="width: 100%">
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
      <el-table-column prop="productWeight" label="产品净重" min-width="130">
        <template slot-scope="scope">{{ scope.row.productWeight }} kg</template>
      </el-table-column>
      <el-table-column prop="innerStandard" label="包装规格" min-width="130">
        <template slot-scope="scope">{{ showPackageStandard(scope.row.innerStandard) }}</template>
      </el-table-column>
      <el-table-column prop="innerCount" label="数量" min-width="130">
        <template slot-scope="scope">{{ scope.row.innerCount }}</template>
      </el-table-column>
      <el-table-column prop="additive" label="额外添加" min-width="160" show-overflow-tooltip>
        <template slot-scope="scope">{{ showAdditive(scope.row.extra) }}</template>
      </el-table-column>
      <el-table-column label="操作" min-width="100">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">分配</el-button>
        </template>
      </el-table-column>
      <el-table-column label="分配情况" min-width="100">
        <template slot-scope="scope">
          <i :class="[ scope.row.isFinish ? 'fa fa-check icon-b' : 'fa fa-times icon-a' ]"></i>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="text-align: right; margin-top: 5px">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
      :current-page.sync="currentPage" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
      </el-pagination>
    </div>
    <el-dialog title="分配方式" :visible="dialogFormVisible" width="60%" :before-close="handleClose">
      <el-form ref="form" :model="form">
        <el-row :gutter="20">
          <el-col :span="11">
            <el-form-item label="剩余分配量" label-width="100px">
              <el-input v-model="currentSurplus" disabled></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="班组选择" label-width="100px">
              <el-checkbox-group v-model="checkedWorker" @change="handleCheckedChange" :disabled="order.status>4">
                <el-row :gutter="0">
                  <el-col v-for="worker in workers" :key="worker.name" style="width: 20%;text-align: center;">
                    <el-checkbox :label="worker.name">
                      {{worker.name}}</el-checkbox>
                  </el-col>
                </el-row>
              </el-checkbox-group>
            </el-form-item>
          </el-col>
          <el-col :span="22" v-for="record in form.list" :key="form.list.name">
            <el-row :gutter="20">
              <el-col :span="14">
                <el-form-item label="班组名" label-width="100px">
                    <el-input v-model="record.worker" disabled></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="开始时间" label-width="100px">
                  <el-date-picker v-model="record.startTime" :disabled="order.status>4" type="datetime" format="yyyy-MM-dd HH:00:00" placeholder="选择日期时间">
                  </el-date-picker>
                </el-form-item>
              </el-col>
              <el-col :span="14">
                <el-form-item label="工作量" label-width="100px">
                    <el-input v-model.number="record.count" :disabled="order.status>4" @change="handleCountChange(record)"></el-input>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item label="预计耗时" label-width="100px">
                    <span>{{ showTime(record.spand) }}</span>
                </el-form-item>
              </el-col>
            </el-row>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary"  @click="doAdd()">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      orderId: 0,
      dialogFormVisible: false,
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
      order: {},
      workers: [],
      packageStandard: [],
      checkedWorker: [],
      currentPackType: 0,
      currentAllCount: 0,
      currentSurplus: 0,
      form: {
        list: [],
        detailId: 0
      },
      workRateOption: []
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
      if (role == 1 || role == 3) {
        this.form.list = [];
        this.form.detailId = row.id;
        this.dialogFormVisible = true;
        this.currentSurplus = row.productWeight;
        this.currentAllCount = row.productWeight;
        this.currentPackType = row.packType;
        this.checkedWorker = [];
        this.renderDivisionDetail();
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
    handleNext() {
      this.$confirm("确认提交？")
        .then(_ => {
          let tag = true;
          this.listData.forEach(element => {
            tag = element.isFinish && tag;
          });
          if (tag) {
            this.order.status = 3;
            this.order.divisionTime = new Date().getTime();
            this.updateOrderStatus();
            setTimeout(() => {
              this.$router.push({
                name: "onBusiness"
              });
            }, 500);
          }
          done();
        })
        .catch(_ => {});
    },
    handleCheckedChange(val) {
      let formLen = this.form.list.length;
      let valLen = val.length;
      let newList = [];
      if (formLen > valLen) {
        val.forEach(element => {
          let i = 0;
          for (i; i < this.form.list.length; i++) {
            if (this.form.list[i].worker == element) {
              newList.push(this.form.list[i]);
            }
          }
        });
        this.form.list = newList;
        this.currentSurplus = this.currentAllCount;
        this.form.list.forEach(element => {
          if (element.count != "") {
            this.currentSurplus -= element.count;
          }
        });
      } else {
        this.form.list.push({
          worker: val[valLen - 1],
          orderDetail: this.form.detailId,
          count: "",
          type: this.currentPackType,
          isFinish: false,
          spand: ""
        });
      }
    },
    handleCountChange(record) {
      let count = 0;
      this.form.list.forEach(element => {
        count += element.count;
      });
      if (count > this.currentAllCount) {
        this.$message({
          message: "超出可分配量，请重新分配",
          type: "error"
        });
      } else {
        this.currentSurplus = this.currentAllCount - count;
      }
      let speed = 0;
      this.workRateOption.forEach(element => {
        if (element.id == this.currentPackType) {
          speed = element.count;
        }
      });
      record.spand = (record.count / 1000 / speed).toFixed(2);
    },
    goBack() {
      this.$router.go(-1);
    },
    rendeWorkRate() {
      let params = {
        status: 2
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
    renderPackageStandard() {
      let params = {
        standard: "",
        status: 1,
        type: 2
      };
      this.$api
        .post(this.$url.getPackageList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.packageStandard = data.list;
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
    renderWorker() {
      let params = {
        name: "",
        role: 4
      };
      this.$api
        .post(this.$url.getWorkerList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.workers = data.list;
          }else {
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
    renderOrderDetail(rePage) {
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
            if(rePage) {
              this.initPagination(6);
            } else {
              if(this.listData.length % this.pageSize == 0 && this.currentPage != 1) {
                this.currentPage -= 1;
              }
              this.handleCurrentChange(this.currentPage);
            }
            this.renderOrder();
          } else if (data.code == "NO_AUTHORITY") {
            this.$message({
              message: "无权限操作",
              type: "error"
            });
            this.$router.go(-1);
          }  else {
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
    renderDivisionDetail() {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        background: "rgba(255, 255, 255, 0.7)"
      });
      let params = {
        detailId: this.form.detailId
      };
      this.$api
        .post(this.$url.getDivisionDetail, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.form.list = data.list;
            this.checkedWorker = [];
            this.form.list.forEach(element => {
              this.checkedWorker.push(element.worker);
              this.currentSurplus -= element.count;
            });
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
    doAdd() {
      if (this.currentSurplus != 0) {
        this.$message({
          message: "仍有未分配部分，请完成后再提交",
          type: "warning"
        });
        return;
      }
      this.dialogFormVisible = false;
      this.form.list.forEach(element => {
        element.startTime = element.startTime - element.startTime % 3600000;
      });
      let params = this.form;
      this.$api
        .post(this.$url.addWorkRecord, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "添加成功",
              type: "success"
            });
            this.renderOrderDetail(false);
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
    showPrice(num) {
      return JSON.parse(localStorage.getItem("ms_user")).role == 1 ? num : "-";
    },
    showTime(time) {
      time == "" ? 0 : parseFloat(time);
      let str = "";
      let hour, minute;
      if (time >= 1) {
        hour = parseInt(time);
        minute = ((time - hour) * 60).toFixed(0);
        str = hour + "小时" + minute + "分钟";
      } else {
        minute = (time * 60).toFixed(0);
        str = minute + "分钟";
      }
      return str;
    },
    showPackageStandard(id) {
      let show = "";
      this.packageStandard.forEach(element => {
        if(element.id == id) {
          show = element.standard;
        }
      });
      return show;
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.orderId = vm.$route.params.id;
      vm.renderWorker();
      vm.rendeWorkRate();
      vm.renderPackageStandard();
      vm.renderOrderDetail(true);
    });
  }
};
</script>