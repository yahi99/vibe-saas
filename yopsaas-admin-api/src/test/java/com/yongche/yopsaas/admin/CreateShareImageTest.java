package com.yongche.yopsaas.admin;

import org.junit.Test;
import org.junit.runner.RunWith;
import com.yongche.yopsaas.core.qcode.QCodeService;
import com.yongche.yopsaas.db.domain.LitemallGoods;
import com.yongche.yopsaas.db.service.LitemallGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@WebAppConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class CreateShareImageTest {
    @Autowired
    QCodeService qCodeService;
    @Autowired
    LitemallGoodsService yopsaasGoodsService;

    @Test
    public void test() {
        LitemallGoods good = yopsaasGoodsService.findById(1181010);
        qCodeService.createGoodShareImage(good.getId().toString(), good.getPicUrl(), good.getName());
    }
}
