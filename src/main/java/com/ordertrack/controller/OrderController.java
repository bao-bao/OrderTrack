package com.ordertrack.controller;

/* Created by AMXPC on 2018/4/8. */

import com.ordertrack.constant.OrderStatus;
import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.Order;
import com.ordertrack.entity.OrderDetail;
import com.ordertrack.pojo.*;
import com.ordertrack.service.impl.OrderServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/api/order")
public class OrderController {
    @Resource
    private OrderServiceImpl orderService;

    @ResponseBody
    @PostMapping("/getOrderList")
    public ListResponse<Order> getOrderList(@RequestBody OrderListRequest request) {
        ListResponse<Order> resp = new ListResponse<>();
        String contractId = request.getContractId();
        String customName = request.getCustomName();
        Long startDate = request.getStartDate();
        Long endDate = request.getEndDate();
        List<Order> orderList = orderService.queryOrderListOnBusiness(contractId, customName, startDate, endDate);
        resp.setList(orderList);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }


    @ResponseBody
    @PostMapping("/addOrder")
    public ReturnCode addOrder(@RequestBody Order order) {
        order.setStatus(OrderStatus.NEW.getStatus());
        if(orderService.orderCheck(order)) {
            return orderService.addOrder(order);
        } else {
            return ReturnCode.ORDER_INVALID;
        }
    }

    @ResponseBody
    @PostMapping("/updateOrder")
    public ReturnCode updateOrder(@RequestBody Order order) {
        return orderService.updateOrder(order);
    }

    @ResponseBody
    @PostMapping("/deleteOrder")
    public ReturnCode deleteOrder(@RequestBody Order order) {
        return orderService.deleteOrder(order);
    }

    @ResponseBody
    @PostMapping("/getOrderInfo")
    public OrderInfoResponse getOrderInfo(@RequestBody OrderInfoRequest request) {
        OrderInfoResponse resp = new OrderInfoResponse();
        Integer orderId = request.getOrderId();
        Order order = orderService.getOrderInfo(orderId);
        resp.setOrder(order);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }

    @ResponseBody
    @PostMapping("/getOrderDetail")
    public ListResponse<OrderDetail> getOrderDetailList(@RequestBody OrderDetailRequest request) {
        ListResponse<OrderDetail> resp = new ListResponse<>();
        Integer orderId = request.getOrderId();
        List<OrderDetail> detailList = orderService.queryOrderDetail(orderId);
        resp.setList(detailList);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }

    @ResponseBody
    @PostMapping("/addOrderDetail")
    public ReturnCode addOrderDetail(@RequestBody OrderDetail orderDetail) {
        return orderService.addOrderDetail(orderDetail);
    }

    @ResponseBody
    @PostMapping("/updateOrderDetail")
    public ReturnCode updateOrderDetail(@RequestBody OrderDetail orderDetail) {
        return orderService.updateOrderDetail(orderDetail);
    }

    @ResponseBody
    @PostMapping("/deleteOrderDetail")
    public ReturnCode deleteOrderDetail(@RequestBody OrderDetail orderDetail) {
        return orderService.deleteOrderDetail(orderDetail);
    }

    @ResponseBody
    @PostMapping("/getOrderHistory")
    public ListResponse<Order> getOrderHistory(@RequestBody OrderListRequest request) {
        ListResponse<Order> resp = new ListResponse<>();
        String contractId = request.getContractId();
        String customName = request.getCustomName();
        Long startDate = request.getStartDate();
        Long endDate = request.getEndDate();
        List<Order> orderList = orderService.queryOrderListHistory(contractId, customName, startDate, endDate);
        resp.setList(orderList);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }

}
