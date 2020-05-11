package com.yongche.yopsaas.wx.task;

import com.yongche.yopsaas.core.system.SystemConfig;
import com.yongche.yopsaas.core.task.Task;
import com.yongche.yopsaas.core.util.BeanUtil;
import com.yongche.yopsaas.db.domain.YopsaasRideOrder;
import com.yongche.yopsaas.db.domain.YopsaasRideOrderExample;
import com.yongche.yopsaas.db.service.YopsaasRideOrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RideOrderUnchooseCarTask extends Task {

    private final Log logger = LogFactory.getLog(RideOrderUnchooseCarTask.class);
    private long rideOrderId = -1;

    public RideOrderUnchooseCarTask(long rideOrderId, long delayInMilliseconds) {
        super("RideOrderUnchooseCarTask-" + rideOrderId, delayInMilliseconds);
        this.rideOrderId = rideOrderId;
    }

    public RideOrderUnchooseCarTask(long rideOrderId) {
        super("RideOrderUnchooseCarTask-" + rideOrderId, SystemConfig.getRideOrderUnchooseCar() * 60 * 1000);
        this.rideOrderId = rideOrderId;
    }

    @Override
    public void run() {
        logger.info("系统开始处理延时任务---网约车订单超时未选车---" + this.rideOrderId);

        YopsaasRideOrderService rideOrderService = BeanUtil.getBean(YopsaasRideOrderService.class);
        YopsaasRideOrder rideOrder = rideOrderService.findById(rideOrderId);
        if(rideOrder.getStatus() <= YopsaasRideOrderService.ORDER_STATUS_WAITDRIVERCONFIRM) {
            // process
            YopsaasRideOrder updateRideOrder = new YopsaasRideOrder();
            updateRideOrder.setStatus(YopsaasRideOrderService.ORDER_STATUS_CANCELLED);
            updateRideOrder.setReasonId(YopsaasRideOrderService.REASON_SYSTEM);

            YopsaasRideOrderExample example = new YopsaasRideOrderExample();
            example.or().andRideOrderIdEqualTo(rideOrderId);
            rideOrderService.updateByExample(updateRideOrder, example);
        }

        logger.info("系统结束处理延时任务---网约车订单超时未选车---" + this.rideOrderId);
    }
}
