package com.yongche.yopsaas.db;

import com.yongche.yopsaas.db.util.IdGeneratorUtil;
import org.junit.Test;

public class IdGeneratorUtilTest {
    @Test
    public void test() {
        System.out.println(IdGeneratorUtil.getLongGuid());
    }
}
