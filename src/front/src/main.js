// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import qs from 'qs'
import Element from 'element-ui';
import url from './axios/url';
import vueEventCalendar from 'vue-event-calendar'
import ECharts from 'vue-echarts/components/ECharts'
 
// import ECharts modules manually to reduce bundle size
import 'echarts'

import 'vue-event-calendar/dist/style.css'
import 'font-awesome/css/font-awesome.css'
import 'element-ui/lib/theme-chalk/index.css';    // 默认主题
//import '../static/css/theme-green/index.css';       // 浅绿色主题

// register component to use
Vue.component('chart', ECharts)

Vue.use(vueEventCalendar, {locale: 'zh'})

Vue.use(Element, { size: 'small' });
Vue.config.productionTip = false
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})

// 设置axios
let axiosIns = axios.create({});

if (process.env.NODE_ENV == 'development') {
    axiosIns.defaults.baseURL = 'http://localhost:8081/api';
} else if (process.env.NODE_ENV == 'debug') {
    axiosIns.defaults.baseURL = '***';
} else if (process.env.NODE_ENV == 'production') {
    axiosIns.defaults.baseURL = '/api';
}

axiosIns.defaults.headers.post['Content-Type'] = 'application/json;charset=UTF-8';
axiosIns.defaults.headers.post['X-Requested-With'] = 'XMLHttpRequest';
axiosIns.defaults.headers.get['X-Requested-With'] = 'XMLHttpRequest';
axiosIns.defaults.responseType = 'json';
axiosIns.defaults.transformRequest = [function (data) {
    //数据序列化
    return JSON.stringify(data);
}
];
axiosIns.defaults.validateStatus = function (status) {
    return true;
};
axiosIns.interceptors.request.use(function (config) {
    //配置config
    config.headers.Accept = 'application/json';
    config.headers.System = 'vue';
    // let token = Vue.localStorage.get('token');
    // if(token){
    //     config.headers.Token = token;
    // }
    return config;
});
axiosIns.interceptors.response.use(function (response) {
    let status = response.status;
    if (status === 200) {
        return Promise.resolve(response);
    } else {
        return Promise.reject(response);
    }
});

let ajaxMethod = ['get', 'post'];

let api = {};
ajaxMethod.forEach((method)=> {
    //数组取值的两种方式
    api[method] = function (uri, data, config) {
        return axiosIns[method](uri, data, config);
    }
});

// 将API方法绑定到全局
Vue.prototype.$api = api
Vue.prototype.$url = url