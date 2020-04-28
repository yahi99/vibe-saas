package com.yongche.yopsaas.wx.web;

import com.yongche.yopsaas.core.util.JacksonUtil;
import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.core.validator.Order;
import com.yongche.yopsaas.core.validator.RideSort;
import com.yongche.yopsaas.core.yop.OrderService;
import com.yongche.yopsaas.wx.annotation.LoginUser;
import com.yongche.yopsaas.wx.service.YopOrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 获取当前和未付订单
     *
     * @param userId 用户ID
     * @return 获取当前和未付订单
     */
    @GetMapping("getCurrentAndUnpayOrder")
    public Object getCurrentAndUnpayOrder(@LoginUser Integer userId) {
        return yopOrderService.getCurrentAndUnpayOrder(userId);
    }

    /**
     * 获取订单列表
     *
     * @param userId 用户ID
     * @param showType showType
     * @param page page
     * @param limit limit
     * @return 获取订单列表
     */
    @GetMapping("getOrderList")
    public Object getOrderList(@LoginUser Integer userId, Integer showType,
                               @RequestParam(defaultValue = "1") Integer page,
                               @RequestParam(defaultValue = "10") Integer limit,
                               @RideSort @RequestParam(defaultValue = "create_time") String sort,
                               @Order @RequestParam(defaultValue = "desc") String order) {
        return yopOrderService.getOrderList(userId, showType, page, limit, sort, order);
    }

    /**
     * 获取订单状态
     *
     * @param userId 用户ID
     * @param rideOrderId 订单ID
     * @return 获取订单状态
     */
    @GetMapping("getStatus")
    public Object getStatus(@LoginUser Integer userId, @RequestParam(defaultValue = "1", name = "ride_order_id") Long rideOrderId) {
        return yopOrderService.getStatus(userId, rideOrderId);
    }

    /**
     * 获取订单详情
     *
     * @param userId 用户ID
     * @param rideOrderId 订单ID
     * @return 获取订单详情
     */
    @GetMapping("getOrderInfo")
    public Object getOrderInfo(@LoginUser Integer userId, @RequestParam(defaultValue = "1", name = "ride_order_id") Long rideOrderId) {
        return yopOrderService.getOrderInfo(userId, rideOrderId);
    }

    /**
     * 回调
     *
     * https://github.com/yongche/developer.yongche.com/wiki/order#callback
     *
     * id	订单状态	加推字段	说明
     1)	服务结束	abnormal	异常状态 详见“异常状态说明” 表
     2)	取消	reason_id	合作方取消全部是100，易到取消为实际原因id
     3)	调整金额	adjust_flag = 1 & abnormal
     4)	扣款	withdraw_flag = 1

     * @param yongcheOrderId 易到订单号
     * @param yongcheOrderStatus 订单状态
     * @param abnormal 异常状态
     * @param reasonId 取消原因id
     * @param adjustFlag 调整金额
     * @param withdrawFlag 扣款
     * @return 回调
     */
    @GetMapping("callback1")
    public Object callback1(@RequestParam(defaultValue = "1", name = "yongche_order_id") String yongcheOrderId,
                  @RequestParam(defaultValue = "4", name = "yongche_order_status") String yongcheOrderStatus,
                  @RequestParam(defaultValue = "", name = "abnormal") String abnormal,
                  @RequestParam(defaultValue = "", name = "reason_id") String reasonId,
                  @RequestParam(defaultValue = "", name = "adjust_flag") String adjustFlag,
                  @RequestParam(defaultValue = "", name = "withdraw_flag") String withdrawFlag) {
        logger.debug(String.format("yop callback, yongche_order_id:%s, yongche_order_status:%s", yongcheOrderId, yongcheOrderStatus));
        return ResponseUtil.ok();
    }

    /**
     * 回调
     *
     * https://github.com/yongche/developer.yongche.com/wiki/order#callback
     *
     * id	订单状态	加推字段	说明
     1)	服务结束	abnormal	异常状态 详见“异常状态说明” 表
     2)	取消	reason_id	合作方取消全部是100，易到取消为实际原因id
     3)	调整金额	adjust_flag = 1 & abnormal
     4)	扣款	withdraw_flag = 1

     * @param httpServletRequest httpServletRequest
     * @return 回调
     */
    @GetMapping("callback")
    public Object callback(HttpServletRequest httpServletRequest) {
        logger.debug(JacksonUtil.toJson(httpServletRequest.getParameterMap()));
        return yopOrderService.updateByCallback(httpServletRequest);
    }

    /**
     * 取消订单
     * @param userId userId
     * @param body 订单ID
     * @return 取消订单
     */
    @PostMapping("cancel")
    public Object cancel(@LoginUser Integer userId, @RequestBody String body) {
        return yopOrderService.cancel(userId, body);
    }

    /**
     * 取消订单费用
     * @param userId userId
     * @param rideOrderId 订单ID
     * @return 取消订单费用
     */
    @GetMapping("getCancelOrderFee")
    public Object getCancelOrderFee(@LoginUser Integer userId, @RequestParam(defaultValue = "1", name = "ride_order_id") Long rideOrderId) {
        return yopOrderService.getCancelOrderFee(userId, rideOrderId);
    }

    /**
     * 订单行驶轨迹
     * @param userId userId
     * @param rideOrderId 订单ID
     * @return 订单行驶轨迹
     */
    @GetMapping("getOrderTrack")
    public Object getOrderTrack(@LoginUser Integer userId, @RequestParam(defaultValue = "1", name = "ride_order_id") Long rideOrderId) {
        return yopOrderService.getOrderTrack(userId, rideOrderId);
    }

    /**
     * 司机位置
     * @param userId userId
     * @param rideOrderId 订单ID
     * @return 司机位置
     */
    @GetMapping("getDriverLocation")
    public Object getDriverLocation(@LoginUser Integer userId, @RequestParam(defaultValue = "1", name = "ride_order_id") Long rideOrderId) {
        return yopOrderService.getDriverLocation(userId, rideOrderId);
    }

    /**
     * 获取订单计费信息
     * @param userId userId
     * @param rideOrderId 订单ID
     * @return 订单计费信息
     */
    @GetMapping("getCostDetail")
    public Object getCostDetail(@LoginUser Integer userId, @RequestParam(defaultValue = "1", name = "ride_order_id") Long rideOrderId) {
        return yopOrderService.getCostDetail(userId, rideOrderId);
    }
}
