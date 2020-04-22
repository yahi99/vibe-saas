package com.yongche.yopsaas.wx.web;

import com.yongche.yopsaas.core.yop.OrderService;
import com.yongche.yopsaas.wx.annotation.LoginUser;
import com.yongche.yopsaas.wx.service.YopOrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    private YopOrderService yopOrderService;

    /**
     * 提交订单
     *
     * @param userId 用户ID
     * @param body   订单信息
     * @return 提交订单操作结果
     */
    @PostMapping("create")
    public Object create(@LoginUser Integer userId, @RequestBody String body) {
        return yopOrderService.create(userId, body);
    }
}
