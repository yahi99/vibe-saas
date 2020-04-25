package com.yongche.yopsaas.wx.task;

import com.ridegroup.yop.bean.BaseResult;
import com.ridegroup.yop.bean.BaseResultT;
import com.ridegroup.yop.bean.order.AcceptedDriver;
import com.yongche.yopsaas.core.task.Task;
import com.yongche.yopsaas.core.util.BeanUtil;
import com.yongche.yopsaas.core.util.JacksonUtil;
import com.yongche.yopsaas.db.domain.YopsaasRideOrder;
import com.yongche.yopsaas.db.domain.YopsaasRideOrderDispatch;
import com.yongche.yopsaas.db.service.YopsaasRideOrderDispatchService;
import com.yongche.yopsaas.db.service.YopsaasRideOrderService;
import com.yongche.yopsaas.db.util.RideOrderDispatchUtil;
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
            YopsaasRideOrderDispatchService rideOrderDispatchService = BeanUtil.getBean(YopsaasRideOrderDispatchService.class);
            YopsaasRideOrder rideOrder = rideOrderService.findById(rideOrderId);
            Long userId = rideOrder.getUserId();
            String driverIds = "";
            int maxNum = 120 / 2;
            int num = 1;
            boolean hasDriver = true;
            boolean hasDecision = false;
            String decisionDriverId = "";
            while(true) {
                logger.debug("loop for getSelectDriver num:" + num);
                if(num >= maxNum) {
                    hasDriver = false;
                    break;
                }
                selectDriver = yopOrderService.getSelectDriver(this.ycOrderId, driverIds);

                logger.debug(JacksonUtil.toJson(selectDriver));
                if(selectDriver.getWait_driver_time_length() <= 0
                        && selectDriver.getWait_user_time_length() <= 0) {
                    hasDriver = false;
                    break;
                }
                if(selectDriver.getCarlist().size() > 0) {
                    hasDriver = true;
                    for(int i = 0 ; i < selectDriver.getCarlist().size() ; i++) {
                        AcceptedDriver.Driver driver = selectDriver.getCarlist().get(i);
                        YopsaasRideOrderDispatch orderDispatch = new YopsaasRideOrderDispatch();
                        orderDispatch.setRideOrderId(rideOrderId);
                        orderDispatch.setDriverId(driver.getDriver_id());
                        orderDispatch.setName(driver.getName());
                        orderDispatch.setScore(driver.getScore());
                        orderDispatch.setPhoto(driver.getPhoto());
                        orderDispatch.setBrand(driver.getBrand());
                        orderDispatch.setCarType(driver.getCar_type());
                        orderDispatch.setCarTypeId(driver.getCar_type_id());
                        orderDispatch.setGoodCommentRate(driver.getGood_comment_rate());
                        orderDispatch.setIsDefault(Short.valueOf(String.valueOf(driver.getIs_default())));
                        orderDispatch.setIsServed(Short.valueOf(String.valueOf(driver.getIs_served())));
                        orderDispatch.setLatitude(driver.getLatitude());
                        orderDispatch.setLongitude(driver.getLongitude());
                        orderDispatch.setUnittimeCompleteCount(driver.getUnittime_complete_count());
                        orderDispatch.setCreateTime(YopOrderService.getTimestamp());
                        if(!hasDecision) {
                            decisionDriverId = "";
                            if(userId.intValue() == 1) {
                                if(driver.getDriver_id() == 52968) {
                                    decisionDriverId = String.valueOf(driver.getDriver_id());
                                }
                            } else {
                                decisionDriverId = String.valueOf(driver.getDriver_id());
                            }
                            if(!decisionDriverId.equals("")) {
                                BaseResult decisionResult = yopOrderService.decisionDriver(ycOrderId, decisionDriverId);
                                if(decisionResult.getCode().equals("200")) {
                                    hasDecision = true;
                                    orderDispatch.setStatus(RideOrderDispatchUtil.STATUS_SELECTED);
                                }
                            }
                        }
                        rideOrderDispatchService.add(orderDispatch);
                    }

                    break;
                }
                num += 2;
                Thread.sleep(2000);
            }
            if(!hasDriver) {
                //cancel
                YopsaasRideOrder updateOrder = new YopsaasRideOrder();
                updateOrder.setStatus(YopsaasRideOrderService.ORDER_STATUS_CANCELLED);
                updateOrder.setCancelTime(YopOrderService.getTimestamp());
                rideOrderService.update(updateOrder);
            }
        } catch (InterruptedException e) {
            logger.error(e);
        }
        logger.info("系统结束处理延时任务---网约车决策司机---" + this.rideOrderId + "---" + this.ycOrderId);
    }
}
