package com.yongche.yopsaas.wx.web;

import com.yongche.yopsaas.core.yop.OrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * YOP服务
 */
@RestController
@RequestMapping("/wx/yoporder")
@Validated
public class WxYopOrderController {
    private final Log logger = LogFactory.getLog(WxYopOrderController.class);

    @Autowired
    private OrderService orderService;
}
