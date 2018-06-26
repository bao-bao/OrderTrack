package com.ordertrack.service.impl;

/* Created by AMXPC on 2018/4/11. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.dao.*;
import com.ordertrack.entity.*;
import com.ordertrack.entity.Package;
import com.ordertrack.service.SettingService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {
    @Resource
    private AdditiveDao additiveDao;
    @Resource
    private WorkRateDao workRateDao;
    @Resource
    private ProductDao productDao;
    @Resource
    private PackageDao packageDao;
    @Resource
    private CarTypeDao carTypeDao;

    @Override
    @Transactional
    public List<Additive> queryAdditiveList(Integer status) {
        if (status == 2) {
            return additiveDao.findAll();
        } else {
            return additiveDao.findByIsActive(status == 1);
        }
    }

    @Override
    @Transactional
    public ReturnCode addAdditive(Additive additive) {
        additiveDao.save(additive);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode updateAdditive(Additive additive) {
        additiveDao.saveAndFlush(additive);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode deleteAdditive(Additive additive) {
        additiveDao.delete(additive);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public List<String> queryAdditiveName() {
        List<Additive> additives = additiveDao.findByIsActive(true);
        ArrayList<String> names = new ArrayList<>();
        for (Additive additive : additives) {
            names.add(additive.getName());
        }
        return names;
    }

    @Override
    @Transactional
    public List<WorkRate> queryWorkRateList(Integer status) {
        if (status == 2) {
            return workRateDao.findAll();
        } else {
            return workRateDao.findByIsActive(status == 1);
        }
    }

    @Override
    @Transactional
    public ReturnCode addWorkRate(WorkRate workRate) {
        workRateDao.save(workRate);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode updateWorkRate(WorkRate workRate) {
        workRateDao.saveAndFlush(workRate);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode deleteWorkRate(WorkRate workRate) {
        workRateDao.delete(workRate);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public List<Product> queryProductList(Integer status) {
        if (status == 2) {
            return productDao.findAll();
        } else {
            return productDao.findByIsActive(status == 1);
        }
    }

    @Override
    @Transactional
    public ReturnCode addProduct(Product product) {
        productDao.save(product);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode updateProduct(Product product) {
        productDao.saveAndFlush(product);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode deleteProduct(Product product) {
        productDao.delete(product);
        return ReturnCode.SUCCESS;
    }

    @Override
    public List<Package> queryPackageList(String standard, Integer status, Integer type) {
        if (status == 2) {
            if (type == 2 && standard.equals("")) {
                return packageDao.findAll();
            } else if (type == 2 && !standard.equals("")) {
                return packageDao.findByStandardContaining(standard);
            } else if (type != 2 && standard.equals("")) {
                return packageDao.findByType(type);
            } else {
                return packageDao.findByTypeAndStandardContaining(type, standard);
            }
        } else {
            if (type == 2 && standard.equals("")) {
                return packageDao.findByIsActive(status == 1);
            } else if (type == 2 && !standard.equals("")) {
                return packageDao.findByIsActiveAndStandardContaining(status == 1, standard);
            } else if (type != 2 && standard.equals("")) {
                return packageDao.findByIsActiveAndType(status == 1, type);
            } else {
                return packageDao.findByIsActiveAndTypeAndStandardContaining(status == 1, type, standard);
            }
        }
    }

    @Override
    public ReturnCode addPackage(Package pack) {
        packageDao.save(pack);
        return ReturnCode.SUCCESS;
    }

    @Override
    public ReturnCode updatePackage(Package pack) {
        packageDao.saveAndFlush(pack);
        return ReturnCode.SUCCESS;
    }

    @Override
    public ReturnCode deletePackage(Package pack) {
        packageDao.delete(pack);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public List<CarType> queryCarTypeList(Integer status) {
        if (status == 2) {
            return carTypeDao.findAll();
        } else {
            return carTypeDao.findByIsActive(status == 1);
        }
    }

    @Override
    @Transactional
    public ReturnCode addCarType(CarType carType) {
        carTypeDao.save(carType);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode updateCarType(CarType carType) {
        carTypeDao.saveAndFlush(carType);
        return ReturnCode.SUCCESS;
    }

    @Override
    @Transactional
    public ReturnCode deleteCarType(CarType carType) {
        carTypeDao.delete(carType);
        return ReturnCode.SUCCESS;
    }

}
