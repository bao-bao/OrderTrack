<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item><i class="el-icon-date"></i> 订单管理</el-breadcrumb-item>
        <el-breadcrumb-item>生产中</el-breadcrumb-item>
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
            icon="el-icon-search" @click="renderOrder(true)">搜索</el-button>
          </el-col>
          <el-col :span="2" :offset="2" style="text-align:right">
            <el-button type="warning" :v-show="status==0" icon="el-icon-circle-plus" @click="addOrder()">新增</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <el-table show-header border :data="tableData" style="width: 100%; text-align: center" :row-class-name="tableRowClassName">
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
              <span>{{ props.row.purchaseId }}</span>
            </el-form-item>
            <el-form-item label="包装图片">
              <span>{{ props.row.picture }}</span>
            </el-form-item> 
            <el-form-item label="缴运费方式">
              <span v-if="props.row.status > 6">{{ showCarFee(props.row.carFeeType) }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column prop="id" label="#" min-width="50">
        <template slot-scope="scope">
          <el-tag size="medium">{{ scope.row.orderId }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="customName" label="客户名称" min-width="120">
        <template slot-scope="scope">{{ scope.row.customName }}</template>
      </el-table-column>
      <el-table-column prop="deliveryDate" label="要求交货期" min-width="100">
        <template slot-scope="scope">{{ showDate(scope.row.deliveryDate) }}</template>
      </el-table-column>
      <el-table-column prop="contractId" label="销售合同号" min-width="120">
        <template slot-scope="scope">{{ scope.row.contractId }}</template>
      </el-table-column>
      <el-table-column prop="totalPrice" label="总价值" min-width="100">
          <template slot-scope="scope">
            <el-tooltip placement="top" :disabled="authCheck()"
             :content="'产品' + scope.row.totalPrice.toFixed(2) + '元 + 包装' + scope.row.packagePrice.toFixed(2) + '元 + 装车' + scope.row.carPrice.toFixed(2) + '元'">
              <span>{{ showPrice((scope.row.totalPrice + scope.row.packagePrice + scope.row.carPrice).toFixed(2)) }} 元</span>
            </el-tooltip>
            </template>
      </el-table-column>
      <el-table-column prop="status" label="当前状态" min-width="90">
        <template slot-scope="scope">
          <el-tag size="medium" :type="scope.row.status <= 2 ? '' : scope.row.status <= 4 ? 'warning' : scope.row.status <= 6 ? 'info' : 'success'">
            {{ showStatus(scope.row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="325">
        <template slot-scope="scope">
          <el-button size="mini" v-if="scope.row.status == 0" @click="handleDown(scope.$index, scope.row)">下发</el-button>
          <el-button size="mini" v-if="scope.row.status == 2" @click="handleDivision(scope.$index, scope.row)">分配</el-button>
          <el-button size="mini" v-if="scope.row.status == 3" @click="handlePickUp(scope.$index, scope.row)">提货</el-button>
          <el-button size="mini" v-if="scope.row.status == 4" @click="handleCheck(scope.$index, scope.row)">验收</el-button>
          <el-button size="mini" v-if="scope.row.status == 5" @click="handleCar(scope.$index, scope.row)">装车</el-button>
          <el-button size="mini" v-if="scope.row.status == 6" @click="handleCarFee(scope.$index, scope.row)">运费选择</el-button>
          <el-button size="mini" v-if="scope.row.status == 7" @click="handleBalance(scope.$index, scope.row)">结算</el-button>
          <el-button size="mini" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
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
    <el-dialog :title="dialogFormTitle":visible="dialogFormVisible" width="50%" :before-close="handleClose">
      <el-form ref="form" :model="form">
        <el-row :gutter="20">
          <el-col :span="11">
            <el-form-item label="客户名称" prop="customName" label-width="100px">
              <el-input v-model="form.customName"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="要求交货期" label-width="100px">
              <el-date-picker type="date" placeholder="选择时间" v-model="form.deliveryDate" style="width: 100%;">></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="采购单号" prop="purchaseId" label-width="100px">
              <el-input v-model="form.purchaseId"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="销售合同号" prop="contractId" label-width="100px">
              <el-input v-model="form.contractId"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="当前状态" v-show="isUpdate" label-width="100px">
              <el-select v-model="form.status">
                <el-option v-for="item in statusOption"
                :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="11" v-show="form.status > 2">
            <el-form-item label="分工情况" label-width="100px">
              <el-button size="mini" v-show="form.status == 3 || form.status == 4" @click="handleDivision(0, form)">重新分配</el-button>
              <el-button size="mini" v-show="form.status > 4" @click="handleDivision(0, form)" style="margin-left: 0px">查看详情</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="11" v-show="form.status > 3">
            <el-form-item label="包装损耗情况" label-width="100px">
              <el-button size="mini" @click="handleLoss(0, form)">查看详情</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="11" v-show="form.status > 5">
            <el-form-item label="装车情况" label-width="100px">
              <el-button size="mini" @click="handleCar(0, form)">查看详情</el-button>
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="包装图片" label-width="100px">
              <el-input v-model="form.picture"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="isUpdate == true ? doUpdate() : doAdd()">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="运费付款方" :visible="carFeeFormVisible" width="30%">
      <el-form>
        <el-form-item label="付款方" label-width="100px">
          <el-select v-model="carFee">
            <el-option v-for="item in carFeeOption"
             :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="carFeeFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="doCarFeeUpdate()">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="包装损耗" :visible="packLossFormVisible" width="30%" :before-close="handleClose">
      <el-form v-for="i in lossCount" :key="i">
        <el-form-item label="包装类型" label-width="100px">
          <el-select v-model="lossForm[i-1].packageId">
            <el-option v-for="item in packOption"
             :key="item.value" :label="item.label" :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="损耗数量" label-width="100px">
          <el-input v-model="lossForm[i-1].number"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="warning" @click="addLoss()">新增项</el-button>
        <el-button type="danger" @click="deleteLoss()">删除项</el-button>
        <el-button @click="cancelLoss()">取 消</el-button>
        <el-button type="primary" @click="updatePackageLoss()">确 定</el-button>
      </div>
    </el-dialog>
     <el-dialog title="包装损耗情况" :visible="lossDialogVisible" width="50%" :before-close="handleClose">
      <el-form ref="lossData" :model="lossData">
        <el-row :gutter="20" v-for="item in lossData" :key="item.id">
          <el-col :span="11">
            <el-form-item label="包装名称：" prop="packageId" label-width="120px">
              <span>{{showPackageStandard(item.packageId)}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="损耗数量：" prop="number" label-width="120px">
              <span>{{item.number}} 个</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="lossDialogVisible = false">关 闭</el-button>
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
      dialogFormTitle: "新增订单信息",
      carFeeFormVisible: false,
      packLossFormVisible: false,
      lossDialogVisible: false,
      tableData: [],
      listData: [],
      lossData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      filterData: {
        contractId: "",
        customName: "",
        daterange: null
      },
      carFee: "",
      status: 0,
      form: {},
      lossForm: [],
      lossCount: 0,
      currentOrderId: 0,
      currentOrder: {},
      statusOption: [
        { label: "准备", value: 0 },
        { label: "接单中", value: 1 },
        { label: "已接单", value: 2 },
        { label: "待提货", value: 3 },
        { label: "待验收", value: 4 },
        { label: "待装车", value: 5 },
        { label: "缴运费", value: 6 },
        { label: "待结算", value: 7 },
        { label: "完成", value: 8 }
      ],
      carFeeOption: [
        { label: "工厂付款", value: 0 },
        { label: "客户付款", value: 1 }
      ],
      packOption: [],
      innerStandard: [],
      rule: {
        customName: [
          { required: true, message: "请输入客户名称", trigger: "blur" }
        ],
        purchaseId: [
          { required: true, message: "请输入采购单号", trigger: "blur" }
        ],
        contractId: [
          { required: true, message: "请输入销售合同号", trigger: "blur" }
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
      if (role == 1) {
        this.isUpdate = true;
        this.dialogFormTitle = "编辑订单信息";
        this.form = JSON.parse(JSON.stringify(row));
        this.dialogFormVisible = true;
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    handleDown(index, row) {
      if (localStorage.getItem("ms_user") == null) {
        this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
        return;
      }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1) {
        this.$confirm("确认提交？")
          .then(_ => {
            row.status = 1;
            this.form = JSON.parse(JSON.stringify(row));
            this.doUncheckUpdate();
            done();
          })
          .catch(_ => {});
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
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
    handlePickUp(index, row) {
      this.currentOrderId = row.orderId;
      this.currentOrder = row;
      this.renderInnerPack();
    },
    addLoss() {
      this.lossCount += 1;
      this.lossForm.push({
        orderId: this.currentOrderId,
        packageId: "",
        number: ""
      });
    },
    deleteLoss() {
      this.lossCount = this.lossCount == 0 ? 0 : this.lossCount - 1;
      this.lossForm.pop();
    },
    cancelLoss() {
      this.packLossFormVisible = false;
      this.lossForm = [];
      this.lossCount = 0;
      this.currentOrderId = 0;
      this.currentOrder = {};
    },
    updatePackageLoss() {
      if (localStorage.getItem("ms_user") == null) {
        this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
        return;
      }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1 || role == 2) {
        this.confirmPickUp();
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    confirmPickUp() {
      this.$confirm("确认提交？")
        .then(_ => {
          this.form = JSON.parse(JSON.stringify(this.currentOrder));
          this.doCheckPickUp(this.form.orderId, this.lossForm);
          done();
        })
        .catch(_ => {});
    },
    handleCheck(index, row) {
      if (localStorage.getItem("ms_user") == null) {
        this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
        return;
      }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1 || role == 3) {
        this.$confirm("确认提交？")
          .then(_ => {
            row.status = 5;
            row.checkTime = new Date().getTime();
            this.form = JSON.parse(JSON.stringify(row));
            this.doUncheckUpdate();
            this.doCheck(this.form.orderId);
            done();
          })
          .catch(_ => {});
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    handleBalance(index, row) {
      if (localStorage.getItem("ms_user") == null) {
        this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
        return;
      }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1 || role == 6) {
        this.$confirm("确认提交？")
          .then(_ => {
            row.status = 8;
            row.finishTime = new Date().getTime();
            this.form = JSON.parse(JSON.stringify(row));
            this.doUncheckUpdate();
            done();
          })
          .catch(_ => {});
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
      if (role == 1 || role == 5) {
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
    handleLoss(index, row) {
      if (localStorage.getItem("ms_user") == null) {
        this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
        return;
      }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1 || role == 2) {
        this.renderPackLoss(row.orderId);
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    handleCarFee(index, row) {
      if (localStorage.getItem("ms_user") == null) {
        this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
        return;
      }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1 || role == 5) {
        this.carFeeFormVisible = true;
        this.form = JSON.parse(JSON.stringify(row));
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    doCarFeeUpdate() {
      this.carFeeFormVisible = false;
      this.form.status = 7;
      this.form.carFeeType = this.carFee;
      this.form.carFeeTime = new Date().getTime();
      this.doUncheckUpdate();
    },
    handleDetail(index, row) {
      if (row.status == 1) {
        if (localStorage.getItem("ms_user") == null) {
          this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
          return;
        }
        let role = JSON.parse(localStorage.getItem("ms_user")).role;
        if (role == 3) {
          row.status = 2;
          row.takeTime = new Date().getTime();
          this.form = JSON.parse(JSON.stringify(row));
          this.doUncheckUpdate();
        } else if (role == 1) {
        } else {
          this.$message({
            message: "无权限操作",
            type: "error"
          });
        }
      }
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
        this.$confirm("此操作会同时删除相关订单详细信息！", "确认删除？")
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
        customName: this.filterData.customName,
        contractId: this.filterData.contractId,
        startDate:
          this.filterData.daterange == null ? 0 : this.filterData.daterange[0],
        endDate:
          this.filterData.daterange == null ? 0 : this.filterData.daterange[1]
      };
      this.$api
        .post(this.$url.getOrderList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.listData = data.list;
            if (rePage) {
              this.initPagination(10);
            } else {
              if (
                this.listData.length % this.pageSize == 0 &&
                this.currentPage != 1
              ) {
                this.currentPage -= 1;
              }
              this.handleCurrentChange(this.currentPage);
            }
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
    renderStatusOrder(rePage) {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        background: "rgba(255, 255, 255, 0.7)"
      });
      let params = {
        status: this.status
      };
      this.$api
        .post(this.$url.getStatusOrderList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.listData = data.list;
            if (rePage) {
              this.initPagination(10);
            } else {
              if (
                this.listData.length % this.pageSize == 0 &&
                this.currentPage != 1
              ) {
                this.currentPage -= 1;
              }
              this.handleCurrentChange(this.currentPage);
            }
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
    addOrder() {
      if (localStorage.getItem("ms_user") == null) {
        this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
        return;
      }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1) {
        this.isUpdate = false;
        this.dialogFormTitle = "新增订单信息";
        this.form = {
          customName: "",
          purchaseId: "",
          contractId: "",
          totalBig: 0,
          totalSmall: 0,
          totalWeight: 0,
          totalPrice: 0,
          packagePrice: 0,
          carPrice: 0,
          carFeeType: 0,
          deliveryDate: new Date(),
          picture: "",
          status: 0
        };
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
          this.dialogFormVisible = false;
          let params = this.form;
          this.$api
            .post(this.$url.addOrder, params)
            .then(res => {
              let data = res.data;
              if (data == "SUCCESS") {
                this.$message({
                  message: "添加成功",
                  type: "success"
                });
                if (this.status != 0) {
                  this.renderStatusOrder(false);
                } else {
                  this.renderOrder(false);
                }
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
    },
    doUpdate() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          this.dialogFormVisible = false;
          let params = this.form;
          this.$api
            .post(this.$url.updateOrder, params)
            .then(res => {
              let data = res.data;
              if (data == "SUCCESS") {
                this.$message({
                  message: "更新成功",
                  type: "success"
                });
                if (this.status != 0) {
                  this.renderStatusOrder(false);
                } else {
                  this.renderOrder(false);
                }
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
    doUncheckUpdate() {
      let params = this.form;
      this.$api
        .post(this.$url.updateOrder, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "更新成功",
              type: "success"
            });
            if (this.status != 0) {
              this.renderStatusOrder(false);
            } else {
              this.renderOrder(false);
            }
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
            if (this.status != 0) {
              this.renderStatusOrder(false);
            } else {
              this.renderOrder(false);
            }
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
    doCheck(oId) {
      let params = {
        orderId: oId
      };
      this.$api
        .post(this.$url.checkWork, params)
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
    doCheckPickUp(oId, loss) {
      let params = {
        orderId: oId,
        loss: loss
      };
      this.$api
        .post(this.$url.checkPickUp, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.$message({
              message: "更新成功",
              type: "success"
            });
            this.$notify({
              title: "包装剩余情况",
              message: data.message,
              type: "success",
              dangerouslyUseHTMLString: true
            });
            this.form.status = 4;
            this.form.pickUpTime = new Date().getTime();
            this.doUncheckUpdate();
            this.packLossFormVisible = false;
            this.lossForm = [];
            this.lossCount = 0;
            this.currentOrderId = 0;
            this.currentOrder = {};
          } else if (data.code == "NO_ENOUGH_PACKAGE") {
            this.$notify({
              title: "包装数量不足",
              message: data.message,
              type: "warning",
              dangerouslyUseHTMLString: true
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
    renderInnerPack() {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        background: "rgba(255, 255, 255, 0.7)"
      });
      let params = {
        orderId: this.currentOrderId
      };
      this.$api
        .post(this.$url.getOrderDetail, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.packOption = [];
            data.list.forEach(element => {
              let tag = false;
              this.packOption.forEach(e => {
                if (e.value == element.innerStandard) {
                  tag = true;
                }
              });
              if (!tag) {
                this.innerStandard.forEach(f => {
                  if (f.id == element.innerStandard) {
                    this.packOption.push({
                      value: f.id,
                      label: f.standard
                    });
                  }
                });
              }
            });
            this.packLossFormVisible = true;
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
    renderInnerStandard() {
      let params = {
        standard: "",
        status: 1,
        type: 1
      };
      this.$api
        .post(this.$url.getPackageList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.innerStandard = data.list;
          } else if (data.code == "NO_AUTHORITY") {
            this.innerStandard = [];
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
    renderPackLoss(oid) {
      let params = {
        orderId: oid
      };
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        background: "rgba(255, 255, 255, 0.7)"
      });
      this.$api
        .post(this.$url.getPackLoss, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.lossData = data.list;
            this.lossDialogVisible = true;
          } else if (data.code == "NO_AUTHORITY") {
            this.lossData = [];
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
    },
    showPrice(num) {
      return JSON.parse(localStorage.getItem("ms_user")).role == 1 ? num : "-";
    },
    showCarFee(type) {
      let str = "";
      this.carFeeOption.forEach(element => {
        if (element.value == type) {
          str = element.label;
        }
      });
      return str;
    },
    showPackageStandard(id) {
      let show = "";
      this.innerStandard.forEach(element => {
        if(element.id == id) {
          show = element.standard;
        }
      });
      return show;
    },
    tableRowClassName({ row, rowIndex }) {
      let now = new Date().getTime();
      if (row.deliveryDate < now) {
        return "dangrous-row";
      } else if (row.deliveryDate - now < 3600 * 24 * 3 * 1000) {
        return "warning-row";
      }
      return "";
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (vm.$route.params.status != undefined) {
        vm.status = vm.$route.params.status;
        vm.renderStatusOrder(true);
      } else {
        vm.renderOrder(true);
      }
      vm.renderInnerStandard();
    });
  }
};
</script>
<style>
.el-table .warning-row {
  background: rgb(253, 249, 241);
}

.el-table .dangrous-row {
  background: #fcf2f2;
}
</style>