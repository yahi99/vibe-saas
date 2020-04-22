package com.yongche.yopsaas.wx.util;

public class StringUtil {
    public static String getPartMobile(String mobile) {
        mobile = mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        return mobile;
    }
}
