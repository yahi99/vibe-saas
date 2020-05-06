package com.yongche.yopsaas.wx.service;

import com.ridegroup.yop.api.BaseAPI;
import com.ridegroup.yop.bean.BaseResultT;
import com.ridegroup.yop.bean.price.PriceNew;
import com.ridegroup.yop.bean.toft.AvailableService;
import com.ridegroup.yop.bean.toft.Estimated;
import com.yongche.yopsaas.core.util.JacksonUtil;
import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.core.yop.ToftService;
import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.apache.commons.logging.LogFactory;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YopToftService {
    private final Log logger = LogFactory.getLog(YopToftService.class);

    @Autowired
    private ToftService toftService;

    public Object estimatedAll(String body) {
        /*
        * city	城市	*	bj
         type	产品类型	*	7
         aircode	机场三字码，接送机必传，接机则传到达机场三字码、送机则传起飞机场三字码接送机必填		PEK
         car_type_id	车型	*	2
         expect_start_longitude	出发地点经度	*	116.458637
         expect_start_latitude	出发地点纬度	*	39.955538
         expect_end_longitude	目的地点经度	*	116.373055
         expect_end_latitude	目的地点纬度	*	39.911093
         time	开始时间	*	2013-04-19 11:22:33
         rent_time	使用时长 单位：小时,时租必填		2
         map_type	地图类型 1-百度 2-火星 3-谷歌 默认 1-百度	*	2

        reqMap.put("city", "bj");
        reqMap.put("type", "7");
        reqMap.put("aircode", "PEK");
        if(hasCarType) {
            reqMap.put("car_type_id", "2");
        }
        reqMap.put("start_position", "颐和园");
        reqMap.put("expect_start_latitude", "39.955538");
        reqMap.put("expect_start_longitude", "116.458637");
        reqMap.put("time", df.format(calendar.getTime()));
        reqMap.put("rent_time", "2");
        reqMap.put("end_position", "总部基地");
        reqMap.put("expect_end_latitude", "39.911093");
        reqMap.put("expect_end_longitude", "116.373055");
        reqMap.put("map_type", BaseAPI.MAP_TYPE_MARS);*/
        String city = JacksonUtil.parseString(body, "city");
        String productType = JacksonUtil.parseString(body, "product_type_id");
        String startAddress = JacksonUtil.parseString(body, "start_address");
        String startLatitude = JacksonUtil.parseString(body, "start_latitude");
        String startLongitude = JacksonUtil.parseString(body, "start_longitude");
        String startTime = JacksonUtil.parseString(body, "start_time");
        String endAddress = JacksonUtil.parseString(body, "end_position");
        String endLatitude = JacksonUtil.parseString(body, "end_latitude");
        String endLongitude = JacksonUtil.parseString(body, "end_longitude");

        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("city", city);
        reqMap.put("type", productType);
        reqMap.put("start_position", startAddress);
        reqMap.put("expect_start_latitude", startLatitude);
        reqMap.put("expect_start_longitude", startLongitude);
        Date date = new Date(Long.valueOf(startTime) * 1000);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        reqMap.put("time", df.format(date));
        reqMap.put("rent_time", "2");
        reqMap.put("end_position", endAddress);
        reqMap.put("expect_end_latitude", endLatitude);
        reqMap.put("expect_end_longitude", endLongitude);
        reqMap.put("map_type", BaseAPI.MAP_TYPE_MARS);
        logger.debug(reqMap);

        BaseResultT<List<Estimated>> data = this.toftService.estimatedAll(reqMap);
        if(data.getCode().equals("200")) {
            return ResponseUtil.ok(data.getResult());
        } else {
            return ResponseUtil.fail(Integer.valueOf(data.getCode()), data.getMsg());
        }
    }

    public Object getAvailableService() {
        BaseResultT<Map<String, AvailableService>> data = this.toftService.getAvailableService();
        if(data.getCode().equals("200")) {
            return ResponseUtil.ok(data.getResult());
        } else {
            return ResponseUtil.fail(Integer.valueOf(data.getCode()), data.getMsg());
        }
    }

    public Object getPrice(String city, String productType, String airCode) {
        PriceNew data = this.toftService.getPrice(city, productType, airCode);
        if(data.getCode().equals("200")) {
            return ResponseUtil.ok(data.getResult());
        } else {
            return ResponseUtil.fail(Integer.valueOf(data.getCode()), data.getMsg());
        }
    }
}
