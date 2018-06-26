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
          path: "/index",
          component: resolve => require(["../components/Index.vue"], resolve)
        },
        {
          path: "/employee",
          name: "employee",
          component: resolve => require(["../components/Employee.vue"], resolve)
        },
        {
          path: "/workrecord",
          name: "workrecord",
          component: resolve => require(["../components/WorkRecord.vue"], resolve)
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
              path: "onbusiness/:status",
              name: "status",
              component: resolve => require(["../components/OnBusiness.vue"], resolve)
            },
            {
              path: "history",
              name: "history",
              component: resolve => require(["../components/History.vue"], resolve)
            },
            {
              path: "workDivision/:id",
              name: "workDivision",
              component: resolve => require(["../components/WorkDivision.vue"], resolve)
            },
            {
              path: "car/:id",
              name: "car",
              component: resolve => require(["../components/Car.vue"], resolve)
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
              path: "carType",
              component: resolve => require(["../components/CarType.vue"], resolve)
            },
            {
              path: "additive",
              component: resolve => require(["../components/Additive.vue"], resolve)
            },
            {
              path: "workRate",
              component: resolve => require(["../components/WorkRate.vue"], resolve)
            },
            {
              path: "package",
              component: resolve => require(["../components/Package.vue"], resolve)
            },
            {
              path: "product",
              name: "product",
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
