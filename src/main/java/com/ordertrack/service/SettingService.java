package com.ordertrack.service;

/* Created by AMXPC on 2018/4/11. */

import com.ordertrack.constant.ReturnCode;
import com.ordertrack.entity.Additive;
import com.ordertrack.entity.WorkRate;

import java.util.List;

public interface SettingService {
    List<Additive> queryAdditiveList(Integer status);
    ReturnCode addAdditive(Additive additive);
    ReturnCode updateAdditive(Additive additive);
    ReturnCode deleteAdditive(Additive additive);

    List<String> queryAdditiveName();

    List<WorkRate> queryWorkRateList(Integer status);
    ReturnCode addWorkRate(WorkRate workRate);
    ReturnCode updateWorkRate(WorkRate workRate);
    ReturnCode deleteWorkRate(WorkRate workRate);

}
