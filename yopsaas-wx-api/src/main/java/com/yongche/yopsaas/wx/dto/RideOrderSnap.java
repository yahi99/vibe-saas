package com.yongche.yopsaas.wx.dto;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RideOrderSnap {
    private long ycOrderId = 0;
    private String city = "bj";
    private String fixedFee = "0";
    private String parkingAmount = "0";
    private String highwayAmount = "0";
    private String ftDistance = "0";
    private String ftDistanceDanjia = "0";
    private String ftFee = "0";
    private String originAmount = "0";
    private String couponFacevalue = "0";
    private String orderAmount = "0";

    private String timeLength = "0";
    private String actualTimeLength = "0";
    private String kiloLength = "0";
    private String feePerHour = "0";
    private String timeFee = "0";
    private String feePerKilo = "0";
    private String kiloFee = "0";
    private String extraDistance = "0";
    private String extraDistanceDanjia = "0";
    private String extraDistanceFee = "0";
    private String extraTime = "0";
    private String extraTimeDanjia = "0";
    private String extraTimeFee = "0";
    private String airportServiceAmount = "0";
    private String nightAmount = "0";
    private String adjustFee = "0";
    private String otherFee = "0";
    private String otherFeeMsg = "0";
    private String actualBoardTime = "0";
    private String actualOffTime = "0";
    private String actualCarType = "0";
    private String timeLengthMinute = "0";
    private String extraTimeMinute = "0";
    private String feePerMinute = "0";

    private String cancelOrderAmount = "0";

    private List<Detail> details;
    private List<Detail> combos;
    private List<Detail> addPrice;
    private List<Detail> discount;
    private List<Detail> realPay;

    private final static Map<String, String> DETAIL_COLUMN = new HashMap<String, String>(){{
        put("timeFee", "时租总费用");
        put("kiloFee", "公里总费用");
        put("extraDistanceFee", "超公里费");
        put("extraTimeFee", "超小时总费用");
        put("parkingAmount", "停车费");
        put("highwayAmount", "高速费");
        put("airportServiceAmount", "机场服务费");
        put("nightAmount", "夜间服务费费");
        put("ftFee", "空驶费");
        put("otherFee", "其他费用金额");
    }};

    private final static Map<String, String> REALPAY_COLUMN = new HashMap<String, String>(){{
        put("orderAmount", "应付金额");
        //put("couponFacevalue", "优惠金额");
        //put("originAmount", "订单原始金额");
    }};

    public void make() {
        this.details = new ArrayList<Detail>();
        for (Map.Entry<String, String> entry : DETAIL_COLUMN.entrySet()) {
            Detail detail = new Detail();
            detail.setTitle(entry.getValue());
            detail.setFee(this.getFieldValueByFieldMethod(entry.getKey(), this));
            this.details.add(detail);
        }
        this.combos = new ArrayList<Detail>();
        this.addPrice = new ArrayList<>();
        this.discount = new ArrayList<>();

        this.realPay = new ArrayList<Detail>();
        for (Map.Entry<String, String> entry : REALPAY_COLUMN.entrySet()) {
            Detail detail = new Detail();
            detail.setTitle(entry.getValue());
            detail.setFee(this.getFieldValueByFieldMethod(entry.getKey(), this));
            this.realPay.add(detail);
        }
    }

    /**
     * 根据属性名获取属性值
     *
     * @param fieldName
     * @param object
     * @return
     */
    private String getFieldValueByFieldName(String fieldName, Object object) {
        try {
            Field field = object.getClass().getField(fieldName);
            //设置对象的访问权限，保证对private的属性的访问
            //field.setAccessible(true);
            return (String) field.get(object);
        } catch (Exception e) {
            return null;
        }
    }

    private String getFieldValueByFieldMethod(String fieldName, Object object) {
        try {
            String method = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method invokeMethod = object.getClass().getDeclaredMethod(method);
            return (String) invokeMethod.invoke(object);
        } catch (Exception e) {
            return null;
        }
    }

    public static class Detail {
        private String title = "";
        private String subTitle = "";
        private String subTitleUrl = "";
        private String fee = "";
        private String unit = "元";

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getSubTitle() {
            return subTitle;
        }

        public void setSubTitle(String subTitle) {
            this.subTitle = subTitle;
        }

        public String getSubTitleUrl() {
            return subTitleUrl;
        }

        public void setSubTitleUrl(String subTitleUrl) {
            this.subTitleUrl = subTitleUrl;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }
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

    public String getCancelOrderAmount() {
        return cancelOrderAmount;
    }

    public void setCancelOrderAmount(String cancelOrderAmount) {
        this.cancelOrderAmount = cancelOrderAmount;
    }

    public List<Detail> getDetails() {
        return details;
    }

    public void setDetails(List<Detail> details) {
        this.details = details;
    }

    public List<Detail> getCombos() {
        return combos;
    }

    public void setCombos(List<Detail> combos) {
        this.combos = combos;
    }

    public List<Detail> getAddPrice() {
        return addPrice;
    }

    public void setAddPrice(List<Detail> addPrice) {
        this.addPrice = addPrice;
    }

    public List<Detail> getDiscount() {
        return discount;
    }

    public void setDiscount(List<Detail> discount) {
        this.discount = discount;
    }

    public List<Detail> getRealPay() {
        return realPay;
    }

    public void setRealPay(List<Detail> realPay) {
        this.realPay = realPay;
    }
}
