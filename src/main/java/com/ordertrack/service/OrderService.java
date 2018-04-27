package com.ordertrack.service;

/* Created by AMXPC on 2018/4/8. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.Order;
import com.ordertrack.entity.OrderDetail;
import com.ordertrack.entity.WorkRecord;

import java.util.List;

public interface OrderService {

    Order getOrderInfo(Integer orderId);
    List<Order> getOrderListOnBusiness(int pageId, int pageSize, Order filter);
    List<Order> queryOrderListOnBusiness(String contractId, String customName, Long startTime, Long endTime);
    List<Order> getOrderListHistory(int pageId, int pageSize, Order filter);
    List<Order> queryOrderListHistory(String contractId, String customName, Long startTime, Long endTime);
    ReturnCode addOrder(Order order);
    ReturnCode updateOrder(Order order);
    ReturnCode deleteOrder(Order order);
    List<Order> queryOrderListByStatus(Integer status);

    boolean orderCheck(Order order);

    List<OrderDetail> queryOrderDetail(Integer orderId);
    OrderDetail queryOrderDetailById(Integer detailId);
    ReturnCode addOrderDetail(OrderDetail orderDetail);
    ReturnCode updateOrderDetail(OrderDetail orderDetail);
    ReturnCode deleteOrderDetail(OrderDetail orderDetail);

    ReturnCode addWorkRecord(List<WorkRecord> records, Integer detailId);
    ReturnCode deleteWorkRecord(WorkRecord workRecord);
    List<WorkRecord> queryWorkRecordList(String name, Integer year, Integer month);
    List<WorkRecord> queryDivisionDetail(int detailId);
    ReturnCode checkWork(int orderId);

    Integer getBusinessCount();
    Integer getOperateCount();
    Integer getPauseCount();
    Integer getTotalCount();
    Integer getDivisionCount();
    Integer getPickUpCount();
    List<Order> getOnBusinessList();
    List getLastYearList();
}
