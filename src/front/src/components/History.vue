<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item><i class="el-icon-date"></i> 订单管理</el-breadcrumb-item>
        <el-breadcrumb-item>历史记录</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div>
      <el-form ref="filterData" :model="filterData">
        <el-row :gutter="20">
          <el-col :span="5">
            <el-form-item label-width="0px">
              <el-input v-model="filterData.customName">
                <template slot="prepend">客户名称</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item label="缴运费方式" label-width="90px">
              <el-select v-model="filterData.carFeeType">
                <el-option v-for="item in carFeeOption"
                :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="交货日期" label-width="70px">
              <el-date-picker v-model="filterData.daterange" type="daterange" value-format="timestamp"
              range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="2" style="text-align:right">
            <el-button type="primary"
            icon="el-icon-search" @click="renderOrder(true)">搜索</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <el-table show-header border :data="tableData" style="width: 100%; text-align: center">
      <el-table-column type="expand">
        <template slot-scope="props">
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item label="净重合计">
              <span>{{ props.row.totalWeight }} kg</span>
            </el-form-item>
            <el-form-item label="小件数合计">
              <span>{{ props.row.totalSmall }} 件</span>
            </el-form-item>
            <el-form-item label="大件数合计">
              <span>{{ props.row.totalBig }} 件</span>
            </el-form-item>
            <el-form-item label="销售合同号">
              <span>{{ props.row.contractId }}</span>
            </el-form-item>
            <el-form-item label="采购单号">
              <span>{{ props.row.purchaseId }}</span>
            </el-form-item>
            <el-form-item label="包装图片">
              <span>{{ props.row.picture }}</span>
            </el-form-item>
            <el-form-item label="缴付运费" style="width: 100%;">
              <span>{{ showCarFee(props.row.carFeeType) }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="#" min-width="70">
        <template slot-scope="scope">
          <el-tag size="medium">{{ scope.row.orderId }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="customName" label="客户名称" min-width="170">
        <template slot-scope="scope">{{ scope.row.customName }}</template>
      </el-table-column>
      <el-table-column prop="deliveryDate" label="要求交货期" min-width="150">
        <template slot-scope="scope">{{ showDate(scope.row.deliveryDate) }}</template>
      </el-table-column>
      <el-table-column prop="totalPrice" label="总价值" min-width="140">
        <template slot-scope="scope">
          <el-tooltip placement="top" :disabled="authCheck()"
           :content="'产品' + scope.row.totalPrice.toFixed(2) + '元 + 包装' + scope.row.packagePrice.toFixed(2) + '元 + 装车' + scope.row.carPrice.toFixed(2) + '元'">
            <span>{{ showPrice((scope.row.totalPrice + scope.row.packagePrice + scope.row.carPrice).toFixed(2)) }} 元</span>
          </el-tooltip>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="当前状态" min-width="90">
        <template slot-scope="scope">
          <el-tag size="medium" :type="scope.row.status == 1 ? '' : scope.row.status == 0 ? 'warning' : 'success'">
            {{ showStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="350">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleDivision(scope.$index, scope.row)">分工情况</el-button>
          <el-button size="mini" @click="handleCar(scope.$index, scope.row)">装车情况</el-button>
          <el-button size="mini" @click="handleDetail(scope.$index, scope.row)">详细信息</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="text-align: right; margin-top: 5px">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" 
      :current-page.sync="currentPage" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
      </el-pagination>
    </div>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      isUpdate: false,
      dialogFormVisible: false,
      dialogFormTitle: "新增订单信息",
      tableData: [],
      listData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      filterData: {
        carFeeType: 2,
        contractId: "",
        customName: "",
        daterange: null
      },
      form: {},
      statusOption: [
        { label: "准备", value: 0 },
        { label: "接单", value: 1 },
        { label: "提货", value: 2 },
        { label: "分配", value: 3 },
        { label: "包装", value: 4 },
        { label: "验收", value: 5 },
        { label: "装车", value: 6 },
        { label: "运费", value: 7 },
        { label: "完成", value: 8 }
      ],
      carFeeOption: [
        { label: "全部", value: 2 },
        { label: "工厂付款", value: 0 },
        { label: "客户付款", value: 1 }
      ],
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
    handleDivision(index, row) {
    if (localStorage.getItem("ms_user") == null) {
      this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
      return;
    }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1 || role == 3) {
        return this.$router.push({
          name: "workDivision",
          params: { id: row.orderId }
        });
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    handleCar(index, row) {
    if (localStorage.getItem("ms_user") == null) {
      this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
      return;
    }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1) {
        return this.$router.push({
          name: "car",
          params: { id: row.orderId }
        });
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    handleDetail(index, row) {
      return this.$router.push({
        name: "orderDetail",
        params: { id: row.orderId }
      });
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
    renderOrder(rePage) {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        background: "rgba(255, 255, 255, 0.7)"
      });
      let params = {
        carFeeType: this.filterData.carFeeType,
        customName: this.filterData.customName,
        contractId: this.filterData.contractId,
        startDate:
          this.filterData.daterange == null ? 0 : this.filterData.daterange[0],
        endDate:
          this.filterData.daterange == null ? 0 : this.filterData.daterange[1]
      };
      console.log(params);
      this.$api
        .post(this.$url.getOrderHistory, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.listData = data.list;
            if(rePage) {
              this.initPagination(10);
            } else {
              if(this.listData.length % this.pageSize == 0 && this.currentPage != 1) {
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
    doDelete(index, row) {
      let params = row;
      this.$api
        .post(this.$url.deleteOrder, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "删除成功",
              type: "success"
            });
            this.renderOrder(false);
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
    showStatus(status) {
      let label = "";
      this.statusOption.forEach(element => {
        if (element.value == status) {
          label = element.label;
        }
      });
      return label;
    },
    showPrice(num) {
      return JSON.parse(localStorage.getItem("ms_user")).role == 1 ? num : "-";
    },
    showDate(timestamp) {
      let date = new Date(timestamp);
      return date.toLocaleDateString();
    },
    showCarFee(type) {
      let str = "";
      this.carFeeOption.forEach(element => {
        if(element.value == type){
          str = element.label;
        }
      });
      return str;
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.renderOrder(true);
    });
  }
};
</script>