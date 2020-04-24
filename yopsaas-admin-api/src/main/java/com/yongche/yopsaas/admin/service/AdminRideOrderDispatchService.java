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
import com.yongche.yopsaas.db.domain.YopsaasRideOrder;
import com.yongche.yopsaas.db.domain.YopsaasRideOrderDispatch;
import com.yongche.yopsaas.db.service.YopsaasRideOrderDispatchService;
import com.yongche.yopsaas.db.service.YopsaasRideOrderService;
import com.yongche.yopsaas.db.service.YopsaasUserService;
import com.yongche.yopsaas.db.util.RideOrderDispatchUtil;
import com.yongche.yopsaas.db.util.RideOrderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.*;

import static com.yongche.yopsaas.admin.util.AdminResponseCode.*;

@Service

public class AdminRideOrderDispatchService {
    private final Log logger = LogFactory.getLog(AdminRideOrderDispatchService.class);

    @Autowired
    private YopsaasRideOrderDispatchService orderDispatchService;
    @Autowired
    private YopsaasUserService userService;
    @Autowired
    private LogHelper logHelper;

    public Object list(Long rideOrderId, Long rideOrderDispatchId, int start, int end, List<Byte> orderStatusArray,
                       Integer page, Integer limit, String sort, String order) {
        List<YopsaasRideOrderDispatch> orderDispatchList = orderDispatchService.querySelective(rideOrderId, rideOrderDispatchId, start, end, orderStatusArray, page, limit,
                sort, order);
        return ResponseUtil.okList(orderDispatchList);
    }

    public Object detail(Long rideOrderDispatchId) {
        YopsaasRideOrderDispatch orderDispatch = orderDispatchService.findById(rideOrderDispatchId);
        Map<String, Object> data = new HashMap<>();
        data.put("orderDispatch", orderDispatch);

        return ResponseUtil.ok(data);
    }

    /**
     * 删除派单
     * 1. 检测当前订单是否能够删除
     * 2. 删除派单
     *
     * @param body 订单信息，{ orderId：xxx }
     * @return 订单操作结果
     * 成功则 { errno: 0, errmsg: '成功' }
     * 失败则 { errno: XXX, errmsg: XXX }
     */
    public Object delete(String body) {
        Long orderDispatchId = JacksonUtil.parseLong(body, "orderDispatchId");
        YopsaasRideOrderDispatch orderDispatch = orderDispatchService.findById(orderDispatchId);
        if (orderDispatch == null) {
            return ResponseUtil.badArgument();
        }

        // 如果订单不是关闭状态(已取消、系统取消、已退款、用户已确认、系统已确认)，则不能删除
        Byte status = orderDispatch.getStatus();
        if (status.equals(RideOrderDispatchUtil.STATUS_SELECTED)) {
            return ResponseUtil.fail(ORDER_DELETE_FAILED, "派单不能删除");
        }
        // 删除订单
        orderDispatchService.deleteById(orderDispatchId);
        logHelper.logOrderSucceed("删除", "派单编号 " + orderDispatch.getRideOrderDispatchId());
        return ResponseUtil.ok();
    }

}
