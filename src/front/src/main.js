// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import axios from 'axios'
import qs from 'qs'
import Element from 'element-ui';
import url from './axios/url';

import 'element-ui/lib/theme-chalk/index.css';    // 默认主题
//import '../static/css/theme-green/index.css';       // 浅绿色主题

// 设置axios
let axiosIns = axios.create({});

if (process.env.NODE_ENV == 'development') {
    axiosIns.defaults.baseURL = 'http://localhost:8081/api';
} else if (process.env.NODE_ENV == 'debug') {
    axiosIns.defaults.baseURL = '***';
} else if (process.env.NODE_ENV == 'production') {
    axiosIns.defaults.baseURL = '***';
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
        return new Promise(function (resolve, reject) {
            axiosIns[method](uri, data, config).then((response)=> {
                /*根据后台数据进行处理
                 *1 code===200   正常数据+错误数据     code!==200   网络异常等
                 *2 code===200   正常数据     code!==200   错误数据+网络异常等
                 * 这里使用的是第一种方式
                 * ......
                 */
                if (response.data.StatusCode) {
                    //toast封装：  参考ele-mint-ui
                    Toast({
                        message: response.data.Message,
                        position: 'top',
                        duration: 2000
                    });
                    if (response.data.Message === '未登录') {
                        Toast({
                            message: response.data.Message,
                            position: '',
                            duration: 2000
                        });
                        //使用vue实例做出对应行为  change state or router
                    }
                } else {
                    resolve(response);
                }
            }).catch((response)=> {
                //reject response
                //alert('xiuxiu，限你10分钟到我面前来,不然...');
            })
        })
    }
});

// 将API方法绑定到全局
Vue.prototype.$api = api
Vue.prototype.$url = url

Vue.use(Element, { size: 'small' });
Vue.config.productionTip = false
/* eslint-disable no-new */
new Vue({
  el: '#app',
  router,
  components: { App },
  template: '<App/>'
})
