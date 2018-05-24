package com.ordertrack.controller;

/* Created by AMXPC on 2018/4/8. */

import com.ordertrack.constant.OrderStatus;
import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.Car;
import com.ordertrack.entity.Order;
import com.ordertrack.entity.OrderDetail;
import com.ordertrack.entity.WorkRecord;
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
    @PostMapping("/getStatusOrderList")
    public ListResponse<Order> getOrderList(@RequestBody OrderStatusListRequest request) {
        ListResponse<Order> resp = new ListResponse<>();
        Integer status = request.getStatus();
        List<Order> orderList = orderService.queryOrderListByStatus(status);
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
    @PostMapping("/getOrderDetailById")
    public OrderDetailIdResponse getOrderDetailList(@RequestBody OrderDetailIdRequest request) {
        OrderDetailIdResponse resp = new OrderDetailIdResponse();
        Integer detailId = request.getDetailId();
        OrderDetail detail = orderService.queryOrderDetailById(detailId);
        if(detail != null) {
            resp.setDetail(detail);
            resp.setCode(ReturnCode.SUCCESS);
        } else {
            resp.setCode(ReturnCode.NO_DATA);
        }
        return resp;
    }

    @ResponseBody
    @PostMapping("/addOrderDetail")
    public ReturnCode addOrderDetail(@RequestBody OrderDetail orderDetail) {
        return orderService.addOrderDetail(orderDetail);
    }

    @ResponseBody
    @PostMapping("/addOrderDetailBatch")
    public ReturnCode addOrderDetailBatch(@RequestBody OrderDetailBatchRequest request) {
        OrderDetail detail = request.getDetail();
        Integer count = request.getCount();
        return orderService.addOrderDetailBatch(detail, count);
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

    @ResponseBody
    @PostMapping("/addWorkRecord")
    public ReturnCode addWorkRecord(@RequestBody AddWorkRecordRequest request) {
        List<WorkRecord> list = request.getList();
        Integer detailId = request.getDetailId();
        return orderService.addWorkRecord(list, detailId);
    }

    @ResponseBody
    @PostMapping("/deleteWorkRecord")
    public ReturnCode deleteWorkRecord(@RequestBody WorkRecord workRecord) {
        return orderService.deleteWorkRecord(workRecord);
    }

    @ResponseBody
    @PostMapping("/getWorkRecordList")
    public ListResponse<WorkRecord> getWorkRecordList(@RequestBody WorkRecordListRequest request) {
        ListResponse<WorkRecord> resp = new ListResponse<>();
        String name = request.getName();
        Integer year = request.getYear();
        Integer month = request.getMonth();
        List<WorkRecord> list = orderService.queryWorkRecordList(name, year, month);
        resp.setList(list);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }

    @ResponseBody
    @PostMapping("/getDivisionDetail")
    public ListResponse<WorkRecord> getDivisionDetail(@RequestBody DivisionDetailRequest request) {
        ListResponse<WorkRecord> resp = new ListResponse<>();
        Integer detailId = request.getDetailId();
        List<WorkRecord> list = orderService.queryDivisionDetail(detailId);
        resp.setList(list);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }

    @ResponseBody
    @PostMapping("/checkWork")
    public ReturnCode checkWork(@RequestBody CheckWorkRequest request) {
        Integer orderId = request.getOrderId();
        return orderService.checkWork(orderId);
    }

    @ResponseBody
    @PostMapping("/checkPickUp")
    public PackageCheckResponse checkPickUp(@RequestBody CheckPickUpRequest request) {
        Integer orderId = request.getOrderId();
        return orderService.checkPickUp(orderId);
    }

    @ResponseBody
    @PostMapping("/getCarInfo")
    public ListResponse<Car> getCarInfo(@RequestBody CarListRequest request) {
        ListResponse<Car> resp = new ListResponse<>();
        Integer orderId = request.getOrderId();
        List<Car> orderList = orderService.queryCarInfo(orderId);
        resp.setList(orderList);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }

    @ResponseBody
    @PostMapping("/addCar")
    public ReturnCode addCar(@RequestBody Car car) {
        return orderService.addCarInfo(car);
    }

    @ResponseBody
    @PostMapping("/updateCar")
    public ReturnCode updateCar(@RequestBody Car car) {
        return orderService.updateCarInfo(car);
    }

    @ResponseBody
    @PostMapping("/deleteCar")
    public ReturnCode deleteCar(@RequestBody Car car) {
        return orderService.deleteCarInfo(car);
    }
}
