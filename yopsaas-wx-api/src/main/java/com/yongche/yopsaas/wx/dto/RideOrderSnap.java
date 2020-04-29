package com.yongche.yopsaas.wx.dto;

import java.lang.reflect.Field;
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
        put("couponFacevalue", "优惠金额");
        put("originAmount", "订单原始金额");
    }};

    public void make() {
        this.details = new ArrayList<Detail>();
        for (Map.Entry<String, String> entry : DETAIL_COLUMN.entrySet()) {
            Detail detail = new Detail();
            detail.setTitle(entry.getValue());
            detail.setFee(this.getFieldValueByFieldName(entry.getKey(), this));
            this.details.add(detail);
        }
        this.combos = new ArrayList<Detail>();
        this.addPrice = new ArrayList<>();
        this.discount = new ArrayList<>();

        this.realPay = new ArrayList<Detail>();
        for (Map.Entry<String, String> entry : REALPAY_COLUMN.entrySet()) {
            Detail detail = new Detail();
            detail.setTitle(entry.getValue());
            detail.setFee(this.getFieldValueByFieldName(entry.getKey(), this));
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

            return (String) field.get(object);
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
}
