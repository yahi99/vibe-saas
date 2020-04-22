package com.yongche.yopsaas.wx.dto;

public class UserInfo {
    private String nickName;
    private String avatarUrl;
    private String country;
    private String province;
    private String city;
    private String language;
    private String mobile;
    private Byte gender;

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

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

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        this.mobile = mobile;
    }

    public Byte getGender() {
        return gender;
    }

    public void setGender(Byte gender) {
        this.gender = gender;
    }
}
