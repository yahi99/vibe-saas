package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasRideDriverMapper;
import com.yongche.yopsaas.db.dao.YopsaasRideOrderTransactionHistoryMapper;
import com.yongche.yopsaas.db.domain.YopsaasRideDriver;
import com.yongche.yopsaas.db.domain.YopsaasRideDriverExample;
import com.yongche.yopsaas.db.domain.YopsaasRideOrderTransactionHistory;
import com.yongche.yopsaas.db.domain.YopsaasRideOrderTransactionHistoryExample;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class YopsaasRideOrderTransactionHistoryService extends YopsaasRideBaseService {
    //1:预收 2:预收退款 3:预授权 4:预授权完成 5:人工退款 6:未消费退款 7:取消订单退款 8:结账收款
    public final static Byte OPERATION_PREPAY = 1;
    public final static Byte OPERATION_PREPAY_REFUND = 2;
    public final static Byte OPERATION_PREAUTH = 3;
    public final static Byte OPERATION_PREAUTH_DONE = 4;
    public final static Byte OPERATION_OPERATOR_REFUND = 5;
    public final static Byte OPERATION_REFUND = 6;
    public final static Byte OPERATION_CANCEL_REFUND = 7;
    public final static Byte OPERATION_PAY = 8;

    public final static Byte PAY_TYPE_WECHAT = 1;
    public final static Short PAY_SOURCE_WECHAT = 1;
    public final static Byte TRANSACTION_TYPE_ORDER = 1;

    //消费状态 -10:失败 0:初始化 10:验证通过 20:部分付款 30:成功
    public final static Byte PAY_STATUS_FAIL = -10;
    public final static Byte PAY_STATUS_INIT = 0;
    public final static Byte PAY_STATUS_PASS = 10;
    public final static Byte PAY_STATUS_PART = 20;
    public final static Byte PAY_STATUS_SUCCESS = 30;

    private final Log logger = LogFactory.getLog(YopsaasRideOrderTransactionHistoryService.class);

    @Resource
    private YopsaasRideOrderTransactionHistoryMapper yopsaasRideOrderTransactionHistoryMapper;

    public int add(YopsaasRideOrderTransactionHistory orderTransactionHistory) {
        int time = YopsaasRideOrderTransactionHistoryService.getSecondTimestamp(new Date());
        orderTransactionHistory.setCreateTime(time);
        return yopsaasRideOrderTransactionHistoryMapper.insertSelective(orderTransactionHistory);
    }

    public int updateByTransactionId(YopsaasRideOrderTransactionHistory orderTransactionHistory, Long rechargeTransactionId) {
        YopsaasRideOrderTransactionHistoryExample example = new YopsaasRideOrderTransactionHistoryExample();
        example.or().andRechargeTransactionIdEqualTo(rechargeTransactionId);
        int time = YopsaasRideOrderTransactionHistoryService.getSecondTimestamp(new Date());
        orderTransactionHistory.setUpdateTime(time);
        return yopsaasRideOrderTransactionHistoryMapper.updateByExampleSelective(orderTransactionHistory, example);
    }

    public YopsaasRideOrderTransactionHistory findById(Long rideOrderTransactionHistoryId) {
        return yopsaasRideOrderTransactionHistoryMapper.selectByPrimaryKey(rideOrderTransactionHistoryId);
    }

    public YopsaasRideOrderTransactionHistory findByTransactionId(Long rechargeTransactionId) {
        YopsaasRideOrderTransactionHistoryExample example = new YopsaasRideOrderTransactionHistoryExample();
        example.or().andRechargeTransactionIdEqualTo(rechargeTransactionId);
        return yopsaasRideOrderTransactionHistoryMapper.selectOneByExample(example);
    }

    public List<YopsaasRideOrderTransactionHistory> querySelective(Long userId, Long rideOrderId, int start, int end, Integer page, Integer limit, String sort, String order) {
        YopsaasRideOrderTransactionHistoryExample example = new YopsaasRideOrderTransactionHistoryExample();
        YopsaasRideOrderTransactionHistoryExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andAccountIdEqualTo(userId);
        }
        if (rideOrderId != null) {
            criteria.andRideOrderIdEqualTo(rideOrderId);
        }
        if (start != 0) {
            criteria.andCreateTimeGreaterThanOrEqualTo(start);
        }
        if (end != 0) {
            criteria.andCreateTimeLessThanOrEqualTo(end);
        }

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        logger.debug(example.toString());
        return yopsaasRideOrderTransactionHistoryMapper.selectByExample(example);
    }

    public void deleteById(Long rechargeTransactionId) {
        //not support
    }
}
