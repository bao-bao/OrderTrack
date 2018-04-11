package com.ordertrack.controller;

/* Created by AMXPC on 2018/4/11. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.Additive;
import com.ordertrack.entity.WorkRate;
import com.ordertrack.pojo.AdditiveListRequest;
import com.ordertrack.pojo.ListResponse;
import com.ordertrack.pojo.WorkRateListRequest;
import com.ordertrack.service.impl.SettingServiceImpl;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/api/setting")
public class SettingController {
    @Resource
    private SettingServiceImpl settingService;
    
    @ResponseBody
    @PostMapping("/getAdditiveList")
    public ListResponse<Additive> getAdditiveList(@RequestBody AdditiveListRequest filterData) {
        ListResponse<Additive> resp = new ListResponse<>();
        Integer status = filterData.getStatus();
        List<Additive> additiveList = settingService.queryAdditiveList(status);
        resp.setList(additiveList);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }

    @ResponseBody
    @PostMapping("/addAdditive")
    public ReturnCode addAdditive(@RequestBody Additive additive) {
        return settingService.addAdditive(additive);
    }

    @ResponseBody
    @PostMapping("/updateAdditive")
    public ReturnCode updateAdditive(@RequestBody Additive additive) {
        return settingService.updateAdditive(additive);
    }

    @ResponseBody
    @PostMapping("/deleteAdditive")
    public ReturnCode deleteAdditive(@RequestBody Additive additive) {
        return settingService.deleteAdditive(additive);
    }

    @ResponseBody
    @GetMapping("/getAdditiveName")
    public ListResponse<String> getAdditiveName() {
        ListResponse<String> resp = new ListResponse<>();
        List<String> additiveList = settingService.queryAdditiveName();
        resp.setList(additiveList);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }

    @ResponseBody
    @PostMapping("/getWorkRateList")
    public ListResponse<WorkRate> getWorkRateList(@RequestBody WorkRateListRequest filterData) {
        ListResponse<WorkRate> resp = new ListResponse<>();
        Integer status = filterData.getStatus();
        List<WorkRate> workRateList = settingService.queryWorkRateList(status);
        resp.setList(workRateList);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }

    @ResponseBody
    @PostMapping("/addWorkRate")
    public ReturnCode addWorkRate(@RequestBody WorkRate workRate) {
        return settingService.addWorkRate(workRate);
    }

    @ResponseBody
    @PostMapping("/updateWorkRate")
    public ReturnCode updateWorkRate(@RequestBody WorkRate workRate) {
        return settingService.updateWorkRate(workRate);
    }

    @ResponseBody
    @PostMapping("/deleteWorkRate")
    public ReturnCode deleteWorkRate(@RequestBody WorkRate workRate) {
        return settingService.deleteWorkRate(workRate);
    }

}
