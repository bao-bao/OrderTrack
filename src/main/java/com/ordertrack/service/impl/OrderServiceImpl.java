package com.ordertrack.service.impl;

/* Created by AMXPC on 2018/4/8. */

import com.ordertrack.constant.OrderStatus;
import com.ordertrack.constant.ReturnCode;
import com.ordertrack.dao.OrderDao;
import com.ordertrack.dao.OrderDetailDao;
import com.ordertrack.dao.WorkRecordDao;
import com.ordertrack.entity.Order;
import com.ordertrack.entity.OrderDetail;
import com.ordertrack.entity.WorkRecord;
import com.ordertrack.service.OrderService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderDetailDao orderDetailDao;
    @Resource
    private WorkRecordDao workRecordDao;
    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Order getOrderInfo(Integer orderId) {
        return orderDao.findByOrderId(orderId);
    }

    @Override
    @Transactional
    public List<Order> getOrderListOnBusiness(int pageId, int pageSize, Order filter) {
        Page<Order> pageInfo = orderDao.findAll(Example.of(filter), new PageRequest(pageId, pageSize, Sort.Direction.ASC, "orderid"));
        ArrayList<Order> orderList = new ArrayList<>();
        for (Order order : pageInfo) {
            if(order.getStatus() != OrderStatus.FINISH.getStatus()) {
                orderList.add(order);
            }
        }
        return orderList;
    }

    @Override
    @Transactional
    public List<Order> queryOrderListOnBusiness(String contractId, String customName, Long startDate, Long endDate) {
        int finishStatus = OrderStatus.FINISH.getStatus();
        if(!contractId.equals("")) {
            return orderDao.findOrderByContractIdAndStatusLessThan(contractId, finishStatus);
        } else if(customName.equals("") && startDate == 0) {
            return orderDao.findByStatusLessThan(finishStatus);
        } else if(customName.equals("") && startDate != 0) {
            return orderDao.findOrderByOrderTimeBetweenAndStatusLessThan(new Timestamp(startDate), new Timestamp(endDate), finishStatus);
        } else if(!customName.equals("") && startDate == 0) {
            return orderDao.findOrderByCustomNameContainingAndStatusLessThan(customName, finishStatus);
        } else if(!customName.equals("") && startDate != 0) {
            return orderDao.findOrderByCustomNameContainingAndOrderTimeBetweenAndStatusLessThan(customName, new Timestamp(startDate), new Timestamp(endDate), finishStatus);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public List<Order> getOrderListHistory(int pageId, int pageSize, Order filter) {
        Page<Order> pageInfo = orderDao.findAll(Example.of(filter), new PageRequest(pageId, pageSize, Sort.Direction.ASC, "orderid"));
        ArrayList<Order> orderList = new ArrayList<>();
        for (Order order : pageInfo) {
            if(order.getStatus() == OrderStatus.FINISH.getStatus()) {
                orderList.add(order);
            }
        }
        return orderList;
    }

    @Override
    public List<Order> queryOrderListHistory(String contractId, String customName, Long startDate, Long endDate) {
        int finishStatus = OrderStatus.FINISH.getStatus();
        if(!contractId.equals("")) {
            return orderDao.findOrderByContractIdAndStatus(contractId, finishStatus);
        } else if(customName.equals("") && startDate == 0) {
            return orderDao.findOrderByStatus(finishStatus);
        } else if(customName.equals("") && startDate != 0) {
            return orderDao.findOrderByOrderTimeBetweenAndStatus(new Timestamp(startDate), new Timestamp(endDate), finishStatus);
        } else if(!customName.equals("") && startDate == 0) {
            return orderDao.findOrderByCustomNameContainingAndStatus(customName, finishStatus);
        } else if(!customName.equals("") && startDate != 0) {
            return orderDao.findOrderByCustomNameContainingAndOrderTimeBetweenAndStatus(customName, new Timestamp(startDate), new Timestamp(endDate), finishStatus);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public ReturnCode addOrder(Order order) {
        orderDao.save(order);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode updateOrder(Order order) {
        orderDao.saveAndFlush(order);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode deleteOrder(Order order) {
        orderDetailDao.deleteByOrderId(order.getOrderId());
        orderDao.delete(order);
        return ReturnCode.SUCCESS;
    }



    @Override
    @Transactional
    public List<OrderDetail> queryOrderDetail(Integer orderId) {
        return orderDetailDao.findByOrderId(orderId);
    }

    @Override
    @Transactional
    public ReturnCode addOrderDetail(OrderDetail orderDetail) {
        orderDetailDao.save(orderDetail);
        Integer orderId = orderDetail.getOrderId();
        Order order = orderDao.findByOrderId(orderId);
        order.setTotalSmall(order.getTotalSmall() + orderDetail.getInnerCount());
        order.setTotalBig(order.getTotalBig() + orderDetail.getOuterCount());
        order.setTotalPrice(order.getTotalPrice() + orderDetail.getProductPrice());
        order.setTotalWeight(order.getTotalWeight() + orderDetail.getProductWeight());
        orderDao.saveAndFlush(order);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode updateOrderDetail(OrderDetail orderDetail) {
        orderDetailDao.saveAndFlush(orderDetail);
        Integer orderId = orderDetail.getOrderId();
        Order order = orderDao.findByOrderId(orderId);
        Integer totalSmall = 0;
        Integer totalBig = 0;
        Double totalWeight = 0.0;
        Double totalPrice = 0.0;
        List<OrderDetail> details = orderDetailDao.findByOrderId(orderId);
        for(OrderDetail detail : details) {
            totalSmall += detail.getInnerCount();
            totalBig += detail.getOuterCount();
            totalPrice += detail.getProductPrice();
            totalWeight += detail.getProductWeight();
        }
        order.setTotalSmall(totalSmall);
        order.setTotalBig(totalBig);
        order.setTotalPrice(totalPrice);
        order.setTotalWeight(totalWeight);
        orderDao.saveAndFlush(order);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode deleteOrderDetail(OrderDetail orderDetail) {
        orderDetailDao.delete(orderDetail);
        Integer orderId = orderDetail.getOrderId();
        Order order = orderDao.findByOrderId(orderId);
        order.setTotalSmall(order.getTotalSmall() - orderDetail.getInnerCount());
        order.setTotalBig(order.getTotalBig() - orderDetail.getOuterCount());
        order.setTotalPrice(order.getTotalPrice() - orderDetail.getProductPrice());
        order.setTotalWeight(order.getTotalWeight() - orderDetail.getProductWeight());
        orderDao.saveAndFlush(order);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public boolean orderCheck(Order order) {
        return true;
    }

    @Override
    @Transactional
    public ReturnCode addWorkRecord(List<WorkRecord> records, Integer detailId) {
        workRecordDao.deleteByOrderDetail(detailId);
        for(int i = 0; i < records.size(); i++) {
            em.merge(records.get(i));
            if(i % 30 == 0) {
                em.flush();
                em.clear();
            }
        }
        orderDetailDao.updateIsFinishByOrderDetailId(detailId);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public List<WorkRecord> queryDivisionDetail(int detailId) {
        return workRecordDao.findByOrderDetail(detailId);
    }

}
