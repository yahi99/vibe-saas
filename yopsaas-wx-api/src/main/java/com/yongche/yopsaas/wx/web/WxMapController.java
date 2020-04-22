package com.yongche.yopsaas.wx.web;

import com.github.zhangchunsheng.amapgeo.bean.result.RegeoResult;
import com.github.zhangchunsheng.amapgeo.exception.AmapGeoException;
import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.wx.dto.LocationInfo;
import com.yongche.yopsaas.wx.dto.PlaceSearch;
import com.yongche.yopsaas.wx.service.MapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * YOP服务
 */
@RestController
@RequestMapping("/wx/map")
@Validated
public class WxMapController {
    @Autowired
    private MapService mapService;

    /**
     * Map定位
     *
     * @param location 经纬度
     * @return LocationInfo
     */
    @GetMapping("location")
    public Object location(@RequestParam(defaultValue = "116.307487,39.984123") String location) {
        LocationInfo data = mapService.location(location);
        return ResponseUtil.ok(data);
    }

    /**
     * 地址搜索
     *
     * @param city 城市
     * @param keywords 关键字
     * @return LocationInfo
     */
    @GetMapping("placesearch")
    public Object placeSearch(@RequestParam(defaultValue = "北京") String city,
                            @RequestParam(defaultValue = "技术交易大厦") String keywords) {
        List<PlaceSearch> data = mapService.placeSearch(city, keywords);
        return ResponseUtil.ok(data);
    }
}
