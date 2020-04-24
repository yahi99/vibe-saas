package com.yongche.yopsaas.wx.task;

import com.ridegroup.yop.bean.BaseResultT;
import com.ridegroup.yop.bean.order.AcceptedDriver;
import com.yongche.yopsaas.core.task.Task;
import com.yongche.yopsaas.core.util.BeanUtil;
import com.yongche.yopsaas.db.domain.YopsaasRideOrder;
import com.yongche.yopsaas.db.service.YopsaasRideOrderService;
import com.yongche.yopsaas.wx.service.YopOrderService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class RideOrderDecisionDriver extends Task {

    private final Log logger = LogFactory.getLog(OrderUnpaidTask.class);
    private long rideOrderId = -1;
    private String ycOrderId = null;

    public RideOrderDecisionDriver(long rideOrderId, long delayInMilliseconds) {
        super("RideOrderDecisionDriver-" + rideOrderId, delayInMilliseconds);
        this.rideOrderId = rideOrderId;
    }

    public RideOrderDecisionDriver(long rideOrderId) {
        super("RideOrderDecisionDriver-" + rideOrderId, 1000);
        this.rideOrderId = rideOrderId;
    }

    public RideOrderDecisionDriver(long rideOrderId, String ycOrderId) {
        super("RideOrderDecisionDriver-" + rideOrderId + "-" + ycOrderId, 1000);
        this.rideOrderId = rideOrderId;
        this.ycOrderId = ycOrderId;
    }

    @Override
    public void run() {
        logger.info("系统开始处理延时任务---网约车决策司机---" + this.rideOrderId + "---" + this.ycOrderId);
        try {
            AcceptedDriver selectDriver = null;

            YopOrderService yopOrderService = BeanUtil.getBean(YopOrderService.class);
            YopsaasRideOrderService rideOrderService = BeanUtil.getBean(YopsaasRideOrderService.class);
            YopsaasRideOrder rideOrder = rideOrderService.findById(rideOrderId);
            Long userId = rideOrder.getUserId();
            String driverIds = "";
            int maxNum = 120 / 2;
            int num = 1;
            boolean hasDriver = true;
            while(true) {
                if(num >= maxNum) {
                    hasDriver = false;
                    break;
                }
                selectDriver = yopOrderService.getSelectDriver(this.ycOrderId, driverIds);

                if(selectDriver.getWait_driver_time_length() <= 0
                        && selectDriver.getWait_user_time_length() <= 0) {
                    hasDriver = false;
                    break;
                }
                if(selectDriver.getCarlist().size() > 0) {
                    hasDriver = true;
                    for(int i = 0 ; i < selectDriver.getCarlist().size() ; i++) {
                        AcceptedDriver.Driver driver = selectDriver.getCarlist().get(i);

                    }
                    if(userId == 1) {

                    } else {

                    }
                    break;
                }
                num += 2;
                Thread.sleep(2000);
            }
            if(!hasDriver) {
                //cancel
            }
        } catch (InterruptedException e) {
            logger.error(e);
        }
        logger.info("系统结束处理延时任务---网约车决策司机---" + this.rideOrderId + "---" + this.ycOrderId);
    }
}
