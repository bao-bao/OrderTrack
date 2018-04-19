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
              name: "onBusiness",
              component: resolve => require(["../components/OnBusiness.vue"], resolve)
            },
            {
              path: "history",
              component: resolve => require(["../components/History.vue"], resolve)
            },
            {
              path: "workDivision/:id",
              name: "workDivision",
              component: resolve => require(["../components/WorkDivision.vue"], resolve)
            },
            {
              path: "detail/:id",
              name: "orderDetail",
              component: resolve => require(["../components/OrderDetail.vue"], resolve)
            }
          ]
        },
        {
          path: "/setting",
          component: {template: "<router-view></router-view>"},
          children: [
            {
              path: "additive",
              component: resolve => require(["../components/Additive.vue"], resolve)
            },
            {
              path: "workRate",
              component: resolve => require(["../components/WorkRate.vue"], resolve)
            },
            {
              path: "product",
              component: resolve => require(["../components/Product.vue"], resolve)
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
