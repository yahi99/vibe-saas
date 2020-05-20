package com.yongche.yopsaas.admin.service;

import com.github.binarywang.wxpay.bean.request.WxPayRefundRequest;
import com.github.binarywang.wxpay.bean.result.WxPayRefundResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.yongche.yopsaas.core.notify.NotifyService;
import com.yongche.yopsaas.core.notify.NotifyType;
import com.yongche.yopsaas.core.util.JacksonUtil;
import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.db.domain.UserVo;
import com.yongche.yopsaas.db.domain.YopsaasComment;
import com.yongche.yopsaas.db.domain.YopsaasRideOrder;
import com.yongche.yopsaas.db.domain.YopsaasOrderGoods;
import com.yongche.yopsaas.db.service.*;
import com.yongche.yopsaas.db.util.RideOrderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static com.yongche.yopsaas.admin.util.AdminResponseCode.*;

@Service

public class AdminRideOrderService {
    private final Log logger = LogFactory.getLog(AdminRideOrderService.class);

    @Autowired
    private YopsaasRideOrderService orderService;
    @Autowired
    private YopsaasUserService userService;
    @Autowired
    private WxPayService wxPayService;
    @Autowired
    private NotifyService notifyService;
    @Autowired
    private LogHelper logHelper;

    public Object list(Long userId, Long rideOrderId, int start, int end, List<Byte> orderStatusArray,
                       Integer page, Integer limit, String sort, String order) {
        List<YopsaasRideOrder> orderList = orderService.querySelective(userId, rideOrderId, start, end, orderStatusArray, page, limit,
                sort, order);
        List<Map<String, Object>> rets = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for(int i = 0 ; i < orderList.size() ; i++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("rideOrderId", orderList.get(i).getRideOrderId());
            map.put("userId", orderList.get(i).getUserId());
            map.put("ycOrderId", String.valueOf(orderList.get(i).getYcOrderId()));
            map.put("productTypeId", orderList.get(i).getProductTypeId());
            map.put("expectStartTime", orderList.get(i).getExpectStartTime());
            map.put("startAddress", orderList.get(i).getStartAddress());
            map.put("startPosition", orderList.get(i).getStartPosition());
            map.put("endAddress", orderList.get(i).getEndAddress());
            map.put("endPosition", orderList.get(i).getEndPosition());
            map.put("status", orderList.get(i).getStatus());
            map.put("payStatus", orderList.get(i).getPayStatus());
            map.put("totalAmount", orderList.get(i).getTotalAmount());
            map.put("deposit", orderList.get(i).getDeposit());
            map.put("payTime", orderList.get(i).getPayTime());
            map.put("expectStartTimeF", df.format(new Date(orderList.get(i).getExpectStartTime().longValue() * 1000)));
            map.put("payTimeF", df.format(new Date(orderList.get(i).getPayTime().longValue() * 1000)));
            map.put("refundStatus", orderList.get(i).getRefundStatus());
            rets.add(map);
        }
        return ResponseUtil.okList(rets, orderList);
    }

    public Object detail(Long rideOrderId) {
        YopsaasRideOrder order = orderService.findById(rideOrderId);
        UserVo user = userService.findUserVoById(order.getUserId().intValue());
        Map<String, Object> data = new HashMap<>();
        Map<String, Object> orderTime = new HashMap<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        orderTime.put("payTime", df.format(new Date(order.getPayTime().longValue() * 1000)));
        orderTime.put("refundTime", df.format(new Date(order.getRefundTime().longValue() * 1000)));
        data.put("order", order);
        data.put("orderTime", orderTime);
        data.put("user", user);

        return ResponseUtil.ok(data);
    }

    /**
     * 订单退款
     * <p>
     * 1. 检测当前订单是否能够退款;
     * 2. 微信退款操作;
     * 3. 设置订单退款确认状态；
     * 4. 订单商品库存回库。
     * <p>
     * TODO
     * 虽然接入了微信退款API，但是从安全角度考虑，建议开发者删除这里微信退款代码，采用以下两步走步骤：
     * 1. 管理员登录微信官方支付平台点击退款操作进行退款
     * 2. 管理员登录yopsaas管理后台点击退款操作进行订单状态修改和商品库存回库
     *
     * @param body 订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
    @Transactional
    public Object refund(String body) {
        Long orderId = JacksonUtil.parseLong(body, "rideOrderId");
        String refundMoney = JacksonUtil.parseString(body, "refundMoney");
        if (orderId == null) {
            return ResponseUtil.badArgument();
        }
        if (StringUtils.isEmpty(refundMoney)) {
            return ResponseUtil.badArgument();
        }

        YopsaasRideOrder rideOrder = orderService.findById(orderId);
        if (rideOrder == null) {
            return ResponseUtil.badArgument();
        }

        if (rideOrder.getDeposit().compareTo(new BigDecimal(refundMoney)) != 0) {
            return ResponseUtil.badArgumentValue();
        }

        // 如果订单不是退款状态，则不能退款
        if (!rideOrder.getStatus().equals(RideOrderUtil.STATUS_SERVICE_DONE)
            && !rideOrder.getStatus().equals(RideOrderUtil.STATUS_SERVICE_CANCEL)) {
            return ResponseUtil.fail(ORDER_CONFIRM_NOT_ALLOWED, "订单不能确认完成");
        }

        // 微信退款
        WxPayRefundRequest wxPayRefundRequest = new WxPayRefundRequest();
        wxPayRefundRequest.setOutTradeNo(rideOrder.getWxOrderSn());
        wxPayRefundRequest.setOutRefundNo("refund_" + rideOrder.getWxOrderSn());
        // 元转成分
        Integer totalFee = rideOrder.getDeposit().multiply(new BigDecimal(100)).intValue();
        wxPayRefundRequest.setTotalFee(totalFee);
        wxPayRefundRequest.setRefundFee(totalFee);

        WxPayRefundResult wxPayRefundResult;
        try {
            wxPayRefundResult = wxPayService.refund(wxPayRefundRequest);
        } catch (WxPayException e) {
            logger.error(e.getMessage(), e);
            return ResponseUtil.fail(ORDER_REFUND_FAILED, "订单退款失败");
        }
        if (!wxPayRefundResult.getReturnCode().equals("SUCCESS")) {
            logger.warn("refund fail: " + wxPayRefundResult.getReturnMsg());
            return ResponseUtil.fail(ORDER_REFUND_FAILED, "订单退款失败");
        }
        if (!wxPayRefundResult.getResultCode().equals("SUCCESS")) {
            logger.warn("refund fail: " + wxPayRefundResult.getReturnMsg());
            return ResponseUtil.fail(ORDER_REFUND_FAILED, "订单退款失败");
        }

        int now = YopsaasRideOrderService.getSecondTimestamp(new Date());
        // 设置订单退款状态
        rideOrder.setRefundStatus(YopsaasRideOrderService.REFUND_STATUS_DONE);
        // 记录订单退款相关信息
        rideOrder.setRefundAmount(rideOrder.getDeposit());
        rideOrder.setRefundType("微信退款接口");
        rideOrder.setRefundContent(wxPayRefundResult.getRefundId());
        rideOrder.setRefundTime(now);
        if (orderService.updateWithOptimisticLocker(rideOrder) == 0) {
            throw new RuntimeException("更新数据已失效");
        }

        //TODO 发送邮件和短信通知，这里采用异步发送
        // 退款成功通知用户, 例如“您申请的订单退款 [ 单号:{1} ] 已成功，请耐心等待到账。”
        // 注意订单号只发后6位
        notifyService.notifySmsTemplate(
                rideOrder.getPassengerPhone(), NotifyType.REFUND,
                new String[]{rideOrder.getWxOrderSn().substring(8, 14)}
                );

        logHelper.logOrderSucceed("退款", "订单编号 " + rideOrder.getWxOrderSn());
        return ResponseUtil.ok();
    }

    /**
     * 发货
     * 1. 检测当前订单是否能够发货
     * 2. 设置订单发货状态
     *
     * @param body 订单信息，{ orderId：xxx, shipSn: xxx, shipChannel: xxx }
     * @return 订单操作结果
     * 成功则 { errno: 0, errmsg: '成功' }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    public Object ship(String body) {
        Long orderId = JacksonUtil.parseLong(body, "orderId");
        String shipSn = JacksonUtil.parseString(body, "shipSn");
        String shipChannel = JacksonUtil.parseString(body, "shipChannel");
        if (orderId == null || shipSn == null || shipChannel == null) {
            return ResponseUtil.badArgument();
        }

        YopsaasRideOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }

        // 如果订单不是已付款状态，则不能发货
        if (!order.getStatus().equals(RideOrderUtil.STATUS_SERVICE_DONE)) {
            return ResponseUtil.fail(ORDER_CONFIRM_NOT_ALLOWED, "订单不能确认收货");
        }

        order.setStatus(RideOrderUtil.STATUS_SERVICE_DONE);
        if (orderService.updateWithOptimisticLocker(order) == 0) {
            return ResponseUtil.updatedDateExpired();
        }

        //TODO 发送邮件和短信通知，这里采用异步发送
        // 发货会发送通知短信给用户:          *
        // "您的订单已经发货，快递公司 {1}，快递单 {2} ，请注意查收"
        notifyService.notifySmsTemplate(order.getPassengerPhone(), NotifyType.SHIP, new String[]{shipChannel, shipSn});

        logHelper.logOrderSucceed("发货", "订单编号 " + order.getWxOrderSn());
        return ResponseUtil.ok();
    }

    /**
     * 删除订单
     * 1. 检测当前订单是否能够删除
     * 2. 删除订单
     *
     * @param body 订单信息，{ orderId：xxx }
     * @return 订单操作结果
     * 成功则 { errno: 0, errmsg: '成功' }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    public Object delete(String body) {
        Long orderId = JacksonUtil.parseLong(body, "orderId");
        YopsaasRideOrder order = orderService.findById(orderId);
        if (order == null) {
            return ResponseUtil.badArgument();
        }

        // 如果订单不是关闭状态(已取消、系统取消、已退款、用户已确认、系统已确认)，则不能删除
        Byte status = order.getStatus();
        if (status.equals(RideOrderUtil.STATUS_SERVICE_DONE)) {
            return ResponseUtil.fail(ORDER_DELETE_FAILED, "订单不能删除");
        }
        // 删除订单
        orderService.deleteById(orderId);
        logHelper.logOrderSucceed("删除", "订单编号 " + order.getWxOrderSn());
        return ResponseUtil.ok();
    }

}
