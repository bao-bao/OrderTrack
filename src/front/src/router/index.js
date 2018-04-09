import Vue from "vue";
import Router from "vue-router";
import HelloWorld from "@/components/HelloWorld";

Vue.use(Router);

export default new Router({
  routes: [
    {
      path: "/",
      redirect: "/login"
    },
    {
      path: "/home",
      component: resolve => require(["../components/common/Home.vue"], resolve),
      children: [
        {
          path: "/baseform",
          component: resolve => require(["../components/BaseForm.vue"], resolve)
        },
        {
          path: "/employee",
          component: resolve => require(["../components/Employee.vue"], resolve)
        },
        {
          path: "/order",
          component: {template: "<router-view></router-view>"},
          children: [
            {
              path: "onbusiness",
              component: resolve => require(["../components/OnBusiness.vue"], resolve)
            },
            {
              path: "history",
              component: resolve => require(["../components/History.vue"], resolve)
            },
            {
              path: "detail/:id",
              name: "orderDetail",
              component: resolve => require(["../components/OrderDetail.vue"], resolve)
            }
          ]
        }
      ]
    },
    {
      path: "/login",
      component: resolve => require(["../components/Login.vue"], resolve)
    }
  ]
});
