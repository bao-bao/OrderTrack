package com.ordertrack.service.impl;

/* Created by AMXPC on 2018/4/8. */

import com.ordertrack.constant.OrderStatus;
import com.ordertrack.constant.PackageType;
import com.ordertrack.constant.ReturnCode;
import com.ordertrack.dao.OrderDao;
import com.ordertrack.dao.OrderDetailDao;
import com.ordertrack.dao.WorkRecordDao;
import com.ordertrack.entity.Order;
import com.ordertrack.entity.OrderDetail;
import com.ordertrack.entity.Package;
import com.ordertrack.entity.WorkRate;
import com.ordertrack.entity.WorkRecord;
import com.ordertrack.pojo.MonthVolume;
import com.ordertrack.pojo.PackageCheckResponse;
import com.ordertrack.service.OrderService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {
    @Resource
    private OrderDao orderDao;
    @Resource
    private OrderDetailDao orderDetailDao;
    @Resource
    private WorkRecordDao workRecordDao;
    @Resource
    private SettingServiceImpl settingService;
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
            if (order.getStatus() != OrderStatus.FINISH.getStatus()) {
                orderList.add(order);
            }
        }
        return orderList;
    }

    @Override
    @Transactional
    public List<Order> queryOrderListOnBusiness(String contractId, String customName, Long startDate, Long endDate) {
        int finishStatus = OrderStatus.FINISH.getStatus();
        if (!contractId.equals("")) {
            return orderDao.findOrderByContractIdAndStatusLessThan(contractId, finishStatus);
        } else if (customName.equals("") && startDate == 0) {
            return orderDao.findByStatusLessThan(finishStatus);
        } else if (customName.equals("") && startDate != 0) {
            return orderDao.findOrderByOrderTimeBetweenAndStatusLessThan(new Timestamp(startDate), new Timestamp(endDate), finishStatus);
        } else if (!customName.equals("") && startDate == 0) {
            return orderDao.findOrderByCustomNameContainingAndStatusLessThan(customName, finishStatus);
        } else if (!customName.equals("") && startDate != 0) {
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
            if (order.getStatus() == OrderStatus.FINISH.getStatus()) {
                orderList.add(order);
            }
        }
        return orderList;
    }

    @Override
    public List<Order> queryOrderListHistory(String contractId, String customName, Long startDate, Long endDate) {
        int finishStatus = OrderStatus.FINISH.getStatus();
        if (!contractId.equals("")) {
            return orderDao.findOrderByContractIdAndStatus(contractId, finishStatus);
        } else if (customName.equals("") && startDate == 0) {
            return orderDao.findOrderByStatus(finishStatus);
        } else if (customName.equals("") && startDate != 0) {
            return orderDao.findOrderByOrderTimeBetweenAndStatus(new Timestamp(startDate), new Timestamp(endDate), finishStatus);
        } else if (!customName.equals("") && startDate == 0) {
            return orderDao.findOrderByCustomNameContainingAndStatus(customName, finishStatus);
        } else if (!customName.equals("") && startDate != 0) {
            return orderDao.findOrderByCustomNameContainingAndOrderTimeBetweenAndStatus(customName, new Timestamp(startDate), new Timestamp(endDate), finishStatus);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    @Transactional
    public List<Order> queryOrderListByStatus(Integer status) {
        return orderDao.findOrderByStatus(status);
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
        List<OrderDetail> details = orderDetailDao.findByOrderId(order.getOrderId());
        for(OrderDetail detail : details) {
            deleteOrderDetail(detail);
        }
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
    public OrderDetail queryOrderDetailById(Integer orderId) {
        return orderDetailDao.findById(orderId);
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
    public ReturnCode addOrderDetailBatch(OrderDetail detail, Integer count) {
        for (int i = 0; i < count; i++) {
            detail.setId(0);
            addOrderDetail(detail);
        }
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
        for (OrderDetail detail : details) {
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
        workRecordDao.deleteByOrderDetail(orderDetail.getId());
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
        for (int i = 0; i < records.size(); i++) {
            em.merge(records.get(i));
            if (i % 30 == 0) {
                em.flush();
                em.clear();
            }
        }
        orderDetailDao.updateIsFinishByOrderDetailId(detailId);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode deleteWorkRecord(WorkRecord workRecord) {
        workRecordDao.delete(workRecord);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public List<WorkRecord> queryWorkRecordList(String name, Integer year, Integer month) {
        if (name.equals("") && year == 0) {
            return workRecordDao.findAll();
        } else if (name.equals("") && year != 0) {
            return workRecordDao.findWorkRecordByTime(year, month);
        } else if (year == 0) {
            return workRecordDao.findWorkRecordByWorker(name);
        } else {
            return workRecordDao.findWorkRecordByWorkerAndTime(name, year, month);
        }
    }

    @Override
    @Transactional
    public List<WorkRecord> queryDivisionDetail(int detailId) {
        return workRecordDao.findByOrderDetail(detailId);
    }

    @Override
    @Transactional
    public ReturnCode checkWork(int orderId) {
        List<OrderDetail> orderDetails = queryOrderDetail(orderId);
        for (OrderDetail detail : orderDetails) {
            workRecordDao.updateIsFinishByOrderDetailId(detail.getId());
        }
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public PackageCheckResponse checkPickUp(Integer orderId) {
        PackageCheckResponse resp = new PackageCheckResponse();
        boolean tag = true;
        StringBuilder message = new StringBuilder();
        Map<Integer, Integer> packageNeed = new HashMap<>();
        List<OrderDetail> orderDetails = queryOrderDetail(orderId);
        List<Package> packages = settingService.queryPackageList(1, 2);
        for (OrderDetail detail : orderDetails) {
            int outerPackId = detail.getOuterStandard();
            int outerPackNumber = detail.getOuterCount();
            int innerPackId = detail.getInnerStandard();
            int innerPackNumber = detail.getInnerCount();
            packageNeed.merge(outerPackId, outerPackNumber, (a, b) -> b + a);
            packageNeed.merge(innerPackId, innerPackNumber, (a, b) -> b + a);
        }
        for (Map.Entry<Integer, Integer> map : packageNeed.entrySet()) {
            for (Package pack : packages) {
                if (map.getKey() == pack.getId()) {
                    if (map.getValue() > pack.getNumber()) {
                        tag = false;
                        message.append(pack.getStandard()).append("： 现有").append(pack.getNumber()).append("个， 需求").append(map.getValue()).append("个<br>");
                    }
                    break;
                }
            }
        }
        if (tag) {
            for (Map.Entry<Integer, Integer> map : packageNeed.entrySet()) {
                for (Package pack : packages) {
                    if (map.getKey() == pack.getId()) {
                        pack.setNumber(pack.getNumber() - map.getValue());
                        settingService.updatePackage(pack);
                    }
                }
            }
            resp.setCode(ReturnCode.SUCCESS);
        } else {
            resp.setCode(ReturnCode.NO_ENOUGH_PACKAGE);
            resp.setMessage(message.toString());

        }
        return resp;
    }

    @Override
    @Transactional
    public Integer getBusinessCount() {
        return orderDao.findByStatusLessThan(OrderStatus.FINISH.getStatus()).size();
    }

    @Override
    @Transactional
    public Integer getOperateCount() {
        return orderDao.findByStatusBetween(OrderStatus.NEW.getStatus(), OrderStatus.PICKING_UP.getStatus()).size();
    }

    @Override
    @Transactional
    public Integer getPauseCount() {
        return orderDao.findOrderByDeliveryDateBeforeAndStatusNot(new Timestamp(new Date().getTime()), OrderStatus.FINISH.getStatus()).size();
    }

    @Override
    @Transactional
    public Integer getTotalCount() {
        return orderDao.findAll().size();
    }

    @Override
    @Transactional
    public Integer getTakeCount() {
        return orderDao.findOrderByStatus(OrderStatus.NEW.getStatus()).size();
    }

    @Override
    @Transactional
    public Integer getDivisionCount() {
        return orderDao.findOrderByStatus(OrderStatus.TAKE.getStatus()).size();
    }

    @Override
    @Transactional
    public Integer getPickUpCount() {
        return orderDao.findOrderByStatus(OrderStatus.DISTRIBUTING.getStatus()).size();
    }

    @Override
    @Transactional
    public Integer getBalanceCount() {
        return orderDao.findOrderByStatus(OrderStatus.PICKING_UP.getStatus()).size();
    }

    @Override
    @Transactional
    public List<Order> getOnBusinessList() {
        return orderDao.findByStatusLessThan(OrderStatus.FINISH.getStatus());
    }

    @Override
    @Transactional
    public List<MonthVolume> getLastYearList() {
        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH) + 1;
        Query query = em.createNativeQuery("select orderid, totalprice, month(deliverydate)" +
                " from corder where ((year(deliverydate)=?1-1 and month(deliverydate)>?2)" +
                " or (year(deliverydate)=?1 and month(deliverydate)<=?2))" +
                " and status=6" +
                " group by month(deliverydate)", MonthVolume.class);
        query.setParameter(1, year);
        query.setParameter(2, month);
        return query.getResultList();
    }
}
