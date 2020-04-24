package com.yongche.yopsaas.db.util;

import com.yongche.yopsaas.db.domain.YopsaasRideOrder;
import com.yongche.yopsaas.db.domain.YopsaasRideOrderDispatch;

import java.util.ArrayList;
import java.util.List;

/*
 *
 * 0	未选
 * 1	已选
 */
public class RideOrderDispatchUtil {

    public static final Byte STATUS_NOT_SELECTED = 0;
    public static final Byte STATUS_SELECTED = 1;

    public static String orderStatusText(YopsaasRideOrderDispatch orderDispatch) {
        int status = orderDispatch.getStatus().intValue();

        if (status == 0) {
            return "未选";
        }

        if (status == 1) {
            return "已选";
        }
        throw new IllegalStateException("orderDispatchStatus不支持");
    }


    public static RideOrderDispatchHandleOption build(YopsaasRideOrderDispatch orderDispatch) {
        int status = orderDispatch.getStatus().intValue();
        RideOrderDispatchHandleOption handleOption = new RideOrderDispatchHandleOption();

        if (status == 0) {
            // 如果未选，则可删除
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
            // 已选
            status.add((short) 1);
        } else {
            return null;
        }

        return status;
    }
}
