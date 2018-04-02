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
        }
      ]
    },
    {
      path: "/login",
      component: resolve => require(["../components/Login.vue"], resolve)
    }
  ]
});
