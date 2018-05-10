<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item><i class="el-icon-date"></i> 职员管理</el-breadcrumb-item>
        <el-breadcrumb-item>工作记录</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div>
      <el-form ref="filterData" :model="filterData">
        <el-row :gutter="0">
          <el-col :span="6">
            <el-form-item label="班组名" label-width="60px">
              <el-select v-model="filterData.worker">
                <el-option v-for="item in workerOption"
                :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="7">
            <el-form-item label="时间" label-width="60px">
              <el-date-picker v-model="filterData.month" type="month" value-format="yyyy-MM" placeholder="选择月份"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" style="margin-left:20px"
            icon="el-icon-search" @click="renderWorkRecord(true)">搜索</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <el-table show-header border :data="tableData" style="width: 100%; text-align: center">
      <el-table-column prop="id" label="#" min-width="50">
        <template slot-scope="scope">
          <el-tag size="medium">{{ scope.row.id }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="worker" label="班组名称" min-width="150">
        <template slot-scope="scope">{{ scope.row.worker }}</template>
      </el-table-column>
      <el-table-column prop="detailId" label="工作量" min-width="80">
        <template slot-scope="scope">{{ scope.row.count }} kg</template>
      </el-table-column>
      <el-table-column prop="spand" label="预计耗时" min-width="100">
        <template slot-scope="scope">{{ showTime(scope.row.spand) }}</template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间" min-width="160">
        <template slot-scope="scope">{{ showDate(scope.row.startTime) }}</template>
      </el-table-column>
      <el-table-column prop="type" label="包装类型" min-width="100">
        <template slot-scope="scope">{{ showPackLabel(scope.row.type) }}</template>
      </el-table-column>
      <el-table-column prop="isFinish" label="是否完成" min-width="80">
        <template slot-scope="scope"><i :class="[ scope.row.isFinish ? 'fa fa-check icon-b' : 'fa fa-times icon-a' ]"></i></template>
      </el-table-column>
      <el-table-column label="操作" min-width="140">
        <template slot-scope="scope">
          <el-button size="mini" type="primary" @click="handleDetail(scope.$index, scope.row)">查看详情</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="text-align: right; margin-top: 5px">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" 
      :current-page.sync="currentPage" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
      </el-pagination>
    </div>
    <el-dialog title="工作内容" :visible="dialogFormVisible" width="50%" :before-close="handleClose">
      <el-form ref="form" :model="form">
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="中文名称：" prop="chineseName" label-width="120px">
              <span>{{form.chineseName}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="产品净重：" prop="productWeight" label-width="120px">
              <span>{{form.productWeight}} kg</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="分配工作量：" label-width="120px">
              <span style="color: #cc0000; ">{{form.division}} kg ({{(form.division / form.productWeight * 100).toFixed(2)}})% </span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="产品规格：" prop="productStandard" label-width="120px">
              <span>{{form.productStandard}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="包装情况：" prop="packType" label-width="120px">
              <span>{{ showPackLabel(form.packType) }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="内包装规格：" prop="innerStandard" label-width="120px">
              <span>{{ showPackageStandard(form.innerStandard) }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="外包装规格：" prop="outerStandard" label-width="120px">
              <span>{{ showPackageStandard(form.outerStandard) }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="味道：" label-width="120px">
              <span>{{ form.smell }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="11">
            <el-form-item label="水果贴：" label-width="120px">
              <span>{{form.fruitSticker}}</span>
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="额外添加：" label-width="120px" v-if="hasAdditive">
              <el-tag v-for="additive in form.extra" size="medium" :key="additive">
              {{additive}}</el-tag>
            </el-form-item>
            <el-form-item label="额外添加：" label-width="120px" v-if="!hasAdditive">
              <span>无</span>
            </el-form-item>
          </el-col>
          <el-col :span="22">
            <el-form-item label="备注：" label-width="120px">
              <span>{{form.remark}}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      dialogFormVisible: false,
      hasAdditive: false,
      tableData: [],
      listData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      filterData: {
        worker: "",
        month: ""
      },
      form: {},
      workerOption: [],
      workRateOption: [],
      packageStandard: []
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
    handleDetail(index, row) {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        background: "rgba(255, 255, 255, 0.7)"
      });
      let params = {
        detailId: row.orderDetail
      };
      this.$api
        .post(this.$url.getOrderDetailById, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.form = data.detail;
            this.form.extra = JSON.parse(this.form.extra);
            this.hasAdditive = this.form.extra.length != 0;
            this.form.division = row.count;
            this.dialogFormVisible = true;
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
    handleDelete(index, row) {
      this.$confirm("此操作会导致在历史记录的分工情况出现错误！", "确认删除？")
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
    renderWorkRecord(rePage) {
      let cal =
        this.filterData.month == null ? "" : this.filterData.month.split("-");
      let params = {
        name: this.filterData.worker,
        year: cal == "" ? 0 : parseInt(cal[0]),
        month: cal == "" ? 0 : parseInt(cal[1])
      };
      this.$api
        .post(this.$url.getWorkRecordList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.listData = data.list;
            if (rePage) {
              this.initPagination(10);
            } else {
              if (this.listData.length % this.pageSize == 0) {
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
    renderUser() {
      let params = {
        name: "",
        role: 4
      };
      this.$api
        .post(this.$url.getUserList, params)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.workerOption = [];
            this.workerOption.push({ label: "全部", value: "" });
            data.list.forEach(element => {
              this.workerOption.push({
                label: element.name,
                value: element.name
              });
            });
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
    doDelete(index, row) {
      let params = row;
      this.$api
        .post(this.$url.deleteWorkRecord, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "删除成功",
              type: "success"
            });
            this.renderWorkRecord(false);
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
    showTime(time) {
      let str = "";
      let hour, minute;
      if (time > 1) {
        hour = time.toFixed(0);
        minute = ((time - hour) * 60).toFixed(0);
        str = hour + "小时" + minute + "分钟";
      } else {
        minute = (time * 60).toFixed(0);
        str = minute + "分钟";
      }
      return str;
    },
    showDate(timestamp) {
      let date = new Date(timestamp);
      return date.toLocaleString();
    },
    showPackLabel(id) {
      let item;
      this.workRateOption.forEach(element => {
        if (element.id == id) {
          item = element;
        }
      });
      if (item == undefined) {
        return "";
      } else {
        return item.standard;
      }
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
      vm.renderUser();
      vm.rendeWorkRate();
      vm.renderPackageStandard();
      vm.renderWorkRecord(true);
    });
  }
};
</script>
<style scoped>
span + span {
  margin-left: 20px;
}
</style>