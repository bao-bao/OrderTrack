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
          <el-col :span="18" :offset="1">
            <el-form-item label-width="0px">
              <el-steps :active="order.status" finish-status="success">
                <el-step title="已接单"></el-step>
                <el-step title="待提货"></el-step>
                <el-step title="待分配"></el-step>
                <el-step title="包装中"></el-step>
                <el-step title="待验收"></el-step>
                <el-step title="已完成"></el-step>
              </el-steps>
            </el-form-item>
          </el-col>
          <el-col :span="2" :offset="1" style="text-align:right">
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
              <span>{{ order.totalPrice }}</span>
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
              <span>{{ order.totalSmall }} 件</span>
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
    <el-dialog :title="dialogFormTitle" :visible="dialogFormVisible" width="70%" :before-close="handleClose">
      <el-form ref="form" :model="form">
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="中文名称" label-width="100px">
              <el-input v-model="form.chineseName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="产品净重" label-width="100px">
              <el-input v-model="form.productWeight"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="产品规格" label-width="100px">
              <el-input v-model="form.productStandard"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="内包装规格" label-width="100px">
              <el-input v-model="form.innerStandard"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="数量" label-width="100px">
              <el-input v-model="form.innerCount"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="外包装规格" label-width="100px">
              <el-input v-model="form.outerStandard"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="件数" label-width="100px">
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
          <el-col :span="22">
            <el-form-item label="额外添加" label-width="100px">
              <el-checkbox-group v-model="form.extra">
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
      filterData: {
        contractId: "",
        customName: "",
        daterange: null
      },
      additiveOption: [],
      order: {},
      form: {}
    };
  },
  methods: {
    handleEdit(index, row) {
      this.isUpdate = true;
      this.dialogFormTitle = "编辑订单内容";
      this.form = JSON.parse(JSON.stringify(row));
      this.form.extra = JSON.parse(this.form.extra);
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
    goBack() {
      this.$router.go(-1);
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
            this.isFinish = this.order.status == 6;
          } else {
            this.$message({
              message: "查询失败， 失败原因：" + data.code,
              type: "error"
            });
          }
        })
        .catch(err => {
          this.$message({
            message: JSON.stringify(err.data),
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
            this.tableData = data.list;
            this.renderOrder();
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
    renderAdditive() {
      this.$api
        .get(this.$url.getAdditiveName)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.additiveOption = data.list;
          } else {
            this.$message({
              message: "查询失败， 失败原因：" + data.code,
              type: "error"
            });
          }
        })
        .catch(err => {
          this.$message({
            message: JSON.stringify(err.data),
            type: "error"
          });
        });
    },
    addOrderDetail() {
      this.isUpdate = false;
      this.dialogFormTitle = "新增订单内容";
      this.form = {
        orderId: this.orderId,
        chineseName: "",
        productWeight: "",
        productStandard: "",
        innerStandard: "",
        innerCount: "",
        outerStandard: "",
        outerCount: "",
        smell: "",
        fruitSticker: "",
        additive: [],
        remark: ""
      };
      this.dialogFormVisible = true;
    },
    doAdd() {
      this.dialogFormVisible = false;
      this.form.extra = JSON.stringify(this.form.extra);
      let params = this.form;
      this.$api
        .post(this.$url.addOrderDetail, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "添加成功",
              type: "success"
            });
            this.renderOrderDetail();
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
        .post(this.$url.deleteOrderDetail, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "删除成功",
              type: "success"
            });
            this.renderOrderDetail();
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
    showDate(timestamp) {
      let date = new Date(timestamp);
      return date.toLocaleDateString();
    },
    showAdditive(str) {
      let show = "";
      console.log(str)
      let arr = JSON.parse(str);
      if (arr.length == 0) {
        return "无";
      } else {
        arr.forEach(element => {
          show += element + ",";
        });
        return show.substring(0, show.length - 1);
      }
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.orderId = vm.$route.params.id;
      vm.renderAdditive();
      vm.renderOrderDetail();
    });
  }
};
</script>