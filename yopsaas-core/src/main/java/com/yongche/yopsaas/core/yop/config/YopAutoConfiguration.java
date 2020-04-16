package com.yongche.yopsaas.core.yop.config;

import com.yongche.yopsaas.core.config.YopProperties;
import com.yongche.yopsaas.core.yop.OrderService;
import com.yongche.yopsaas.core.yop.PriceService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({PriceService.class, OrderService.class})
public class YopAutoConfiguration {

    private final YopProperties properties;

    public YopAutoConfiguration(YopProperties properties) {
        this.properties = properties;
    }

    @Bean
    public PriceService priceService() {
        PriceService priceService = new PriceService();
        priceService.setProperties(properties);
        return priceService;
    }

    @Bean
    public OrderService orderService() {
        OrderService orderService = new OrderService();
        orderService.setProperties(properties);
        return orderService;
    }

}
