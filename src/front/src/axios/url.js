export default {
  //接口代理配置
  entryProxy: "/api",

  //也可以像下面这样，区分环境或者区分服务器等等
  // let service = 'dev';
  // // let service = 'prod';
  // let api = '';
  // if (service === 'dev') {
  //   /**dev开发**/
  //   api = '/stomatology/patient';
  // } else if (service === 'prod') {
  //   /**prod部署**/
  //   api = '/proxy/client';
  // }

  // hello: '/hello',

  /**登录验证 */
  login: "/user/login",

  /**人员管理 */
  getUserList: "/user/getUserList",
  addUser: "/user/addUser",
  updateUser: "/user/updateUser",
  deleteUser: "/user/deleteUser",

  updateSelf: "/user/updateSelf",

  /**订单管理**/
  getOrderList: "/order/getOrderList",
  addOrder: "/order/addOrder",
  updateOrder: "/order/updateOrder",
  deleteOrder: "/order/deleteOrder",

  getOrderInfo: "/order/getOrderInfo",
  getOrderDetail: "/order/getOrderDetail",
  addOrderDetail: "/order/addOrderDetail",
  updateOrderDetail: "/order/updateOrderDetail",
  deleteOrderDetail: "/order/deleteOrderDetail",

  getOrderHistory: "/order/getOrderHistory",

  /**其他配置**/
  getAdditiveList: "/setting/getAdditiveList",
  addAdditive: "/setting/addAdditive",
  updateAdditive: "/setting/updateAdditive",
  deleteAdditive: "/setting/deleteAdditive",

  getAdditiveName: "/setting/getAdditiveName",

  getWorkRateList: "/setting/getWorkRateList",
  addWorkRate: "/setting/addWorkRate",
  updateWorkRate: "/setting/updateWorkRate",
  deleteWorkRate: "/setting/deleteWorkRate",
};
