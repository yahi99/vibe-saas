package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.RideOrderMapper;
import com.yongche.yopsaas.db.dao.YopsaasRideOrderMapper;
import com.yongche.yopsaas.db.domain.YopsaasRideOrder;
import com.yongche.yopsaas.db.domain.YopsaasRideOrderExample;
import com.yongche.yopsaas.db.util.RideOrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class YopsaasRideOrderService {
    public static final Long FLAG_COMMENTED = 0x01L; // 已评价
    public static final Long FLAG_NOT_SUPPORT_SYSTEM_DECISION = 0x02L; // 不支持系统决策
    public static final Long FLAG_SET_ACCOUNT = 0x04L; // 是否已设置结算帐号
    public static final Long FLAG_RECEIPT = 0x08L; // 已经开具发票

    public static final Long FLAG_WAIT_RECEIPT = 0x10L; // 待开具发票    针对企业客户
    public static final Long FLAG_CHANGE_DRIVER = 0x20L; // 订单改派
    public static final Long FLAG_CLEARING_FORM = 0x40L; // 标志企业用户的结算方式 1: 为月结
    public static final Long FLAG_PASSENGER = 0x80L; // 订车人和乘车人不是同一个人

    public static final Long FLAG_PASSENGER_SMS = 0x100L; // 是否给乘车人发送短信
    public static final Long FLAG_OFFLINE = 0x200L; // 司机端离线标记
    public static final Long FLAG_YIKOUJIA = 0x400L; // 是否一口价订单
    public static final Long FLAG_PAYSENDCOUPON = 0x800L; // 绑卡用户支付送券

    public static final Long FLAG_CHANGBAOCHE = 0x1000L; // 是否长包车订单
    public static final Long FLAG_UPDATE_CAR_TYPE = 0x2000L; // 是否升舱
    public static final Long FLAG_TICKET = 0x4000L; // 是否车票订单
    public static final Long FLAG_YOP_LOCALFIXED = 0x8000L; // 是否为YOP本地一口价订

    public static final Long FLAG_ORDER_UNFINISH = 0x10000L; // 订单是否未完成(司机端同步订单专用)
    public static final Long FLAG_FACE_PAY = 0x20000L; // 是否面付
    public static final Long FLAG_FEE_COMPUTED = 0x40000L; // 是否算费
    public static final Long FLAG_DELETED = 0x80000L; // 删除标识

    public static final Long FLAG_DISCOUNT_LINE = 0x100000L; // 是否优惠线路
    public static final Long FLAG_JIAJIA = 0x200000L; // 加价标识
    public static final Long FLAG_DRIVER_STARTOFF = 0x400000L; // 司机已出发
    public static final Long FLAG_COMMENT_CLOSED = 0x800000L; // 评价入口已关闭

    public static final Long FLAG_CORPORATE_PAY_AFTER = 0x1000000L; // 企业账号支付后置
    public static final Long FLAG_YOP_OVERSEA_ORDER = 0x2000000L; // YOP海外新一口价订单标志
    public static final Long FLAG_CHANGED_COUPON = 0x4000000L; // 用户已经确认修改优惠券
    public static final Long FLAG_ONE_TO_ONE = 0x8000000L; // 收藏司机一对一订单

    public static final Long FLAG_PAY_NOTICE_USER = 0x10000000L; // 是否不给用户发送短信
    public static final Long FLAG_CHARGE_MIN = 0x20000000L; // 在取消时是否收取最小费用 0:否 1:是
    public static final Long FLAG_RETURN_COUPON = 0x40000000L; // 是否返还代金券
    public static final Long FLAG_AUTO_DISPATCH = 0x80000000L; // 该订单是否需要自动派单:1,是;其他,否

    public static final Long FLAG_SUPPORT_MANUALLY_DECISION = 0x100000000L; // 是否支持用户手工决策：1支持，0不支持 has_custom_decision
    public static final Long FLAG_IS_CUSTOM_DECISION = 0x200000000L; // 是否是用户手工决策的订单
    public static final Long FLAG_SUPPORT_MANUAL_DISPATCH = 0x400000000L; // 是否需要手动派单。1:需要 0:不需要 is_need_manual_dispatch
    public static final Long FLAG_ASSIGNED = 0x800000000L; // 是否系统指派(指派成功后标记)

    public static final Long FLAG_DRIVER_FIXED = 0x1000000000L;// 是否为司机一口价
    public static final Long FLAG_DISTRIBUTE_ING = 0x2000000000L;// 是否正在分佣 1:是，不能继续调整金额
    public static final Long FLAG_DRIVER_REDISPATCH = 0x4000000000L; // 是否是司机改派订单, FLAG_CHANGE_DRIVER是老的司机改派逻辑
    public static final Long FLAG_ABNORMAL_RISK = 0x8000000000L; // 是否来自风控的异议工单，如设置本标记表示工单来自风控

    public static final Long FLAG_ORDER_FEE_ZERO = 0x10000000000L; // 已调整订单无费
    public static final Long FLAG_ORDER_BY_THE_WAY = 0x20000000000L; // 顺路订单
    public static final Long FLAG_TIME_SHARING = 0x40000000000L; // 是否为分时计价订单
    public static final Long FLAG_TAXIMETER = 0x80000000000L;   // 是否打表来接

    public static final Long FLAG_BARGAIN = 0x100000000000L;    // 是否议价订单
    public static final Long FLAG_LIVE = 0x200000000000L;    // 是否直播车订单
    public static final Long FLAG_RANDOM_DEDUCT = 0x400000000000L;    // 是否随机立减订单
    public static final Long FLAG_VIP_MEMBER_DISCOUNT = 0x800000000000L;    // 是否vip折扣订单

    public static final Long FLAG_CANCEL_HAVE_DUTY = 0x1000000000000L;   // 取消订单是否有责
    public static final Long FLAG_IS_RECEIPT = 0x2000000000000L;   // 是否可开发票
    public static final Long FLAG_LIMIT_RECEIPT_CITY = 0x4000000000000L;   // 限制开票城市
    public static final Long FLAG_IS_OFFLINE = 0x8000000000000L;   // 是否线下订单

    public static final Long FLAG_USE_COUPON = 0x10000000000000L;   // 是否使用优惠券
    public static final Long FLAG_IS_QUICK_ACCOUNT = 0x20000000000000L;   // 是否快速到账
    public static final Long FLAG_IS_QUICK_WITHDRAW = 0x40000000000000L;   // 是否快速提现
    public static final Long FLAG_IS_MIX_PAYMENT = 0x80000000000000L;   // 是否混合支付

    public static final Long FLAG_IS_COMPENSATION_WITHDRAW = 0x100000000000000L;   // 是否补偿提现订单
    public static final Long FLAG_IS_PREPAYMENT = 0x200000000000000L;   // 是否预付订单

    @Resource
    private YopsaasRideOrderMapper yopsaasRideOrderMapper;
    @Resource
    private RideOrderMapper rideOrderMapper;

    /**
     * 获取精确到秒的时间戳
     *
     * @return
     */
    public static int getSecondTimestamp(Date date) {
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0, length - 3));
        } else {
            return 0;
        }
    }

    public int add(YopsaasRideOrder order) {
        int time = YopsaasRideOrderService.getSecondTimestamp(new Date());
        order.setCreateTime(time);
        order.setUpdateTime(time);
        return yopsaasRideOrderMapper.insertSelective(order);
    }

    public int update(YopsaasRideOrder order) {
        int time = YopsaasRideOrderService.getSecondTimestamp(new Date());
        order.setUpdateTime(time);
        return yopsaasRideOrderMapper.updateByPrimaryKey(order);
    }

    public int count(Long userId) {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        example.or().andUserIdEqualTo(userId);
        return (int) yopsaasRideOrderMapper.countByExample(example);
    }

    public YopsaasRideOrder findById(Long orderId) {
        return yopsaasRideOrderMapper.selectByPrimaryKey(orderId);
    }

    public YopsaasRideOrder findById(Long userId, Long orderId) {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        example.or().andRideOrderIdEqualTo(orderId).andUserIdEqualTo(userId);
        return yopsaasRideOrderMapper.selectOneByExample(example);
    }

    private String getRandomNum(Integer num) {
        String base = "0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < num; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    public List<YopsaasRideOrder> queryByOrderStatus(Long userId, List<Byte> orderStatus, Integer page, Integer limit, String sort, String order) {
        int time = YopsaasRideOrderService.getSecondTimestamp(new Date());

        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        example.setOrderByClause(YopsaasRideOrder.Column.createTime.desc());
        YopsaasRideOrderExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        if (orderStatus != null) {
            criteria.andStatusIn(orderStatus);
        }
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return yopsaasRideOrderMapper.selectByExample(example);
    }

    public List<YopsaasRideOrder> querySelective(Long userId, int start, int end, List<Byte> orderStatusArray, Integer page, Integer limit, String sort, String order) {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        YopsaasRideOrderExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (start != 0) {
            criteria.andCreateTimeLessThanOrEqualTo(start);
        }
        if (end != 0) {
            criteria.andEndTimeLessThanOrEqualTo(end);
        }
        if (orderStatusArray != null && orderStatusArray.size() != 0) {
            criteria.andStatusIn(orderStatusArray);
        }

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return yopsaasRideOrderMapper.selectByExample(example);
    }

    public int updateWithOptimisticLocker(YopsaasRideOrder order) {
        int preUpdateTime = order.getUpdateTime();
        int time = YopsaasRideOrderService.getSecondTimestamp(new Date());
        order.setUpdateTime(time);
        return rideOrderMapper.updateWithOptimisticLocker(preUpdateTime, order);
    }

    public void deleteById(Long rideOrderId) {
        //not support
    }

    public int count() {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        example.or();
        return (int) yopsaasRideOrderMapper.countByExample(example);
    }

    public List<YopsaasRideOrder> queryUnpaid(int minutes) {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        example.or().andStatusEqualTo(RideOrderUtil.STATUS_SERVICE_DONE);
        return yopsaasRideOrderMapper.selectByExample(example);
    }


    public Map<Object, Object> orderInfo(Long userId) {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        example.or().andUserIdEqualTo(userId);
        List<YopsaasRideOrder> orders = yopsaasRideOrderMapper.selectByExampleSelective(example, YopsaasRideOrder.Column.status, YopsaasRideOrder.Column.payStatus);

        int unpaid = 0;
        for (YopsaasRideOrder order : orders) {
            if (!RideOrderUtil.isPayed(order)) {
                unpaid++;
            } else {
                // do nothing
            }
        }

        Map<Object, Object> orderInfo = new HashMap<Object, Object>();
        orderInfo.put("unpaid", unpaid);
        return orderInfo;

    }
}
