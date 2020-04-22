package com.yongche.yopsaas.core.yop;

import com.ridegroup.yop.api.ToftAPI;
import com.ridegroup.yop.bean.BaseResultT;
import com.ridegroup.yop.bean.toft.Estimated;

import java.util.List;
import java.util.Map;

public class ToftService extends BaseService {
    public BaseResultT<List<Estimated>> estimatedAll(Map<String, Object> reqMap) {
        String accessToken = this.getProperties().getAccessToken();
        return ToftAPI.estimatedAll(accessToken, reqMap);
    }
}
