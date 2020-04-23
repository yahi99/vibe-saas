package com.yongche.yopsaas.wx.service;

import com.yongche.yopsaas.core.system.SystemConfig;
import com.yongche.yopsaas.core.task.TaskService;
import com.yongche.yopsaas.core.util.JacksonUtil;
import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.core.yop.OrderService;
import com.yongche.yopsaas.db.domain.*;
import com.yongche.yopsaas.db.service.YopsaasRideOrderService;
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
     * 1. 创建订单表项;
     *
     * @param userId 用户ID
     * @param body   打车订单信息
     * @return 提交订单操作结果
     */
    public Object create(Integer userId, String body) {
        if (userId == null) {
            return ResponseUtil.unlogin();
        }
        if (body == null) {
            return ResponseUtil.badArgument();
        }
        /*
         * is_support_system_decision: '1', //只有系统派单，后面需要调整，根据配置调整
            has_custom_decision: '0', //只有系统派单，后面需要调整，根据配置调整
            order_lat: startLat,
            order_lng: startLng,
            start_address: startAddressParam, //startAddressParam,  //详细地址，比如路牌号码
            start_lat: startLat,
            start_lng: startLng,
            from_pos: startPosition, //startPosition,  //大厦名字
            end_address: endAddress.address,
            end_lat: endAddress.lat,
            end_lng: endAddress.lng,
            to_pos: endAddress.name,
            dst_city_name: endAddress.city,
            dest_city: vm.data.destCityShort,
            city: startCityShort,
            is_asap: 1, //只有随叫随到，后面需要调整
            estimate_price: estimatePrice.total_fee,
            distance: distance,
            time_length: timeLength,
            time: 0,
            car_type_id: estimatePrice.car_type_id,
            start_time: start_time,
            in_coord_type: 'mars',
            passenger_phone: userInfo.cellphone,
            is_taximeter: '0', //是否打表来接
            estimate_info: estimate_info,
            product_type_id: '1', //需要从选择产品类型获取
            passenger_countryshort: userInfo.countryshort, //user中获取
            passenger_name: userInfo.name, //user中获取
            out_coord_type: 'mars',
            is_bargain: '0', //是否支持议价
            passenger_sms: '1', //是否可以给乘客发送短信, 1 or 0
            corporate_id: '0', //企业账户
            corporate_dept_id: '0', //企业账号组id
            is_need_manual_dispatch: is_need_manual_dispatch, //是否派单失败后转人工, ASAP 强制0，其他默认1
            pa_bargain_amount: 0, //	议价金额*/
        String isSupportSystemDecision = JacksonUtil.parseString(body, "is_support_system_decision");
        String hasCustomDecision = JacksonUtil.parseString(body, "has_custom_decision");
        Double orderLat = JacksonUtil.parseDouble(body, "order_lat");
        Double orderLng = JacksonUtil.parseDouble(body, "order_lng");
        String startAddress = JacksonUtil.parseString(body, "start_address");
        Double startLat = JacksonUtil.parseDouble(body, "start_lat");
        Double startLng = JacksonUtil.parseDouble(body, "start_lng");
        String fromPos = JacksonUtil.parseString(body, "from_pos");
        String endAddress = JacksonUtil.parseString(body, "end_address");
        Double endLat = JacksonUtil.parseDouble(body, "end_lat");
        Double endLng = JacksonUtil.parseDouble(body, "end_lng");
        String toPos = JacksonUtil.parseString(body, "to_pos");
        String dstCityName = JacksonUtil.parseString(body, "dst_city_name");
        String destCity = JacksonUtil.parseString(body, "dest_city");
        String city = JacksonUtil.parseString(body, "city");
        Integer isAsap = JacksonUtil.parseInteger(body, "is_asap");
        Double estimatePrice = JacksonUtil.parseDouble(body, "estimate_price");
        Integer distance = JacksonUtil.parseInteger(body, "distance");
        Integer timeLength = JacksonUtil.parseInteger(body, "time_length");
        Integer time = JacksonUtil.parseInteger(body, "time");
        Integer carTypeId = JacksonUtil.parseInteger(body, "car_type_id");
        Integer startTime = JacksonUtil.parseInteger(body, "start_time");
        String inCoordType = JacksonUtil.parseString(body, "in_coord_type");
        String passengerPhone = JacksonUtil.parseString(body, "passenger_phone");
        String isTaximeter = JacksonUtil.parseString(body, "is_taximeter");
        String estimateInfo = JacksonUtil.parseString(body, "estimate_info");
        String productTypeId = JacksonUtil.parseString(body, "product_type_id");
        String passengerCountryshort = JacksonUtil.parseString(body, "passenger_countryshort");
        String passengerName = JacksonUtil.parseString(body, "passenger_name");
        String outCoordType = JacksonUtil.parseString(body, "out_coord_type");
        String isBargain = JacksonUtil.parseString(body, "is_bargain");
        String passengerSms = JacksonUtil.parseString(body, "passenger_sms");
        String corporateId = JacksonUtil.parseString(body, "corporate_id");
        String corporateDeptId = JacksonUtil.parseString(body, "corporate_dept_id");
        Integer isNeedManualDispatch = JacksonUtil.parseInteger(body, "is_need_manual_dispatch");
        Integer paBargainAmount = JacksonUtil.parseInteger(body, "pa_bargain_amount");

        if (startLat == null || startLng == null || endLat == null || endLng == null) {
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
