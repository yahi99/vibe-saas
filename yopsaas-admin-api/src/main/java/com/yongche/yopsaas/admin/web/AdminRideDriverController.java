package com.yongche.yopsaas.admin.web;

import com.yongche.yopsaas.admin.annotation.RequiresPermissionsDesc;
import com.yongche.yopsaas.admin.service.AdminRideDriverService;
import com.yongche.yopsaas.core.validator.Order;
import com.yongche.yopsaas.core.validator.RideDriverSort;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/admin/ridedriver")
@Validated
public class AdminRideDriverController {
    private final Log logger = LogFactory.getLog(AdminRideDriverController.class);

    @Autowired
    private AdminRideDriverService adminRideDriverService;

    /**
     * 查询司机
     *
     * @param ycDriverId
     * @param cellphone
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @RequiresPermissions("admin:ridedriver:list")
    @RequiresPermissionsDesc(menu = {"网约车管理", "司机管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(Integer ycDriverId, String cellphone,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @RideDriverSort @RequestParam(defaultValue = "create_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        int startT = 0;
        if(start != null) {
            Timestamp startTime = Timestamp.valueOf(start);
            startT = Math.round(startTime.getTime() / 1000);
        }
        int endT = 0;
        if(end != null) {
            Timestamp endTime = Timestamp.valueOf(end);
            endT = Math.round(endTime.getTime() / 1000);
        }

        return adminRideDriverService.list(ycDriverId, cellphone, startT, endT, page, limit, sort, order);
    }

    /**
     * 网约车司机详情
     *
     * @param ycDriverId
     * @return
     */
    @RequiresPermissions("admin:ridedriver:read")
    @RequiresPermissionsDesc(menu = {"网约车管理", "司机管理"}, button = "详情")
    @GetMapping("/detail")
    public Object detail(@NotNull Integer ycDriverId) {
        return adminRideDriverService.detail(ycDriverId);
    }
}
