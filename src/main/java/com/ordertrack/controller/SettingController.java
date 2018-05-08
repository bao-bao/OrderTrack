package com.ordertrack.controller;

/* Created by AMXPC on 2018/4/11. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.Additive;
import com.ordertrack.entity.Product;
import com.ordertrack.entity.WorkRate;
import com.ordertrack.entity.Package;
import com.ordertrack.pojo.*;
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


    @ResponseBody
    @PostMapping("/getProductList")
    public ListResponse<Product> getProductList(@RequestBody ProductListRequest filterData) {
        ListResponse<Product> resp = new ListResponse<>();
        Integer status = filterData.getStatus();
        List<Product> productList = settingService.queryProductList(status);
        resp.setList(productList);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }

    @ResponseBody
    @PostMapping("/addProduct")
    public ReturnCode addProduct(@RequestBody Product product) {
        return settingService.addProduct(product);
    }

    @ResponseBody
    @PostMapping("/updateProduct")
    public ReturnCode updateProduct(@RequestBody Product product) {
        return settingService.updateProduct(product);
    }

    @ResponseBody
    @PostMapping("/deleteProduct")
    public ReturnCode deleteProduct(@RequestBody Product product) {
        return settingService.deleteProduct(product);
    }


    @ResponseBody
    @PostMapping("/getPackageList")
    public ListResponse<Package> getPackageList(@RequestBody PackageListRequest filterData) {
        ListResponse<Package> resp = new ListResponse<>();
        Integer status = filterData.getStatus();
        Integer type = filterData.getType();
        List<Package> packageList = settingService.queryPackageList(status, type);
        resp.setList(packageList);
        resp.setCode(ReturnCode.SUCCESS);
        return resp;
    }

    @ResponseBody
    @PostMapping("/addPackage")
    public ReturnCode addPackage(@RequestBody Package pack) {
        return settingService.addPackage(pack);
    }

    @ResponseBody
    @PostMapping("/updatePackage")
    public ReturnCode updatePackage(@RequestBody Package pack) {
        return settingService.updatePackage(pack);
    }

    @ResponseBody
    @PostMapping("/deletePackage")
    public ReturnCode deletePackage(@RequestBody Package pack) {
        return settingService.deletePackage(pack);
    }
}
