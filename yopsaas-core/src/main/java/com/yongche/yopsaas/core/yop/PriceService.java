package com.yongche.yopsaas.core.yop;

import com.ridegroup.yop.api.PriceNewAPI;
import com.ridegroup.yop.bean.price.PriceNew;
import com.yongche.yopsaas.core.config.YopProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PriceService extends BaseService {
    private final Log logger = LogFactory.getLog(PriceService.class);

    public PriceNew getPrice(String city, String type) {
        String accessToken = this.getProperties().getAccessToken();
        return PriceNewAPI.getPrice(accessToken, city, type);
    }
}
