package com.yongche.yopsaas.core.yop;

import com.ridegroup.yop.api.OrderAPI;
import com.ridegroup.yop.bean.order.CreateOrderResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class OrderService extends BaseService {
    private final Log logger = LogFactory.getLog(OrderService.class);

    public CreateOrderResult create(Map<String, Object> reqMap) {
        String accessToken = this.getProperties().getAccessToken();
        return OrderAPI.createOrder(accessToken, reqMap);
    }
}
