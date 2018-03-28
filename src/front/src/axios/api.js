import { fetch } from "./index"; //引用fetch.js
import api from "./url"; //引用url.js


//查看订单列表
export function hello(url, userid) { //hello是你要调用接口的名字，url,userId是传进来的参数
  return fetch({
    url: api.entryProxy + url,
    method: 'get',  //请求方法
    params: userid
  })
}

//新接口同上
