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

        public void setDefaultValue() {
            this.ycOrderId = 0;
            this.city = "bj";
            this.fixedFee = "0";
            this.parkingAmount = "0";
            this.highwayAmount = "0";
            this.ftDistance = "0";
            this.ftDistanceDanjia = "0";
            this.ftFee = "0";
            this.originAmount = "0";
            this.couponFacevalue = "0";
            this.orderAmount = "0";

            this.timeLength = "0";
            this.actualTimeLength = "0";
            this.kiloLength = "0";
            this.feePerHour = "0";
            this.timeFee = "0";
            this.feePerKilo = "0";
            this.kiloFee = "0";
            this.extraDistance = "0";
            this.extraDistanceDanjia = "0";
            this.extraDistanceFee = "0";
            this.extraTime = "0";
            this.extraTimeDanjia = "0";
            this.extraTimeFee = "0";
            this.airportServiceAmount = "0";
            this.nightAmount = "0";
            this.adjustFee = "0";
            this.otherFee = "0";
            this.otherFeeMsg = "0";
            this.actualBoardTime = "0";
            this.actualOffTime = "0";
            this.actualCarType = "0";
            this.timeLengthMinute = "0";
            this.extraTimeMinute = "0";
            this.feePerMinute = "0";
        }

        public long getYcOrderId() {
            return ycOrderId;
        }

        public void setYcOrderId(long ycOrderId) {
            this.ycOrderId = ycOrderId;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getFixedFee() {
            return fixedFee;
        }

        public void setFixedFee(String fixedFee) {
            this.fixedFee = fixedFee;
        }

        public String getParkingAmount() {
            return parkingAmount;
        }

        public void setParkingAmount(String parkingAmount) {
            this.parkingAmount = parkingAmount;
        }

        public String getHighwayAmount() {
            return highwayAmount;
        }

        public void setHighwayAmount(String highwayAmount) {
            this.highwayAmount = highwayAmount;
        }

        public String getFtDistance() {
            return ftDistance;
        }

        public void setFtDistance(String ftDistance) {
            this.ftDistance = ftDistance;
        }

        public String getFtDistanceDanjia() {
            return ftDistanceDanjia;
        }

        public void setFtDistanceDanjia(String ftDistanceDanjia) {
            this.ftDistanceDanjia = ftDistanceDanjia;
        }

        public String getFtFee() {
            return ftFee;
        }

        public void setFtFee(String ftFee) {
            this.ftFee = ftFee;
        }

        public String getOriginAmount() {
            return originAmount;
        }

        public void setOriginAmount(String originAmount) {
            this.originAmount = originAmount;
        }

        public String getCouponFacevalue() {
            return couponFacevalue;
        }

        public void setCouponFacevalue(String couponFacevalue) {
            this.couponFacevalue = couponFacevalue;
        }

        public String getOrderAmount() {
            return orderAmount;
        }

        public void setOrderAmount(String orderAmount) {
            this.orderAmount = orderAmount;
        }

        public String getTimeLength() {
            return timeLength;
        }

        public void setTimeLength(String timeLength) {
            this.timeLength = timeLength;
        }

        public String getActualTimeLength() {
            return actualTimeLength;
        }

        public void setActualTimeLength(String actualTimeLength) {
            this.actualTimeLength = actualTimeLength;
        }

        public String getKiloLength() {
            return kiloLength;
        }

        public void setKiloLength(String kiloLength) {
            this.kiloLength = kiloLength;
        }

        public String getFeePerHour() {
            return feePerHour;
        }

        public void setFeePerHour(String feePerHour) {
            this.feePerHour = feePerHour;
        }

        public String getTimeFee() {
            return timeFee;
        }

        public void setTimeFee(String timeFee) {
            this.timeFee = timeFee;
        }

        public String getFeePerKilo() {
            return feePerKilo;
        }

        public void setFeePerKilo(String feePerKilo) {
            this.feePerKilo = feePerKilo;
        }

        public String getKiloFee() {
            return kiloFee;
        }

        public void setKiloFee(String kiloFee) {
            this.kiloFee = kiloFee;
        }

        public String getExtraDistance() {
            return extraDistance;
        }

        public void setExtraDistance(String extraDistance) {
            this.extraDistance = extraDistance;
        }

        public String getExtraDistanceDanjia() {
            return extraDistanceDanjia;
        }

        public void setExtraDistanceDanjia(String extraDistanceDanjia) {
            this.extraDistanceDanjia = extraDistanceDanjia;
        }

        public String getExtraDistanceFee() {
            return extraDistanceFee;
        }

        public void setExtraDistanceFee(String extraDistanceFee) {
            this.extraDistanceFee = extraDistanceFee;
        }

        public String getExtraTime() {
            return extraTime;
        }

        public void setExtraTime(String extraTime) {
            this.extraTime = extraTime;
        }

        public String getExtraTimeDanjia() {
            return extraTimeDanjia;
        }

        public void setExtraTimeDanjia(String extraTimeDanjia) {
            this.extraTimeDanjia = extraTimeDanjia;
        }

        public String getExtraTimeFee() {
            return extraTimeFee;
        }

        public void setExtraTimeFee(String extraTimeFee) {
            this.extraTimeFee = extraTimeFee;
        }

        public String getAirportServiceAmount() {
            return airportServiceAmount;
        }

        public void setAirportServiceAmount(String airportServiceAmount) {
            this.airportServiceAmount = airportServiceAmount;
        }

        public String getNightAmount() {
            return nightAmount;
        }

        public void setNightAmount(String nightAmount) {
            this.nightAmount = nightAmount;
        }

        public String getAdjustFee() {
            return adjustFee;
        }

        public void setAdjustFee(String adjustFee) {
            this.adjustFee = adjustFee;
        }

        public String getOtherFee() {
            return otherFee;
        }

        public void setOtherFee(String otherFee) {
            this.otherFee = otherFee;
        }

        public String getOtherFeeMsg() {
            return otherFeeMsg;
        }

        public void setOtherFeeMsg(String otherFeeMsg) {
            this.otherFeeMsg = otherFeeMsg;
        }

        public String getActualBoardTime() {
            return actualBoardTime;
        }

        public void setActualBoardTime(String actualBoardTime) {
            this.actualBoardTime = actualBoardTime;
        }

        public String getActualOffTime() {
            return actualOffTime;
        }

        public void setActualOffTime(String actualOffTime) {
            this.actualOffTime = actualOffTime;
        }

        public String getActualCarType() {
            return actualCarType;
        }

        public void setActualCarType(String actualCarType) {
            this.actualCarType = actualCarType;
        }

        public String getTimeLengthMinute() {
            return timeLengthMinute;
        }

        public void setTimeLengthMinute(String timeLengthMinute) {
            this.timeLengthMinute = timeLengthMinute;
        }

        public String getExtraTimeMinute() {
            return extraTimeMinute;
        }

        public void setExtraTimeMinute(String extraTimeMinute) {
            this.extraTimeMinute = extraTimeMinute;
        }

        public String getFeePerMinute() {
            return feePerMinute;
        }

        public void setFeePerMinute(String feePerMinute) {
            this.feePerMinute = feePerMinute;
        }
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

        public void setDefaultValue() {
            this.cancelOrderAmount = "0";
        }

        public String getCancelOrderAmount() {
            return cancelOrderAmount;
        }

        public void setCancelOrderAmount(String cancelOrderAmount) {
            this.cancelOrderAmount = cancelOrderAmount;
        }
    }
}
