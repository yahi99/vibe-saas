package com.yongche.yopsaas.wx.dto;

import com.alibaba.fastjson.annotation.JSONField;

public class LocationInfo {
    public final static String DEFAULT_CITY = "北京市";

    private int count = 0;

    @JSONField(name = "location_info")
    private Location locationInfo;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public Location getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(Location locationInfo) {
        this.locationInfo = locationInfo;
    }

    public static class Location {
        private String province = "北京市";
        private String city = "北京市";
        private String name = "北京";
        private String district = "朝阳区";
        private String street = "朝阳门外大街";
        private String street_number = "34号";
        private String business = "";
        private String formatted_address = "北京市朝阳区人民政府";
        private String building_name = "北京市朝阳区人民政府";
        @JSONField(name = "short")
        private String city_short = "bj";
        private String en = "BeiJing";
        private double lng = 116.44355;
        private double lat = 39.9219;
        private String code = "010";
        private String country = "CN";
        private String timezone = "Asia/Shanghai";
        private int region_code = 110000;

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public String getStreet_number() {
            return street_number;
        }

        public void setStreet_number(String street_number) {
            this.street_number = street_number;
        }

        public String getBusiness() {
            return business;
        }

        public void setBusiness(String business) {
            this.business = business;
        }

        public String getFormatted_address() {
            return formatted_address;
        }

        public void setFormatted_address(String formatted_address) {
            this.formatted_address = formatted_address;
        }

        public String getBuilding_name() {
            return building_name;
        }

        public void setBuilding_name(String building_name) {
            this.building_name = building_name;
        }

        public String getCity_short() {
            return city_short;
        }

        public void setCity_short(String city_short) {
            this.city_short = city_short;
        }

        public String getEn() {
            return en;
        }

        public void setEn(String en) {
            this.en = en;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public int getRegion_code() {
            return region_code;
        }

        public void setRegion_code(int region_code) {
            this.region_code = region_code;
        }
    }
}
