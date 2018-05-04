package com.ordertrack.controller;

/* Created by AMXPC on 2018/4/23. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.Order;
import com.ordertrack.pojo.IndexContentResponse;
import com.ordertrack.pojo.MonthVolume;
import com.ordertrack.pojo.OrderEvent;
import com.ordertrack.pojo.VolumeResponse;
import com.ordertrack.service.impl.OrderServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

@Controller
@RequestMapping("/api/home")
public class HomeController {
    @Resource
    private OrderServiceImpl orderService;

    @ResponseBody
    @GetMapping("/getIndexContent")
    public IndexContentResponse getIndexContent() {
        IndexContentResponse resp = new IndexContentResponse();
        resp.setBusinessCount(orderService.getBusinessCount());
        resp.setOperateCount(orderService.getOperateCount());
        resp.setPauseCount(orderService.getPauseCount());
        resp.setTotalCount(orderService.getTotalCount());
        resp.setTakeCount(orderService.getTakeCount());
        resp.setDivisionCount(orderService.getDivisionCount());
        resp.setPickUpCount(orderService.getPickUpCount());
        resp.setBalanceCount(orderService.getBalanceCount());

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        ArrayList<OrderEvent> eventList = new ArrayList<>();
        List<Order> onBusinessList = orderService.getOnBusinessList();
        for (Order o :onBusinessList) {
            OrderEvent event = new OrderEvent();
            event.setTitle(o.getCustomName());
            event.setDate(sdf.format(o.getDeliveryDate()));
            event.setDesc("总价：" + o.getTotalPrice() + " 元");
            eventList.add(event);
        }
        resp.setEventList(eventList);

        Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+08:00"));
        SimpleDateFormat s = new SimpleDateFormat("yy-MM");
        Integer lastYear = cal.get(Calendar.YEAR) - 1;
        Integer month = cal.get(Calendar.MONTH) + 1;
        ArrayList<VolumeResponse> volumeList = new ArrayList<>();
        List<MonthVolume> lastYearList = orderService.getLastYearList();
        for(int i = 0; i < 12; i++) {
            VolumeResponse volume = new VolumeResponse();
            cal.set(lastYear+((month + i) / 12), (month + i) % 12, 1); // set(2018, 4, 1) => 2018-05-01
            volume.setDate(s.format(cal.getTime()));
            for(MonthVolume v : lastYearList) {
                if(v.getMonth() == (month + i) % 12 + 1) {
                    volume.setVolume(v.getVolume() / 10000);
                }
            }
            volumeList.add(volume);
        }
        resp.setVolume(volumeList);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }
}
