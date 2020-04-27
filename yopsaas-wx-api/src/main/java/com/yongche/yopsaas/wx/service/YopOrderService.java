package com.yongche.yopsaas.wx.service;

import com.ridegroup.yop.api.OrderAPI;
import com.ridegroup.yop.bean.BaseResult;
import com.ridegroup.yop.bean.BaseResultT;
import com.ridegroup.yop.bean.driver.DriverInfo;
import com.ridegroup.yop.bean.order.*;
import com.yongche.yopsaas.core.system.SystemConfig;
import com.yongche.yopsaas.core.task.TaskService;
import com.yongche.yopsaas.core.util.JacksonUtil;
import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.core.yop.OrderService;
import com.yongche.yopsaas.db.domain.*;
import com.yongche.yopsaas.db.service.YopsaasRideDriverService;
import com.yongche.yopsaas.db.service.YopsaasRideOrderExtService;
import com.yongche.yopsaas.db.service.YopsaasRideOrderService;
import com.yongche.yopsaas.db.service.YopsaasUserService;
import com.yongche.yopsaas.wx.task.RideOrderDecisionDriver;
import com.yongche.yopsaas.wx.task.RideOrderUnchooseCarTask;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

import static com.yongche.yopsaas.wx.util.WxResponseCode.*;
import static com.yongche.yopsaas.wx.util.WxResponseCode.GROUPON_JOIN;

@Service
public class YopOrderService {
    private final Log logger = LogFactory.getLog(YopOrderService.class);

    @Autowired
    private OrderService orderService;
    @Autowired
    private YopsaasRideOrderService rideOrderService;
    @Autowired
    private YopsaasRideOrderExtService rideOrderExtService;
    @Autowired
    private YopsaasRideDriverService rideDriverService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private YopsaasUserService userService;

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
        YopsaasUser user = userService.findById(userId);
        String isSupportSystemDecision = JacksonUtil.parseString(body, "is_support_system_decision", "1");
        String hasCustomDecision = JacksonUtil.parseString(body, "has_custom_decision", "0");
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
        Byte isAsap = JacksonUtil.parseByte(body, "is_asap");
        Double estimatePrice = JacksonUtil.parseDouble(body, "estimate_price");
        Integer distance = JacksonUtil.parseInteger(body, "distance");
        Integer timeLength = JacksonUtil.parseInteger(body, "time_length");
        Integer time = JacksonUtil.parseInteger(body, "time");
        Integer carTypeId = JacksonUtil.parseInteger(body, "car_type_id");
        Integer startTime = JacksonUtil.parseInteger(body, "start_time");
        String inCoordType = JacksonUtil.parseString(body, "in_coord_type");
        String passengerPhone = JacksonUtil.parseString(body, "passenger_phone");
        String isTaximeter = JacksonUtil.parseString(body, "is_taximeter", "0");
        String estimateInfo = JacksonUtil.parseString(body, "estimate_info");
        String productTypeId = JacksonUtil.parseString(body, "product_type_id", "1");
        String passengerCountryshort = JacksonUtil.parseString(body, "passenger_countryshort");
        String passengerName = JacksonUtil.parseString(body, "passenger_name");
        String outCoordType = JacksonUtil.parseString(body, "out_coord_type");
        String isBargain = JacksonUtil.parseString(body, "is_bargain", "0");
        String passengerSms = JacksonUtil.parseString(body, "passenger_sms", "1");
        String corporateId = JacksonUtil.parseString(body, "corporate_id", "0");
        String corporateDeptId = JacksonUtil.parseString(body, "corporate_dept_id", "0");
        Integer isNeedManualDispatch = JacksonUtil.parseInteger(body, "is_need_manual_dispatch", 0);
        Integer paBargainAmount = JacksonUtil.parseInteger(body, "pa_bargain_amount");

        if (startLat == null || startLng == null || endLat == null || endLng == null) {
            return ResponseUtil.badArgument();
        }

        Long flag = 0L;

        isSupportSystemDecision = "0";
        hasCustomDecision = "1";

        if(isSupportSystemDecision.equals("0")) {
            flag = flag | YopsaasRideOrderService.FLAG_NOT_SUPPORT_SYSTEM_DECISION;
        }
        if(hasCustomDecision.equals("1")) {
            flag = flag | YopsaasRideOrderService.FLAG_IS_CUSTOM_DECISION;
        }
        if(isBargain.equals("1")) {
            flag = flag | YopsaasRideOrderService.FLAG_BARGAIN;
        }
        if(isTaximeter.equals("1")) {
            flag = flag | YopsaasRideOrderService.FLAG_TAXIMETER;
        }
        if(isNeedManualDispatch == 1) {
            flag = flag | YopsaasRideOrderService.FLAG_SUPPORT_MANUAL_DISPATCH;
        }
        Byte status = YopsaasRideOrderService.ORDER_STATUS_WAITFORCAR;
        Long rideOrderId = null;
        YopsaasRideOrder order = null;
        YopsaasRideOrderExtWithBLOBs orderExt = null;
        // 订单
        order = new YopsaasRideOrder();
        order.setUserId(Long.valueOf(userId));
        order.setCity(city);
        order.setProductTypeId(Integer.valueOf(productTypeId));
        order.setIsAsap(isAsap);
        order.setCarTypeId(carTypeId);
        order.setCarTypeIds(String.valueOf(carTypeId));
        order.setStartAddress(startAddress);
        order.setStartPosition(fromPos);
        //order.setStartLatitude(startLat);
        //order.setStartLongitude(startLng);
        order.setExpectStartLatitude(startLat);
        order.setExpectStartLongitude(startLng);
        //order.setStartTime(startTime);
        order.setExpectStartTime(startTime);
        order.setEndAddress(endAddress);
        order.setEndPosition(toPos);
        //order.setEndLatitude(endLat);
        //order.setEndLongitude(endLng);
        order.setExpectEndLatitude(endLat);
        order.setExpectEndLongitude(endLng);
        order.setPassengerName(passengerName);
        order.setPassengerPhone(user.getMobile());
        order.setUserPhone(user.getMobile());
        order.setTimeLength(timeLength);
        order.setFlag(flag);
        order.setCorporateId(Long.valueOf(corporateId));
        order.setCorporateDeptId(Integer.valueOf(corporateDeptId));
        order.setStatus(status);
        int now = YopOrderService.getTimestamp();
        order.setCreateTime(now);
        order.setInitTime(now);

        // 添加订单表项
        rideOrderService.add(order);
        rideOrderId = order.getRideOrderId();

        // order ext
        orderExt = new YopsaasRideOrderExtWithBLOBs();
        orderExt.setRideOrderId(rideOrderId);
        orderExt.setDestCity(destCity);
        orderExt.setDstCityName(dstCityName);
        orderExt.setCreateOrderLatitude(orderLat);
        orderExt.setCreateOrderLongitude(orderLng);
        orderExt.setEstimateSnap(estimateInfo);
        orderExt.setSms(Integer.valueOf(passengerSms));

        rideOrderExtService.add(orderExt);

        logger.debug("ride_order_id: " + rideOrderId + " order data: " + JacksonUtil.toJson(order));

        Map<String, Object> reqMap = this.getCreateOrderParams(order);
        CreateOrderResult result = orderService.create(reqMap);

        logger.debug(JacksonUtil.toJson(reqMap) + " yop ret:" + JacksonUtil.toJson(result));

        if(result.getCode().equals("200")) {
            // 订单未选择司机
            String ycOrderId = result.getResult().getOrder_id();
            taskService.addTask(new RideOrderUnchooseCarTask(rideOrderId));
            taskService.addTask(new RideOrderDecisionDriver(rideOrderId, ycOrderId));

            status = YopsaasRideOrderService.ORDER_STATUS_WAITDRIVERCONFIRM;
            YopsaasRideOrder updateOrder = new YopsaasRideOrder();
            updateOrder.setRideOrderId(rideOrderId);
            updateOrder.setYcOrderId(Long.valueOf(ycOrderId));
            updateOrder.setStatus(status);
            updateOrder.setConfirmTime(YopOrderService.getTimestamp());
            logger.debug(JacksonUtil.toJson(updateOrder) + " yop ret:" + JacksonUtil.toJson(result));
            rideOrderService.update(updateOrder);

            Map<String, Object> data = new HashMap<>();
            data.put("rideOrderId", rideOrderId);

            return ResponseUtil.ok(data);
        } else {
            List<String> codes = new ArrayList<String>();
            codes.add("400");
            codes.add("500");
            if(codes.contains(result.getCode())) {
                status = YopsaasRideOrderService.ORDER_STATUS_CANCELLED;
                YopsaasRideOrder updateOrder = new YopsaasRideOrder();
                updateOrder.setRideOrderId(rideOrderId);
                updateOrder.setStatus(status);
                updateOrder.setCancelTime(YopOrderService.getTimestamp());
                rideOrderService.update(updateOrder);
            }
            // TODO check order status
            return ResponseUtil.fail(Integer.valueOf(result.getCode()), result.getMsg());
        }
    }

    public Object getCurrentAndUnpayOrder(Integer userId) {
        Long uid = Long.valueOf(userId);
        List<Byte> orderStatus = new ArrayList<Byte>();
        orderStatus.add(YopsaasRideOrderService.ORDER_STATUS_SERVICEREADY);
        orderStatus.add(YopsaasRideOrderService.ORDER_STATUS_ARRIVED);
        orderStatus.add(YopsaasRideOrderService.ORDER_STATUS_SERVICESTART);
        List<YopsaasRideOrder> currentOrderList = rideOrderService.queryByOrderStatus(uid, orderStatus);
        List<Byte> payStatus = new ArrayList<>();
        payStatus.add(YopsaasRideOrderService.PAY_STATUS_NONE);
        List<YopsaasRideOrder> unPayOrderList = rideOrderService.queryByPayStatus(uid, payStatus);

        List<Object> currentTrips = new ArrayList<>();
        List<Object> unPayTrips = new ArrayList<>();
        List<Integer> driverIds = new ArrayList<>();
        Map<String, YopsaasRideDriver> drivers = new HashMap<>();
        for(YopsaasRideOrder currentOrder : currentOrderList) {
            driverIds.add(currentOrder.getDriverId());
        }
        for(YopsaasRideOrder unPayOrder : unPayOrderList) {
            driverIds.add(unPayOrder.getDriverId());
        }
        if(driverIds.size() > 0) {
            List<YopsaasRideDriver> driverList = rideDriverService.queryByYcDriverIds(driverIds);
            for(YopsaasRideDriver driver : driverList) {
                drivers.put(String.valueOf(driver.getYcDriverId()), driver);
            }
        }
        for(YopsaasRideOrder currentOrder : currentOrderList) {
            String driverId = String.valueOf(currentOrder.getDriverId());
            Map<String, Object> currentTrip = new HashMap<>();
            currentTrip.put("order", currentOrder);
            currentTrip.put("driver", drivers.get(driverId));
            currentTrips.add(currentTrip);
        }
        for(YopsaasRideOrder unPayOrder : unPayOrderList) {
            String driverId = String.valueOf(unPayOrder.getDriverId());
            Map<String, Object> unPayTrip = new HashMap<>();
            unPayTrip.put("order", unPayOrder);
            unPayTrip.put("driver", drivers.get(driverId));
            unPayTrips.add(unPayTrip);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("current_trip", currentTrips);
        data.put("unpay_trip", unPayTrips);
        return ResponseUtil.ok(data);
    }

    public Object getStatus(Integer userId, Long rideOrderId) {
        if(rideOrderId != null) {
            YopsaasRideOrder rideOrder = rideOrderService.findById(rideOrderId);
            if(rideOrder != null) {
                Long dbUserId = rideOrder.getUserId();
                if(dbUserId.intValue() != userId) {
                    return ResponseUtil.fail(ResponseUtil.RET_INVALID_PARAM_400, "rideOrderId is not yours");
                }
                Byte status = rideOrder.getStatus();
                Map<String, Object> data = new HashMap<>();
                data.put("status", status);
                return ResponseUtil.ok(data);
            } else {
                return ResponseUtil.badArgument();
            }
        } else {
            return ResponseUtil.fail();
        }
    }

    public Object getOrderInfo(Integer userId, Long rideOrderId) {
        if(rideOrderId != null) {
            YopsaasRideOrder rideOrder = rideOrderService.findById(rideOrderId);
            if(rideOrder != null) {
                Long dbUserId = rideOrder.getUserId();
                if(dbUserId.intValue() != userId) {
                    return ResponseUtil.fail(ResponseUtil.RET_INVALID_PARAM_400, "rideOrderId is not yours");
                }
                Map<String, Object> data = new HashMap<>();
                YopsaasRideDriver driverInfo = null;
                if(rideOrder.getDriverId() > 0) {
                    driverInfo = rideDriverService.findByYcDriverId(rideOrder.getDriverId());
                }
                data.put("order", rideOrder);
                data.put("driver", driverInfo);
                return ResponseUtil.ok(data);
            } else {
                return ResponseUtil.badArgument();
            }
        } else {
            return ResponseUtil.fail();
        }
    }

    public Object updateByCallback(HttpServletRequest httpServletRequest) {
        String yongcheOrderId = httpServletRequest.getParameter("yongche_order_id");
        String yongcheOrderStatus = httpServletRequest.getParameter("yongche_order_status");

        if (yongcheOrderId == null || yongcheOrderStatus == null) {
            return ResponseUtil.badArgumentCode();
        }
        Byte orderStatus = Byte.valueOf(yongcheOrderStatus);
        List<Byte> currentTripStatus = YopsaasRideOrderService.getCurrentTripStatus();
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        example.or().andYcOrderIdEqualTo(Long.valueOf(yongcheOrderId));
        YopsaasRideOrder rideOrder = rideOrderService.queryOneByExample(example);
        if(rideOrder == null) {
            return ResponseUtil.failCode(404, "not exist");
        }
        OrderInfo orderInfo = orderService.getOrderInfo(yongcheOrderId);
        if(orderInfo == null) {
            return ResponseUtil.failCode(404, "yop order not exist");
        }
        if(rideOrder.getStatus().equals(orderStatus)) {
            return ResponseUtil.failCode(400, "order already update");
        }
        if(rideOrder.getStatus() > Byte.valueOf(yongcheOrderStatus)) {
            return ResponseUtil.failCode(400, "yop status is lower");
        }
        logger.debug("callback api, get yop order info:" + JacksonUtil.toJson(orderInfo));
        YopsaasRideOrder updateOrder = new YopsaasRideOrder();
        updateOrder.setStatus(orderStatus);
        if(currentTripStatus.contains(orderStatus)) {
            // update driver
            if(!orderInfo.getDriver_id().equals("")) {
                Integer ycDriverId = Integer.valueOf(orderInfo.getDriver_id());
                updateOrder.setDriverId(ycDriverId);
                updateOrder.setDriverName(orderInfo.getDriver_name());
                updateOrder.setDriverPhone(orderInfo.getDriver_phone());
                updateOrder.setVehicleNumber(orderInfo.getVehicle_number().replace("车牌号", ""));
                updateOrder.setCarTypeId(orderInfo.getCar_type_id());
                updateOrder.setCarType(orderInfo.getCar_type());
                updateOrder.setCarBrand(orderInfo.getCar_brand());
                updateOrder.setCarColor(orderInfo.getColor());
                updateOrder.setConfirmTime(orderInfo.getConfirm_time());
                updateOrder.setPayable(YopsaasRideOrderService.PAYABLE_ALLOW);
                // add driver info
                YopsaasRideDriver driver = rideDriverService.findByYcDriverId(ycDriverId);
                DriverInfo driverInfo = orderService.getOrderDriverInfo(yongcheOrderId);
                if(driver == null) {
                    //add
                    driver = new YopsaasRideDriver();
                    /*{"driver_id":0,
                    "name":"易师傅",
                    "score":0,
                    "good_comment_rate":0,
                    "unittime_complete_count":36,
                    "is_served":0,
                    "latitude":0,
                    "longitude":0,
                    "brand":"奥迪 A3",
                    "car_type":null,
                    "car_type_id":0,
                    "is_default":0,
                    "photo":null,
                    "gender":"男",
                    "driving_years":6,
                    "star_level":2,
                    "vehicle_number":"京U23456",
                    "car_setup":"",
                    "car_company_name":"",
                    "driver_company_name":"北京易快行技术服务有限责任公司",
                    "is_default_photo":0,
                    "photo_url":"https://i3.yongche.name/media/g1/M00/01/06/rBAApVUmFBKIfcrKAAE4EwGNvvMAAAbpgGVXMYAATgr708.png",
                    "cellphone":"16818862925"}*/
                    driver.setYcDriverId(ycDriverId);
                    driver.setName(driverInfo.getName());
                    driver.setCellphone(driverInfo.getCellphone());
                    driver.setVehicleNumber(driverInfo.getVehicle_number());
                    driver.setGender(driverInfo.getGender());
                    driver.setDrivingYears(driverInfo.getDriving_years());
                    driver.setCarSetup(driverInfo.getCar_setup());
                    driver.setCarCompanyName(driverInfo.getCar_company_name());
                    driver.setDriverCompanyName(driverInfo.getDriver_company_name());
                    driver.setUnittimeCompleteCount(driverInfo.getUnittime_complete_count());
                    driver.setBrand(driverInfo.getBrand());
                    driver.setCarType(orderInfo.getCar_type());
                    driver.setCarTypeId(orderInfo.getCar_type_id());
                    driver.setStarLevel(driverInfo.getStar_level());
                    driver.setIsDefault(Short.valueOf(String.valueOf(driverInfo.getIs_default_photo())));
                    driver.setPhoto(driverInfo.getPhoto_url());
                    rideDriverService.add(driver);
                } else {
                    //update
                    YopsaasRideDriver updateDriver = new YopsaasRideDriver();
                    updateDriver.setName(driverInfo.getName());
                    updateDriver.setCellphone(driverInfo.getCellphone());
                    updateDriver.setVehicleNumber(driverInfo.getVehicle_number());
                    updateDriver.setGender(driverInfo.getGender());
                    updateDriver.setDrivingYears(driverInfo.getDriving_years());
                    updateDriver.setCarSetup(driverInfo.getCar_setup());
                    updateDriver.setCarCompanyName(driverInfo.getCar_company_name());
                    updateDriver.setDriverCompanyName(driverInfo.getDriver_company_name());
                    updateDriver.setUnittimeCompleteCount(driverInfo.getUnittime_complete_count());
                    updateDriver.setBrand(driverInfo.getBrand());
                    updateDriver.setCarType(orderInfo.getCar_type());
                    updateDriver.setCarTypeId(orderInfo.getCar_type_id());
                    updateDriver.setStarLevel(driverInfo.getStar_level());
                    updateDriver.setIsDefault(Short.valueOf(String.valueOf(driverInfo.getIs_default_photo())));
                    updateDriver.setPhoto(driverInfo.getPhoto_url());
                    rideDriverService.updateByYcDriverId(driver, ycDriverId);
                }
            }
            if(orderStatus.equals(YopsaasRideOrderService.ORDER_STATUS_SERVICESTART)) {
                updateOrder.setStartLatitude(orderInfo.getStart_latitude());
                updateOrder.setStartLongitude(orderInfo.getStart_longitude());
                updateOrder.setStartTime(orderInfo.getStart_time());
            }
        }
        if(orderStatus.equals(YopsaasRideOrderService.ORDER_STATUS_SERVICEEND)) {
            // update amount
            updateOrder.setEndTime(orderInfo.getEnd_time());
            updateOrder.setOriginAmount(BigDecimal.valueOf(Double.valueOf(orderInfo.getTotal_amount())));
            updateOrder.setTotalAmount(BigDecimal.valueOf(Double.valueOf(orderInfo.getTotal_amount())));
            updateOrder.setActualTimeLength(orderInfo.getTime_length());
            updateOrder.setEndLatitude(orderInfo.getEnd_latitude());
            updateOrder.setEndLongitude(orderInfo.getStart_longitude());
            updateOrder.setEndTime(orderInfo.getEnd_time());
            if(orderInfo.getTotal_amount().equals("0")) {
                // add task?
            }
        }
        if(orderStatus.equals(YopsaasRideOrderService.ORDER_STATUS_CANCELLED)) {
            updateOrder.setCancelTime(YopOrderService.getTimestamp());
        }

        int result = rideOrderService.updateByExample(updateOrder, example);
        return ResponseUtil.okCode(result);
    }

    public Object cancel(Integer userId, String body) {
        Long rideOrderId = JacksonUtil.parseLong(body, "ride_order_id");
        if(rideOrderId != null) {
            YopsaasRideOrder rideOrder = rideOrderService.findById(rideOrderId);
            if(rideOrder != null) {
                Long dbUserId = rideOrder.getUserId();
                if(dbUserId.intValue() != userId) {
                    return ResponseUtil.fail(ResponseUtil.RET_INVALID_PARAM_400, "rideOrderId is not yours");
                }
                String ycOrderId = String.valueOf(rideOrder.getYcOrderId());
                BaseResultT<CancelOrder> cancelOrder = orderService.cancel(ycOrderId, "", "");
                if(cancelOrder.getCode().equals("200")) {
                    YopsaasRideOrder updateOrder = new YopsaasRideOrder();
                    updateOrder.setRideOrderId(rideOrderId);
                    updateOrder.setStatus(YopsaasRideOrderService.ORDER_STATUS_CANCELLED);
                    if(cancelOrder.getResult().getFee() > 0) {
                        updateOrder.setPayable(YopsaasRideOrderService.PAYABLE_ALLOW);
                        updateOrder.setPayStatus(YopsaasRideOrderService.PAY_STATUS_NONE);
                        updateOrder.setTotalAmount(BigDecimal.valueOf(cancelOrder.getResult().getFee()));
                    }
                    // TODO reason
                    updateOrder.setReasonId(0);

                    int result = rideOrderService.update(updateOrder);
                    return ResponseUtil.ok(cancelOrder.getResult());
                } else {
                    return ResponseUtil.fail(cancelOrder.getCode(), cancelOrder.getMsg());
                }
            } else {
                return ResponseUtil.badArgument();
            }
        } else {
            return ResponseUtil.badArgument();
        }
    }

    public Object getCancelOrderFee(Integer userId, Long rideOrderId) {
        if(rideOrderId != null) {
            YopsaasRideOrder rideOrder = rideOrderService.findById(rideOrderId);
            if (rideOrder != null) {
                Long dbUserId = rideOrder.getUserId();
                if (dbUserId.intValue() != userId) {
                    return ResponseUtil.fail(ResponseUtil.RET_INVALID_PARAM_400, "rideOrderId is not yours");
                }
                String ycOrderId = String.valueOf(rideOrder.getYcOrderId());
                CancelOrderFee orderFee = orderService.getCancelOrderFee(ycOrderId);
                /*
                * allow_cancel	是否允许取消 0：不允许、1：允许
                cancel_order_amount	取消订单罚金金额
                state	罚金的说明*/
                return ResponseUtil.ok(orderFee);
            } else {
                return ResponseUtil.badArgument();
            }
        } else {
            return ResponseUtil.badArgument();
        }
    }

    public Object getOrderTrack(Integer userId, Long rideOrderId) {
        if(rideOrderId != null) {
            YopsaasRideOrder rideOrder = rideOrderService.findById(rideOrderId);
            if (rideOrder != null) {
                Long dbUserId = rideOrder.getUserId();
                if (dbUserId.intValue() != userId) {
                    return ResponseUtil.fail(ResponseUtil.RET_INVALID_PARAM_400, "rideOrderId is not yours");
                }
                String ycOrderId = String.valueOf(rideOrder.getYcOrderId());
                List<Position> positions = orderService.getOrderTrack(ycOrderId);
                if(positions == null) {
                    return ResponseUtil.fail(ResponseUtil.RET_NOT_FOUND_404, "没有轨迹数据");
                } else {
                    return ResponseUtil.ok(positions);
                }
            } else {
                return ResponseUtil.badArgument();
            }
        } else {
            return ResponseUtil.badArgument();
        }
    }

    public AcceptedDriver getSelectDriver(String orderId, String driverIds) {
        return orderService.getSelectDriver(orderId, driverIds);
    }

    public BaseResult decisionDriver(String orderId, String driverId) {
        return orderService.decisionDriver(orderId, driverId);
    }

    public static int getTimestamp() {
        long time = System.currentTimeMillis();
        return Math.round(time / 1000);
    }

    public Map<String, Object> getCreateOrderParams(YopsaasRideOrder order) {
        /*
         * 名称	含义	说明(*必填)	举例	校验规则
        city	城市简码	*	bj	请从服务接口取
        type	产品类型	*	2	同上
        aircode	机场三字码	接送机必填	PEK	请从机场接口取
        flight_number	航班号	不是必须，只是记录一下	ca1801	2位（数字或者字母） + 2-4位（数字）
        car_type_id	车型	*	2	请从服务价格接口取
        start_position	出发地点	*	总部基地	最大长度 utf8 100
        start_address	出发详细地点		总部基地详细	最大长度 utf8 100
        expect_start_longitude	出发地点经度	*	116.458637
        expect_start_latitude	出发地点纬度	*	39.955538
        time	出发时间	*，随叫随到可以不填	2013-04-19 11:22:33	时间格式如前面举例，其他格式会报错
        rent_time	使用时长	单位：小时,默认1	2
        end_position	目的地点	送机必填	总部基地	最大长度 utf8 100
        end_address	目的详细地点		总部基地详细	最大长度 utf8 100
        map_type	坐标类型	1：百度，2：火星 3-谷歌 默认值：1
        expect_end_longitude	目的地点经度	同end_position	116.373055
        expect_end_latitude	目的地点纬度	同end_position	39.911093
        passenger_name	乘车人姓名	*	test	最大长度 utf8 30
        passenger_phone	乘车人电话	*	111111111	1[345678][\d]{9}
        passenger_number	乘车人数	默认1	2
        invoice	是否需要发票		1	请调用发票接口，这里准备废弃
        receipt_title	发票抬头	invoice为1必填	**有限公司	同上
        receipt_content	发票内容	invoice为1必填	打车费	同上
        address	发票邮寄地址	invoice为1必填	总部基地	同上
        postcode	邮政编码	invoice为1必填	100000	同上
        receipt_user	发票接收人姓名			同上
        receipt_phone	发票接收人电话			同上
        msg	客户留言			最大长度 utf8 30
        is_asap	是否随叫随到	1是随叫随到的订单，0是普通订单
        is_face_pay	是否当面付	1面付，0或不传为非面付
        third_party_coupon	优惠券金额	100
        sms_type	是否给乘车人短信	1：发短信 0：不发短信	对于yop企业，特殊处理，把乘车人当做订车人接收短信
        ad	第三方的额外信息，对账用，json格式	可选	{"wid":1}	准备废弃
        app_trade_no	第三方订单号，字符串，不可重复,用于排除重复订单请求，请尽量填写	可选	order_12345	用之前重复的app_trade_no，会返回之前的易到订单号
        dep_date_local	飞机起飞日期，接机产品如果传值，司机可收到航班信息	可选	2014-04-21
        app_user_id	第三方用户唯一标识，用于判断取消订单	必填		最大长度 utf8 20
        appoint_price	新一口价价格，用于校验价格是否一致	新一品价日租、半日租必填

        reqMap.put("city", "bj");
        reqMap.put("type", "7");
        reqMap.put("aircode", "PEK");
        reqMap.put("car_type_id", "2");
        reqMap.put("start_position", "颐和园");
        reqMap.put("expect_start_latitude", "39.955538");
        reqMap.put("expect_start_longitude", "116.458637");
        reqMap.put("time", df.format(calendar.getTime()));
        reqMap.put("rent_time", "2");
        reqMap.put("end_position", "总部基地");
        reqMap.put("expect_end_latitude", "39.911093");
        reqMap.put("expect_end_longitude", "116.373055");
        reqMap.put("passenger_name", "test");
        reqMap.put("passenger_phone", "16811116667");
        reqMap.put("sms_type", "1");
        reqMap.put("msg", "1");
        reqMap.put("app_trade_no", "ceshi" + time);*/
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("city", order.getCity());
        reqMap.put("type", String.valueOf(order.getProductTypeId()));
        reqMap.put("car_type_id", String.valueOf(order.getCarTypeId()));
        reqMap.put("start_position", order.getStartPosition());
        reqMap.put("start_address", order.getStartAddress());
        reqMap.put("expect_start_latitude", String.valueOf(order.getExpectStartLatitude()));
        reqMap.put("expect_start_longitude", String.valueOf((order.getExpectStartLongitude())));
        Date date = new Date(Long.valueOf(order.getExpectStartTime()) * 1000);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        reqMap.put("time", df.format(date));
        reqMap.put("rent_time", "2");
        reqMap.put("end_position", order.getEndPosition());
        reqMap.put("end_address", order.getEndAddress());
        reqMap.put("expect_end_latitude", String.valueOf(order.getExpectEndLatitude()));
        reqMap.put("expect_end_longitude", String.valueOf(order.getExpectEndLongitude()));
        reqMap.put("passenger_name", order.getPassengerName());
        reqMap.put("passenger_phone", order.getPassengerPhone());
        reqMap.put("sms_type", "1");// 对于yop企业，特殊处理，把乘车人当做订车人接收短信
        reqMap.put("msg", "第三方订单");// 客户留言
        reqMap.put("app_trade_no", "yopsaas" + order.getRideOrderId());

        return reqMap;
    }
}
