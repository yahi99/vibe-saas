package com.yongche.yopsaas.admin.web;

import com.yongche.yopsaas.admin.annotation.RequiresPermissionsDesc;
import com.yongche.yopsaas.admin.service.AdminOrderService;
import com.yongche.yopsaas.admin.service.AdminRideOrderService;
import com.yongche.yopsaas.core.express.ExpressService;
import com.yongche.yopsaas.core.util.ResponseUtil;
import com.yongche.yopsaas.core.validator.Order;
import com.yongche.yopsaas.core.validator.RideSort;
import com.yongche.yopsaas.core.validator.Sort;
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
@RequestMapping("/admin/rideorder")
@Validated
public class AdminRideOrderController {
    private final Log logger = LogFactory.getLog(AdminRideOrderController.class);

    @Autowired
    private AdminRideOrderService adminRideOrderService;
    @Autowired
    private ExpressService expressService;

    /**
     * 查询网约车订单
     *
     * @param userId
     * @param orderSn
     * @param orderStatusArray
     * @param page
     * @param limit
     * @param sort
     * @param order
     * @return
     */
    @RequiresPermissions("admin:rideorder:list")
    @RequiresPermissionsDesc(menu = {"网约车管理", "订单管理"}, button = "查询")
    @GetMapping("/list")
    public Object list(Integer userId, String orderSn,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
                       @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
                       @RequestParam(required = false) List<Byte> orderStatusArray,
                       @RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer limit,
                       @RideSort @RequestParam(defaultValue = "create_time") String sort,
                       @Order @RequestParam(defaultValue = "desc") String order) {
        Timestamp startTime = Timestamp.valueOf(start);
        Timestamp endTime = Timestamp.valueOf(end);
        int startT = Math.round(startTime.getTime() / 1000);
        int endT = Math.round(endTime.getTime() / 1000);
        return adminRideOrderService.list(Long.valueOf(userId), startT, endT, orderStatusArray, page, limit, sort, order);
    }

    /**
     * 网约车订单详情
     *
     * @param id
     * @return
     */
    @RequiresPermissions("admin:rideorder:read")
    @RequiresPermissionsDesc(menu = {"网约车管理", "订单管理"}, button = "详情")
    @GetMapping("/detail")
    public Object detail(@NotNull Long id) {
        return adminRideOrderService.detail(id);
    }

    /**
     * 订单退款
     *
     * @param body 订单信息，{ orderId：xxx }
     * @return 订单退款操作结果
     */
    @RequiresPermissions("admin:rideorder:refund")
    @RequiresPermissionsDesc(menu = {"网约车管理", "订单管理"}, button = "订单退款")
    @PostMapping("/refund")
    public Object refund(@RequestBody String body) {
        return adminRideOrderService.refund(body);
    }


    /**
     * 删除订单
     *
     * @param body 订单信息，{ orderId：xxx }
     * @return 订单操作结果
     */
    @RequiresPermissions("admin:rideorder:delete")
    @RequiresPermissionsDesc(menu = {"网约车管理", "订单管理"}, button = "订单删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody String body) {
        return adminRideOrderService.delete(body);
    }
}
