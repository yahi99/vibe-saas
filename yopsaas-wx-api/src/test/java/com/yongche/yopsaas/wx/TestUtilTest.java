package com.yongche.yopsaas.wx;

import org.junit.Test;

public class TestUtilTest {
    @Test
    public void test() {
        String mobile = "";
        System.out.println(mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        mobile = "16811116667";
        System.out.println(mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        Byte status = 7;
        System.out.println(status == 7);
    }
}
