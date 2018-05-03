<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="16">
        <el-card>
          <div slot="header">
            <span class="card-title title-head">订单概览</span>
          </div>
          <div>
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="summary-top">生产中</div>
                <div class="summary-content" style="color: #09c">{{businessCount}}</div>
              </el-col>
              <el-col :span="6" class="separator">
                <div class="summary-top">待处理</div>
               <div class="summary-content" style="color: #09c">{{operateCount}}</div>
              </el-col>
              <el-col :span="6" class="separator">
                <div class="summary-top">已暂停</div>
                <div class="summary-content" style="color: #f00">{{pauseCount}}</div>
              </el-col>
              <el-col :span="6" class="separator">
                <div class="summary-top">总订单数</div>
                <div class="summary-content" style="color: #090">{{totalCount}}</div>
              </el-col>
            </el-row>
          </div>
        </el-card>
        <el-card style="margin-top: 20px">
          <div slot="header">
            <span class="card-title title-head">快捷操作</span>
          </div>
          <div>
            <el-row :gutter="20">
              <el-col :span="4" class="quick-content">
                <el-badge :value="divisionCount" :hidden="divisionCount == 0">
                  <el-button @click="handleDivisionCheck">分工</el-button>
                </el-badge>
              </el-col>
              <el-col :span="4" class="quick-content">
                <el-badge :value="pickUpCount" class="item" :hidden="pickUpCount == 0">
                  <el-button @click="handlePickUpCheck">提货</el-button>
                </el-badge>
              </el-col>
              <el-col :span="4" class="quick-content">
                <el-badge :value="0" class="item" hidden>
                  <el-button @click="handleHistoryCheck">查看历史</el-button>
                </el-badge>
              </el-col>
              <el-col :span="4" class="quick-content">
                <el-badge :value="0" class="item" hidden>
                  <el-button @click="handleProductCheck">产品管理</el-button>
                </el-badge>
              </el-col>
              <el-col :span="4" class="quick-content">
                <el-badge :value="0" class="item" hidden>
                  <el-button @click="handleBusinessCheck">订单管理</el-button>
                </el-badge>
              </el-col>
              <el-col :span="4" class="quick-content">
                <el-badge :value="0" class="item" hidden>
                  <el-button @click="handleEmployeeCheck">职员管理</el-button>
                </el-badge>
              </el-col>
            </el-row>
          </div>
        </el-card>
        <el-card style="margin-top: 20px">
          <div slot="header">
            <span class="card-title title-head">逐月销售额(万元)</span>
          </div>
          <chart :options="lineChart" class="my-chart"></chart>
        </el-card>
      </el-col>
      <el-col :span="8">
        <vue-event-calendar :events="calendarEvents"
         @day-changed="handleDayChanged" @month-changed="handleMonthChanged">
        </vue-event-calendar>
      </el-col>
      <el-col :span="16">
      </el-col>
      <el-col :span="8">
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data: function() {
    return {
      businessCount: 0,
      operateCount: 0,
      pauseCount: 0,
      totalCount: 0,
      divisionCount: 0,
      pickUpCount: 0,
      lineChart: {
        xAxis: {
          type: "category",
          data: []
        },
        yAxis: {
          type: "value"
        },
        series: [
          {
            data: [],
            type: "line",
            label: {
              normal: {
                show: true,
                position: "top"
              }
            }
          }
        ],
        toolbox: {
          show: true,
          feature: {
            dataZoom: {
              yAxisIndex: "none"
            },
            magicType: {
              type: ["line", "bar"]
            },
            restore: {},
            saveAsImage: {}
          }
        }
      },
      calendarEvents: []
    };
  },
  methods: {
    onSubmit() {
      this.$message.success("提交成功！");
    },
    handleDayChanged() {},
    handleMonthChanged() {},
    handleDivisionCheck() {
    if (localStorage.getItem("ms_user") == null) {
      this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
      return;
    }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1 || role == 3) {
        this.$router.push({ name: "status", params: { status: 3 } });
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    handlePickUpCheck() {
    if (localStorage.getItem("ms_user") == null) {
      this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
      return;
    }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1 || role == 2) {
        this.$router.push({ name: "status", params: { status: 2 } });
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    handleHistoryCheck() {
    if (localStorage.getItem("ms_user") == null) {
      this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
      return;
    }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1 || role == 2 || role == 3) {
        this.$router.push({name: 'history'});
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    handleProductCheck() {
    if (localStorage.getItem("ms_user") == null) {
      this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
      return;
    }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1) {
        this.$router.push({name: 'product'});
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    handleBusinessCheck() {
    if (localStorage.getItem("ms_user") == null) {
      this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
      return;
    }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1 || role == 2 || role == 3) {
        this.$router.push({name: 'onBusiness'});
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    handleEmployeeCheck() {
    if (localStorage.getItem("ms_user") == null) {
      this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
      return;
    }
      let role = JSON.parse(localStorage.getItem("ms_user")).role;
      if (role == 1) {
        this.$router.push({name: 'Employee'});
      } else {
        this.$message({
          message: "无权限操作",
          type: "error"
        });
      }
    },
    render() {
      const loading = this.$loading({
        lock: true,
        text: "Loading",
        background: "rgba(255, 255, 255, 0.7)"
      });
      this.$api
        .get(this.$url.getIndexContent)
        .then(res => {
          let data = res.data;
          if (data.code == "SUCCESS") {
            this.businessCount = data.businessCount;
            this.operateCount = data.operateCount;
            this.pauseCount = data.pauseCount;
            this.totalCount = data.totalCount;
            this.divisionCount = data.divisionCount;
            this.pickUpCount = data.pickUpCount;
            this.calendarEvents = data.eventList;
            this.lineChart.xAxis.data = [];
            this.lineChart.series[0].data = [];
            let noVolume = 0;
            data.volume.forEach(e => {
              this.lineChart.xAxis.data.push(e.date);
              this.lineChart.series[0].data.push(
                e.volume == null ? noVolume.toFixed(2) : e.volume.toFixed(2)
              );
            });
            setTimeout(() => {
              let now = new Date().toLocaleDateString();
              this.$EventCalendar.toDate(now);
            }, 1);
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
    }
  },
  beforeRouteEnter(to, from, next) {
    next(vm => {
      vm.render();
    });
  }
};
</script>

<style>
.title-head {
  position: relative;
  padding-left: 8px;
}
.title-head:after {
  position: absolute;
  top: 0.1em;
  height: calc(100% - 0.2em);
  left: 0;
  content: "";
  width: 0;
  border-left: solid rgba(26, 182, 235, 0.842) 3px;
}
.card-title {
  color: #333;
  font-size: 0.9em;
}
.summary-top {
  text-align: center;
}
.summary-content {
  margin-top: 5px;
  text-align: center;
  font-size: 2em;
}
.quick-content {
  text-align: center;
}
.second-line {
  margin-top: 20px;
}
.my-chart {
  margin: -60px 0px -40px 0px;
  width: auto !important;
  height: 250px !important;
}
</style>