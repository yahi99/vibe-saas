package com.yongche.yopsaas.wx.web;

import com.ridegroup.yop.api.BaseAPI;
import com.ridegroup.yop.bean.BaseResultT;
import com.ridegroup.yop.bean.toft.Estimated;
import com.yongche.yopsaas.core.util.JacksonUtil;
import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.core.yop.ToftService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * YOP服务
 */
@RestController
@RequestMapping("/wx/yoptoft")
@Validated
public class WxYopToftController {
    private final Log logger = LogFactory.getLog(WxYopToftController.class);

    @Autowired
    private ToftService toftService;

    /**
     * 预估
     *
     * @param city 城市
     * @param productType 产品类型
     * @return 预估数据
     */
    @GetMapping("estimateAll")
    public Object estimateAll(@RequestParam(defaultValue = "bj") String city,
                  @RequestParam(defaultValue = "17", name = "product_type_id") String productType,
                  @RequestParam(defaultValue = "1587529853", name = "start_time") String startTime,
                  @RequestParam(defaultValue = "39.90469", name = "start_latitude") String startLatitude,
                  @RequestParam(defaultValue = "116.40717", name = "start_longitude") String startLongitude,
                  @RequestParam(defaultValue = "北京市东城区东华门街道正义路北京市人民政府(旧址)", name = "start_address") String startAddress,
                  @RequestParam(defaultValue = "39.927464", name = "end_latitude") String endLatitude,
                  @RequestParam(defaultValue = "116.333305", name = "end_longitude") String endLongitude,
                  @RequestParam(defaultValue = "NS专柜", name = "end_position") String endAddress) {
        /*
        * reqMap.put("city", "bj");
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
        Map<String, Object> reqMap = new HashMap<String, Object>();
        reqMap.put("city", city);
        reqMap.put("type", productType);
        reqMap.put("start_position", startAddress);
        reqMap.put("expect_start_latitude", startLatitude);
        reqMap.put("expect_start_longitude", startLongitude);
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //Date date = new Date(System.currentTimeMillis());
        Date date = new Date(Long.valueOf(startTime) * 1000);
        reqMap.put("time", date);
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
}
