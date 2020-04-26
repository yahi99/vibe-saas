package com.yongche.yopsaas.admin.web;

import com.yongche.yopsaas.admin.annotation.RequiresPermissionsDesc;
import com.yongche.yopsaas.admin.service.AdminRideOrderDispatchService;
import com.yongche.yopsaas.admin.service.AdminRideOrderService;
import com.yongche.yopsaas.core.express.ExpressService;
import com.yongche.yopsaas.core.validator.Order;
import com.yongche.yopsaas.core.validator.RideDispatchSort;
import com.yongche.yopsaas.core.validator.RideSort;
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
@RequestMapping("/admin/rideorderdispatch")
@Validated
public class AdminRideOrderDispatchController {
    private final Log logger = LogFactory.getLog(AdminRideOrderDispatchController.class);

    @Autowired
    private AdminRideOrderDispatchService adminRideOrderDispatchService;

    /**
     * 查询网约车派单
     *
     * @param rideOrderId
     * @param rideOrderDispatchId
     * @param rideOrderStatusArray
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @RequiresPermissions("admin:rideorderdispatch:list")
    @RequiresPermissionsDesc(menu = {"网约车管理", "派单管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(Long rideOrderId, Long rideOrderDispatchId,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                       @RequestParam(required = false) List<Byte> rideOrderStatusArray,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @RideDispatchSort @RequestParam(defaultValue = "create_time") String sort,
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

        return adminRideOrderDispatchService.list(rideOrderId, rideOrderDispatchId, startT, endT, rideOrderStatusArray, page, limit, sort, order);
    }

    /**
     * 网约车派单详情
     *
     * @param rideOrderDispatchId
     * @return
     */
    @RequiresPermissions("admin:rideorderdispatch:read")
    @RequiresPermissionsDesc(menu = {"网约车管理", "派单管理"}, button = "详情")
    @GetMapping("/detail")
    public Object detail(@NotNull Long rideOrderDispatchId) {
        return adminRideOrderDispatchService.detail(rideOrderDispatchId);
    }

    /**
     * 删除派单
     *
     * @param body 派单信息，{ orderDispatchId：xxx }
     * @return 派单操作结果
     */
    @RequiresPermissions("admin:rideorderdispatch:delete")
    @RequiresPermissionsDesc(menu = {"网约车管理", "派单管理"}, button = "订单删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody String body) {
        return adminRideOrderDispatchService.delete(body);
    }
}
