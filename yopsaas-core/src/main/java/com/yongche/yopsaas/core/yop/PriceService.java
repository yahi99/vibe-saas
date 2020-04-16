package com.yongche.yopsaas.core.yop;

import com.ridegroup.yop.api.PriceNewAPI;
import com.ridegroup.yop.bean.price.PriceNew;
import com.yongche.yopsaas.core.config.YopProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

@Service
public class PriceService {
    private final Log logger = LogFactory.getLog(PriceService.class);

    private YopProperties properties;

    public PriceNew getPrice(String city, String type) {
        String accessToken = properties.getAccessToken();
        return PriceNewAPI.getPrice(accessToken, city, type);
    }
}
