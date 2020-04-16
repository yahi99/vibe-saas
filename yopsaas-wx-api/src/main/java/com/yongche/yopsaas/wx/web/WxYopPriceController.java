package com.yongche.yopsaas.wx.web;

import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.core.yop.PriceService;
import com.yongche.yopsaas.wx.annotation.LoginUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * YOP服务
 */
@RestController
@RequestMapping("/wx/yopprice")
@Validated
public class WxYopPriceController {
    private final Log logger = LogFactory.getLog(WxYopPriceController.class);

    @Autowired
    private PriceService priceService;

    /**
     * 价格数据
     *
     * @param city 城市
     * @param productType 产品类型
     * @return 价格数据
     */
    @GetMapping("index")
    public Object index(@RequestParam(defaultValue = "bj") String city,
                        @RequestParam(defaultValue = "17", name = "product_type") String productType) {

        Map<Object, Object> data = new HashMap<Object, Object>();
        data.put("price", priceService.getPrice(city, productType));
        return ResponseUtil.ok(data);
    }

}