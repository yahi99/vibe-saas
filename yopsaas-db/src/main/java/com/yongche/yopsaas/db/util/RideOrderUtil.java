package com.yongche.yopsaas.db.util;

import com.yongche.yopsaas.db.domain.YopsaasRideOrder;

import java.util.ArrayList;
import java.util.List;

/*
 * 订单流程：下单成功－》司机到达-》支付订单
 * 订单状态：
 * -1	无效	-	-	-
 * 0	未初始化	-	-	未初始化
 * 1	等待用户付款	等待付款	-	等待用户付款
 * 2	等待分配车辆	预订成功	-	等待分配车辆
 * 3-1	等待司机确认(driver_id为空)	预订成功	等待司机确认	等待司机确认
 * 3-2	等待用户确认(driver_id不为空)	预订成功	等待决策	等待客户确认
 * 4	等待司机就位	预订成功	等待就位	等待司机就位
 * flag 0x4000000	等待司机出发	预定成功	等待司机出发	等待司机出发
 * 5	等待服务开始	服务中	等待开始服务	等待服务开始
 * 6	等待服务结束	服务中	服务中	服务中
 * 7	服务结束	服务完成	服务已完成	服务结束
 * 8	订单取消	取消	订单已取消	订单已取消
 */
public class RideOrderUtil {

    public static final Byte STATUS_CREATE = 0;
    public static final Byte STATUS_NEED_PREPAY = 1;
    public static final Byte STATUS_DRIVER_CONFIRM = 2;
    public static final Byte STATUS_DRIVER_ACCEPT = 3;
    public static final Byte STATUS_DRIVER_ARRIVE = 4;
    public static final Byte STATUS_SERVICE_START = 5;
    public static final Byte STATUS_SERVICE_END = 6;
    public static final Byte STATUS_SERVICE_DONE = 7;
    public static final Byte STATUS_SERVICE_CANCEL = 8;

    public static String orderStatusText(YopsaasRideOrder order) {
        int status = order.getStatus().intValue();

        if (status == 0) {
            return "未初始化";
        }

        if (status == 1) {
            return "等待用户付款";
        }

        if (status == 2) {
            return "等待分配车辆";
        }

        if (status == 3) {
            return "等待司机确认";
        }

        if (status == 4) {
            return "等待司机就位";
        }

        if (status == 5) {
            return "等待服务开始";
        }

        if (status == 6) {
            return "等待服务结束";
        }

        if (status == 7) {
            return "服务结束";
        }

        if (status == 8) {
            return "订单取消";
        }
        throw new IllegalStateException("orderStatus不支持");
    }


    public static RideOrderHandleOption build(YopsaasRideOrder order) {
        int status = order.getStatus().intValue();
        RideOrderHandleOption handleOption = new RideOrderHandleOption();

        if (status == 7) {
            // 如果服务完成，且没有支付，则可支付，可取消
            handleOption.setPay(true);
        } else if (status == 8) {
            // 如果订单已经取消，则可删除
            handleOption.setDelete(true);
        } else {
            throw new IllegalStateException("status不支持");
        }

        return handleOption;
    }

    public static List<Short> orderStatus(Integer showType) {
        // 全部订单
        if (showType == 0) {
            return null;
        }

        List<Short> status = new ArrayList<Short>(2);

        if (showType.equals(1)) {
            // 待付款订单
            status.add((short) 7);
        } else {
            return null;
        }

        return status;
    }

    public static boolean isPayed(YopsaasRideOrder order) {
        return false;
    }
}
