package com.yongche.yopsaas.wx.web;

import com.ridegroup.yop.api.BaseAPI;
import com.ridegroup.yop.bean.BaseResultT;
import com.ridegroup.yop.bean.toft.Estimated;
import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.core.yop.ToftService;
import com.yongche.yopsaas.wx.annotation.LoginUser;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * YOP服务
 */
@RestController
@RequestMapping("/wx/yopcoupon")
@Validated
public class WxYopCouponController {
    private final Log logger = LogFactory.getLog(WxYopCouponController.class);

    /**
     * getUserCouponList
     *
     * @param userId userId
     * @return 优惠券数据
     */
    @GetMapping("getUserCouponList")
    public Object getUserCouponList(@LoginUser Integer userId) {
        Map<String, Object> data = new HashMap<>();
        List<Object> available = new ArrayList<>();
        List<Object> unavailable = new ArrayList<>();
        data.put("available", available);
        data.put("unavailable", unavailable);
        return ResponseUtil.ok(data);
    }

    /**
     * useCoupon
     *
     * @param userId userId
     * @return 使用优惠券
     */
    @PostMapping("useCoupon")
    public Object useCoupon(@LoginUser Integer userId, @RequestBody String body) {
        return ResponseUtil.ok();
    }
}
