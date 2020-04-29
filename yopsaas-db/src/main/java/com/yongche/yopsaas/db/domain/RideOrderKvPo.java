package com.yongche.yopsaas.db.domain;

public class RideOrderKvPo {
    /**
     * code	状态	200：正确返回 400：发生错误
     result	数组，该订单信息
     time_length	计费时长	单位:小时
     actual_time_length	实际行驶时长	单位:秒 注意：不是小时，因为需要精确的时间
     kilo_length	实际公里数	单位:公里
     fee_per_hour	时租单价	单位:元/小时(时租产品专用)
     time_fee	时租总费用	单位:元(时租产品专用)
     fee_per_kilo	公里单价	单位:元/公里(时租产品专用)
     kilo_fee	公里总费用	单位:元(时租产品专用)
     fixed_fee	固定价格产品服务费	单位:元(固定价格产品专用)
     extra_distance	超公里数	单位:公里
     extra_distance_danjia	超公里单价	单位:元/公里
     extra_distance_fee	超公里费	单位:元
     extra_time	超小时	单位:小时
     extra_time_danjia	超时长单价	单位:元/小时
     extra_time_fee	超小时总费用	单位:元
     parking_amount	停车费
     highway_amount	高速费
     airport_service_amount	机场服务费
     night_amount	夜间服务费费
     kongshi_distance	空驶距离	单位:公里
     kongshi_distance_danjia	空驶单价	单位:元/公里
     kongshi_fee	空驶费
     origin_amount	订单原始金额
     coupon_facevalue	优惠金额
     order_amount	应付金额
     deposit	已付金额	已废弃
     pay_amount	未付金额	已废弃
     adjust_fee	调整金额	取值可以为正，也可为负
     other_fee	其他费用金额
     other_fee_msg	其他费用描述
     actual_board_time	实际开始时间	2014-01-02 23:50:34
     actual_off_time	实际结束时间	2014-01-03 00:58:24
     actual_car_type	车辆品牌	GL8
     time_length_minute	实际计费分钟	（单位：分钟）新增
     extra_time_minute	实际超出分钟	（单位：分钟）新增
     fee_per_minute	分钟单价	（单位：元）新增
     */
    public static class SNAP_SERVICEEND {
        private long ycOrderId;
        private String city;
        private String fixedFee;
        private String parkingAmount;
        private String highwayAmount;
        private String ftDistance;
        private String ftDistanceDanjia;
        private String ftFee;
        private String originAmount;
        private String couponFacevalue;
        private String orderAmount;
        //private int deposit;
        //private String payAmount;

        private String timeLength;
        private String actualTimeLength;
        private String kiloLength;
        private String feePerHour;
        private String timeFee;
        private String feePerKilo;
        private String kiloFee;
        private String extraDistance;
        private String extraDistanceDanjia;
        private String extraDistanceFee;
        private String extraTime;
        private String extraTimeDanjia;
        private String extraTimeFee;
        private String airportServiceAmount;
        private String nightAmount;
        private String adjustFee;
        private String otherFee;
        private String otherFeeMsg;
        private String actualBoardTime;
        private String actualOffTime;
        private String actualCarType;
        private String timeLengthMinute;
        private String extraTimeMinute;
        private String feePerMinute;
    }

    /**
     * code	状态	200：正确返回 400：发生错误
     result	数组，该订单信息
     cancel_order_amount	取消订单应付金额
     cancel_deposit	已付金额	已废弃
     cancel_pay_amount	未付金额	已废弃
     cancel_change_amount	退款金额	已废弃
     */
    public static class SNAP_CANCEL {
        private String cancelOrderAmount;
        //private int cancelDeposit;
        //private String cancelPayAmount;
        //private int cancelChangeAmount;
    }
}
