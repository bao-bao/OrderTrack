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
              <el-input v-model="filterData.contractId">
                <template slot="prepend">合同号</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label-width="0px">
              <el-input v-model="filterData.customName">
                <template slot="prepend">客户名称</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item>
              <el-date-picker v-model="filterData.daterange" type="daterange" value-format="timestamp"
              range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期">
              </el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="2">
            <el-button type="primary" style="margin-left:20px"
            icon="el-icon-search" @click="renderOrder()">搜索</el-button>
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
            <el-form-item label="采购单号">
              <template slot-scope="scope">{{ scope.row.purchaseId }}</template>
            </el-form-item>
            <el-form-item label="包装图片" style="width: 100%">
              <span>{{ props.row.picture }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="#" min-width="70">
        <template slot-scope="scope">
          <el-tag size="medium">{{ scope.row.orderId }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="customName" label="客户名称" min-width="180">
        <template slot-scope="scope">{{ scope.row.customName }}</template>
      </el-table-column>
      <el-table-column prop="deliveryDate" label="要求交货期" min-width="150">
        <template slot-scope="scope">{{ showDate(scope.row.deliveryDate) }}</template>
      </el-table-column>
      <el-table-column prop="contractId" label="销售合同号" min-width="150">
        <template slot-scope="scope">{{ scope.row. contractId }}</template>
      </el-table-column>
      <el-table-column prop="totalPrice" label="总价值" min-width="130">
        <template slot-scope="scope">{{ scope.row. totalPrice }}</template>
      </el-table-column>
      <el-table-column prop="status" label="当前状态" min-width="100">
        <template slot-scope="scope">
          <el-tag size="medium" :type="scope.row.status == 1 ? '' : scope.row.status == 0 ? 'warning' : 'success'">
            {{ showStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="260">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleDetail(scope.$index, scope.row)">详细信息</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
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
      filterData: {
        contractId: "",
        customName: "",
        daterange: null
      },
      form: {},
      statusOption: [
        { label: "准备中", value: 0 },
        { label: "已接单", value: 1 },
        { label: "待提货", value: 2 },
        { label: "待分配", value: 3 },
        { label: "包装中", value: 4 },
        { label: "待结算", value: 5 },
        { label: "已完成", value: 6 }
      ]
    };
  },
  methods: {
    handleDetail(index, row) {
      return this.$router.push({
        name: "orderDetail",
        params: { id: row.orderId }
      });
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
    renderOrder() {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        background: "rgba(255, 255, 255, 0.7)"
      });
      let params = {
        customName: this.filterData.customName,
        contractId: this.filterData.contractId,
        startDate:
          this.filterData.daterange == null ? 0 : this.filterData.daterange[0],
        endDate:
          this.filterData.daterange == null ? 0 : this.filterData.daterange[1]
      };
      this.$api
        .post(this.$url.getOrderHistory, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.tableData = data.list;
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
          loadming.close();
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
            this.renderOrder();
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
    showStatus(status) {
      let label = "";
      this.statusOption.forEach(element => {
        if (element.value == status) {
          label = element.label;
        }
      });
      return label;
    },
    showDate(timestamp) {
      let date = new Date(timestamp);
      return date.toLocaleDateString();
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.renderOrder();
    });
  }
};
</script>