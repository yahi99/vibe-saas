package com.yongche.yopsaas.wx.task;

import com.yongche.yopsaas.core.system.SystemConfig;
import com.yongche.yopsaas.core.task.Task;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RideOrderUnchooseCarTask  extends Task {

    private final Log logger = LogFactory.getLog(OrderUnpaidTask.class);
    private long orderId = -1;

    public RideOrderUnchooseCarTask(long orderId, long delayInMilliseconds){
        super("RideOrderUnchooseCarTask-" + orderId, delayInMilliseconds);
        this.orderId = orderId;
    }

    public RideOrderUnchooseCarTask(long orderId){
        super("RideOrderUnchooseCarTask-" + orderId, SystemConfig.getRideOrderUnchooseCar() * 60 * 1000);
        this.orderId = orderId;
    }

    @Override
    public void run() {
        logger.info("系统开始处理延时任务---网约车订单超时未选车---" + this.orderId);
        logger.info("系统结束处理延时任务---网约车订单超时未选车---" + this.orderId);
    }
}
