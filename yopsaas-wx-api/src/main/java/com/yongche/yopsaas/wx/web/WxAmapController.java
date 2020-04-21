package com.yongche.yopsaas.wx.web;

import com.github.zhangchunsheng.amapgeo.bean.result.GeoResult;
import com.github.zhangchunsheng.amapgeo.exception.AmapGeoException;
import com.github.zhangchunsheng.amapgeo.service.GeoService;
import com.ridegroup.yop.bean.price.PriceNew;
import com.yongche.yopsaas.core.util.ResponseUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * amap 服务
 */
@RestController
@RequestMapping("/wx/amap")
@Validated
public class WxAmapController {
    private final Log logger = LogFactory.getLog(WxAmapController.class);

    @Autowired
    private GeoService geoService;

    /**
     * 价格数据
     *
     * @param address 地址
     * @return 价格数据
     */
    @GetMapping("geo")
    public Object index(@RequestParam(defaultValue = "中国技术交易大厦") String address) {
        try {
            GeoResult data = geoService.geo(address);
            return ResponseUtil.ok(data);
        } catch(AmapGeoException e) {
            return ResponseUtil.fail(Integer.valueOf(e.getReturnInfoCode()), e.getReturnInfo());
        }
    }
}
