package com.yongche.yopsaas.core.yop;

import com.ridegroup.yop.api.PriceNewAPI;
import com.ridegroup.yop.bean.price.PriceNew;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PriceService {
    private final Log logger = LogFactory.getLog(PriceService.class);

    public PriceNew getPrice(String city, String type) {
        return PriceNewAPI.getPrice("", "", "");
    }
}
