<template>
    <div class="sidebar">
        <el-menu :default-active="onRoutes" class="el-menu-vertical-demo" 
        background-color="#324157" text-color="#fff" active-text-color="#ffd04b" unique-opened router>
            <template v-for="item in items">
                <template v-if="item.subs">
                    <el-submenu :index="item.index">
                        <template slot="title"><i :class="item.icon"></i>{{ item.title }}</template>
                        <el-menu-item v-for="(subItem,i) in item.subs" :key="i" :index="item.index + subItem.index">{{ subItem.title }}
                        </el-menu-item>
                    </el-submenu>
                </template>
                <template v-else>
                    <el-menu-item :index="item.index">
                        <i :class="item.icon"></i>{{ item.title }}
                    </el-menu-item>
                </template>
            </template>
        </el-menu>
    </div>
</template>

<script>
export default {
  data() {
    if (localStorage.getItem("ms_user") == null) {
      this.$message({ message: "登录信息丢失，请重新登录", type: "error" });
      return;
    }
    let user = JSON.parse(localStorage.getItem("ms_user"));
    let role = user.role;
    if (role == 1) {
      return {
        items: [
          {
            icon: "el-icon-bell",
            index: "/index",
            title: "首页"
          },
          {
            icon: "el-icon-document",
            index: "/order",
            title: "订单管理",
            subs: [
              {
                index: "/onbusiness",
                title: "生产中"
              },
              {
                index: "/history",
                title: "历史记录"
              }
            ]
          },
          {
            icon: "el-icon-news",
            index: "/employee",
            title: "职员管理"
          },
          {
            icon: "el-icon-bell",
            index: "/workrecord",
            title: "工作记录"
          },
          {
            icon: "el-icon-star-off",
            index: "/setting",
            title: "其他配置",
            subs: [
              {
                index: "/product",
                title: "产品价格"
              },
              {
                index: "/workRate",
                title: "包装"
              },
              {
                index: "/additive",
                title: "添加物"
              }
            ]
          }
        ]
      };
    } else if (role == 2 || role == 3) {
      return {
        items: [
          {
            icon: "el-icon-bell",
            index: "/index",
            title: "首页"
          },
          {
            icon: "el-icon-document",
            index: "/order",
            title: "订单管理",
            subs: [
              {
                index: "/onbusiness",
                title: "生产中"
              },
              {
                index: "/history",
                title: "历史记录"
              }
            ]
          }
        ]
      };
    } else {
      return {
        items: [
          {
            icon: "el-icon-bell",
            index: "/index",
            title: "首页"
          }
        ]
      };
    }
  },
  computed: {
    onRoutes() {
      return this.$router.push("");
    }
  }
};
</script>

<style scoped>
.sidebar {
  display: block;
  position: absolute;
  width: 15%;
  left: 0;
  top: 70px;
  bottom: 0;
  background: #2e363f;
}
.sidebar > ul {
  height: 100%;
}
.el-submenu .el-menu-item {
  min-width: 100px;
}
</style>
