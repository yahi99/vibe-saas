package com.yongche.yopsaas.db;

import org.junit.Test;
import com.yongche.yopsaas.db.util.DbUtil;

import java.io.File;

public class DbUtilTest {
    @Test
    public void testBackup() {
        File file = new File("test.sql");
        DbUtil.backup(file, "yopsaas", "yopsaas123456", "yopsaas");
    }

//    这个测试用例会重置yopsaas数据库，所以比较危险，请开发者注意
//    @Test
    public void testLoad() {
        File file = new File("test.sql");
        DbUtil.load(file, "yopsaas", "yopsaas123456", "yopsaas");
    }
}
