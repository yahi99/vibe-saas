package com.yongche.yopsaas.admin.service;

import com.yongche.yopsaas.core.util.JacksonUtil;
import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.db.domain.YopsaasRideDriver;
import com.yongche.yopsaas.db.domain.YopsaasRideOrderDispatch;
import com.yongche.yopsaas.db.service.YopsaasRideDriverService;
import com.yongche.yopsaas.db.service.YopsaasRideOrderDispatchService;
import com.yongche.yopsaas.db.service.YopsaasUserService;
import com.yongche.yopsaas.db.util.RideOrderDispatchUtil;
import io.swagger.models.auth.In;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yongche.yopsaas.admin.util.AdminResponseCode.ORDER_DELETE_FAILED;

@Service

public class AdminRideDriverService {
    private final Log logger = LogFactory.getLog(AdminRideDriverService.class);

    @Autowired
    private YopsaasRideDriverService driverService;
    @Autowired
    private LogHelper logHelper;

    public Object list(Integer ycDriverId, String cellphone, int start, int end,
                       Integer page, Integer limit, String sort, String order) {
        List<YopsaasRideDriver> driverList = driverService.querySelective(ycDriverId, cellphone, start, end, page, limit,
                sort, order);
        return ResponseUtil.okList(driverList);
    }

    public Object detail(Integer ycDriverId) {
        YopsaasRideDriver driver = driverService.findByYcDriverId(ycDriverId);
        Map<String, Object> data = new HashMap<>();
        data.put("driver", driver);

        return ResponseUtil.ok(data);
    }

}
