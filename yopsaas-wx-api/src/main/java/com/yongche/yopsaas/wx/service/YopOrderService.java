package com.yongche.yopsaas.wx.service;

import com.yongche.yopsaas.core.system.SystemConfig;
import com.yongche.yopsaas.core.task.TaskService;
import com.yongche.yopsaas.core.util.JacksonUtil;
import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.core.yop.OrderService;
import com.yongche.yopsaas.db.domain.*;
import com.yongche.yopsaas.db.service.YopsaasRideOrderService;
import com.yongche.yopsaas.db.util.CouponUserConstant;
import com.yongche.yopsaas.db.util.GrouponConstant;
import com.yongche.yopsaas.db.util.OrderUtil;
import com.yongche.yopsaas.wx.task.OrderUnpaidTask;
import com.yongche.yopsaas.wx.task.RideOrderUnchooseCarTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yongche.yopsaas.wx.util.WxResponseCode.*;
import static com.yongche.yopsaas.wx.util.WxResponseCode.GROUPON_JOIN;

@Service
public class YopOrderService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private YopsaasRideOrderService rideOrderService;
    @Autowired
    private TaskService taskService;

    /**
     * 提交订单
     * <p>
     * 1. 创建订单表项和订单商品表项;
     * 2. 购物车清空;
     * 3. 优惠券设置已用;
     * 4. 商品货品库存减少;
     * 5. 如果是团购商品，则创建团购活动表项。
     *
     * @param userId 用户ID
     * @param body   打车订单信息
     * @return 提交订单操作结果
     */
    @Transactional
    public Object create(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        if (body == null) {
            return ResponseUtil.badArgument();
        }
        Integer cartId = JacksonUtil.parseInteger(body, "cartId");
        Integer addressId = JacksonUtil.parseInteger(body, "addressId");
        Integer couponId = JacksonUtil.parseInteger(body, "couponId");
        Integer userCouponId = JacksonUtil.parseInteger(body, "userCouponId");
        String message = JacksonUtil.parseString(body, "message");
        Integer grouponRulesId = JacksonUtil.parseInteger(body, "grouponRulesId");
        Integer grouponLinkId = JacksonUtil.parseInteger(body, "grouponLinkId");

        if (cartId == null || addressId == null || couponId == null) {
            return ResponseUtil.badArgument();
        }

        Long rideOrderId = null;
        YopsaasRideOrder order = null;
        // 订单
        order = new YopsaasRideOrder();
        order.setUserId(Long.valueOf(userId));

        // 添加订单表项
        rideOrderService.add(order);
        rideOrderId = order.getRideOrderId();

        // 订单支付超期任务
        taskService.addTask(new RideOrderUnchooseCarTask(rideOrderId));

        Map<String, Object> data = new HashMap<>();
        data.put("rideOrderId", rideOrderId);

        return ResponseUtil.ok(data);
    }
}
