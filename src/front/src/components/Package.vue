<template>
  <div>
    <div class="crumbs">
      <el-breadcrumb separator="/">
        <el-breadcrumb-item><i class="el-icon-date"></i> 其他配置</el-breadcrumb-item>
        <el-breadcrumb-item>包装</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div>
      <el-form ref="filterData" :model="filterData">
        <el-row :gutter="0">
          <el-col :span="5">
            <el-form-item label-width="0px">
              <el-input v-model="filterData.standard">
                <template slot="prepend">规格</template>
              </el-input>
            </el-form-item>
          </el-col>
          <el-col :span="5">
            <el-form-item label="类型" label-width="60px">
              <el-select v-model="filterData.type">
                <el-option v-for="item in filterOption"
                :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item label="使用状态" label-width="90px">
              <el-select v-model="filterData.status">
                <el-option v-for="item in statusOption"
                :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="4">
            <el-button type="primary" style="margin-left:20px"
            icon="el-icon-search" @click="renderPackage(true)">搜索</el-button>
          </el-col>
          <el-col :span="4" style="text-align:right">
            <el-button type="warning" icon="el-icon-circle-plus" @click="addPackage()">新增</el-button>
          </el-col>
        </el-row>
      </el-form>
    </div>
    <el-table show-header border :data="tableData" style="width: 100%; text-align: center" >
      <el-table-column prop="id" label="#" min-width="50">
        <template slot-scope="scope">
          <el-tag size="medium">{{ scope.row.id }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="standard" label="规格" min-width="140">
        <template slot-scope="scope">{{ scope.row.standard }}</template>
      </el-table-column>
      <el-table-column prop="type" label="类型" min-width="140">
        <template slot-scope="scope">{{ showType(scope.row.type) }}</template>
      </el-table-column>
      <el-table-column prop="number" label="剩余数量" min-width="140">
        <template slot-scope="scope">{{ scope.row.number }}</template>
      </el-table-column>
      <el-table-column prop="price" label="价格" min-width="140">
        <template slot-scope="scope">{{ scope.row.price }} 元/个</template>
      </el-table-column>
      <el-table-column prop="isActive" label="是否使用" min-width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.isActive"
          active-color="#13ce66" inactive-color="#ff4949" @change="doUpdate(scope.row)">
          </el-switch>
        </template>
      </el-table-column>
      <el-table-column label="操作" min-width="170">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleUpdate(scope.$index, scope.row)">购进</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="block" style="text-align: right; margin-top: 5px">
      <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" 
      :current-page.sync="currentPage" :page-size="pageSize" layout="total, prev, pager, next" :total="total">
      </el-pagination>
    </div>
    <el-dialog title="新增包装" :visible="dialogFormVisible" width="30%">
      <el-form ref="form" :model="form">
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="规格" label-width="80px">
              <el-input v-model="form.standard"></el-input>
            </el-form-item>
            <el-form-item label="价格" label-width="80px">
              <el-input v-model="form.price"></el-input>
            </el-form-item>
            <el-form-item label="类型" label-width="80px">
              <el-select v-model="form.type">
                <el-option v-for="item in typeOption"
                :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="doAdd()">确 定</el-button>
      </div>
    </el-dialog>
    <el-dialog title="购进包装" :visible="buyFormVisible" width="30%">
      <el-form ref="buyForm" :model="form">
        <el-row :gutter="20">
          <el-col :span="22">
            <el-form-item label="购进数量" label-width="80px">
              <el-input v-model.number="buyNumber"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="buyFormVisible = false">取 消</el-button>
        <el-button type="primary" @click="doUpdate(form)">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      dialogFormVisible: false,
      buyFormVisible: false,
      tableData: [],
      listData: [],
      currentPage: 1,
      pageSize: 10,
      total: 0,
      filterData: {
        standard: "",
        status: 2,
        type: 2
      },
      statusOption: [
        { label: "全部", value: 2 },
        { label: "使用中", value: 1 },
        { label: "被禁用", value: 0 }
      ],
      typeOption: [
        { label: "外包装", value: 0 },
        { label: "内包装", value: 1 }
      ],
      filterOption: [
        { label: "全部", value: 2 },
        { label: "外包装", value: 0 },
        { label: "内包装", value: 1 }
      ],
      form: {},
      buyNumber: 0
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
      )
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
    handleDelete(index, row) {
      this.$confirm("确认删除？")
        .then(_ => {
          this.doDelete(index, row);
        })
        .catch(_ => {});
    },
    renderPackage(rePage) {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        background: "rgba(255, 255, 255, 0.7)"
      });
      let params = this.filterData;
      this.$api
        .post(this.$url.getPackageList, params)
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
    handleUpdate(index, row) {
      this.form = JSON.parse(JSON.stringify(row));
      this.buyNumber = 0;
      this.buyFormVisible = true;
    },
    addPackage() {
      this.form = {
        standard: "",
        type: 0,
        number: 0,
        price: "",
        isActive: true
      };
      this.dialogFormVisible = true;
    },
    doAdd() {
      this.dialogFormVisible = false;
      let params = this.form;
      this.$api
        .post(this.$url.addPackage, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "添加成功",
              type: "success"
            });
            this.renderPackage(false);
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
    },
    doUpdate(row) {
      row.number += this.buyNumber;
      this.buyNumber = 0;
      this.buyFormVisible = false;
      let params = row;
      this.$api
        .post(this.$url.updatePackage, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "更新成功",
              type: "success"
            });
            this.renderPackage(false);
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
        .post(this.$url.deletePackage, params)
        .then(res => {
          let data = res.data;
          if (data == "SUCCESS") {
            this.$message({
              message: "删除成功",
              type: "success"
            });
            this.renderPackage(false);
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
    showType(type) {
      let label = "";
      this.typeOption.forEach(element => {
        if (element.value == type) {
          label = element.label;
        }
      });
      return label;
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.filterData.status = 2;
      vm.renderPackage(true);
    });
  }
};
</script>