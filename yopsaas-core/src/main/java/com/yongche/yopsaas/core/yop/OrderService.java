package com.yongche.yopsaas.core.yop;

import com.ridegroup.yop.api.OrderAPI;
import com.ridegroup.yop.bean.BaseResult;
import com.ridegroup.yop.bean.BaseResultT;
import com.ridegroup.yop.bean.driver.DriverInfo;
import com.ridegroup.yop.bean.order.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

public class OrderService extends BaseService {
    private final Log logger = LogFactory.getLog(OrderService.class);

    /**
     * city	城市简码	*	bj	请从服务接口取
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
     * @param reqMap 请求参数
     * @return CreateOrderResult
     */
    public CreateOrderResult create(Map<String, Object> reqMap) {
        String accessToken = this.getProperties().getAccessToken();
        return OrderAPI.createOrder(accessToken, reqMap);
    }

    public OrderInfo getOrderInfo(String orderId) {
        String accessToken = this.getProperties().getAccessToken();
        BaseResultT<OrderInfo> result = OrderAPI.getOrderInfo(accessToken, orderId);
        if(result.getCode().equals("200")) {
            return result.getResult();
        } else {
            return null;
        }
    }

    public AcceptedDriver getSelectDriver(String orderId, String driverIds) {
        String accessToken = this.getProperties().getAccessToken();
        BaseResultT<AcceptedDriver> acceptedDriver = OrderAPI.getSelectDriver(accessToken, orderId, driverIds, OrderAPI.MAP_TYPE_MARS);
        if(acceptedDriver.getCode().equals("200")) {
            return acceptedDriver.getResult();
        } else {
            return null;
        }
    }

    public BaseResult decisionDriver(String orderId, String driverId) {
        String accessToken = this.getProperties().getAccessToken();
        return OrderAPI.decisionDriver(accessToken, orderId, driverId, "");
    }

    public DriverInfo getOrderDriverInfo(String orderId) {
        String accessToken = this.getProperties().getAccessToken();
        BaseResultT<DriverInfo> driverInfo = OrderAPI.getOrderDriverInfo(accessToken, orderId);
        if(driverInfo.getCode().equals("200")) {
            return driverInfo.getResult();
        } else {
            return null;
        }
    }

    public BaseResultT<CancelOrder> cancel(String orderId, String reasonId, String otherReason) {
        String accessToken = this.getProperties().getAccessToken();
        BaseResultT<CancelOrder> cancelOrder = OrderAPI.cancelOrder(accessToken, orderId, reasonId, otherReason);
        return cancelOrder;
    }

    public CancelOrderFee getCancelOrderFee(String orderId) {
        String accessToken = this.getProperties().getAccessToken();
        BaseResultT<CancelOrderFee> cancelOrder = OrderAPI.getCancelOrderFee(accessToken, orderId);
        if(cancelOrder.getCode().equals("200")) {
            return cancelOrder.getResult();
        } else {
            return null;
        }
    }
}
