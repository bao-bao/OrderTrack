export default {
    //接口代理配置
    entryProxy:'/api',
  
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
  
    hello: '/hello',
    /**订单管理**/
    //1 获取订单列表 GET /order/getOrderList
    getOrderList: '/order/getOrderList',
    //2 获取订单信息 GET /order/getOrder
    getOrder: '/order/getOrder',
  
  }