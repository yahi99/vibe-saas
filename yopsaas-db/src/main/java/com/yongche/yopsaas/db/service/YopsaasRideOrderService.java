package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.RideOrderMapper;
import com.yongche.yopsaas.db.dao.YopsaasRideOrderMapper;
import com.yongche.yopsaas.db.domain.YopsaasRideOrder;
import com.yongche.yopsaas.db.domain.YopsaasRideOrderExample;
import com.yongche.yopsaas.db.util.RideOrderUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class YopsaasRideOrderService {
    private final Log logger = LogFactory.getLog(YopsaasRideOrderService.class);

    // order status
    public final static Byte ORDER_STATUS_DISABLED = -1;            //无效
    public final static Byte ORDER_STATUS_PRECREATE = 0;            //未初始化
    public final static Byte ORDER_STATUS_INIT = 1;                 //等待用户付款
    public final static Byte ORDER_STATUS_WAITFORCAR = 2;           //等待选择车辆
    public final static Byte ORDER_STATUS_WAITDRIVERCONFIRM = 3;    //等待司机确认
    public final static Byte ORDER_STATUS_SERVICEREADY = 4;         //司机已确认，用户已确认
    public final static Byte ORDER_STATUS_ARRIVED = 5;              //司机已到达
    public final static Byte ORDER_STATUS_SERVICESTART = 6;         //服务开始
    public final static Byte ORDER_STATUS_SERVICEEND = 7;           //服务结束
    public final static Byte ORDER_STATUS_CANCELLED = 8;            //取消

    public final static Byte PAYABLE_ALLOW = 1;            //允许支付
    public final static Byte PAYABLE_NOT_ALLOW = 2;            //不允许支付

    // pay status
    public final static Byte PAY_STATUS_NO_NEED = 0;                  //0:无需付款
    public final static Byte PAY_STATUS_NONE = 1;                     //1:未付款
    public final static Byte PAY_STATUS_PORTION = 2;                  //2:部分付款
    public final static Byte PAY_STATUS_OFF = 3;                      //3:已付款

    //reason
    /*59	没有信用卡无法验证
        60	没有网银无法充值
        61	变更信息重新下单
        54	车辆未在预定时间到达
        58	其它

        取消原因状态码说明

        状态码	含义	说明
        63001	无车可派
        63002	有车无人接单(无有效司机接单)
        63003	有司机接单，用户未选车，系统自动取消
        63004	有司机接单，用户未选车，客户端自动取消
        100	合作方取消*/
    public final static int REASON_NO_CARD = 59;                      //没有信用卡无法验证
    public final static int REASON_NO_UNIONPAY = 60;                  //没有网银无法充值
    public final static int REASON_CHANGE_INFO_REORDER = 61;          //变更信息重新下单
    public final static int REASON_CAR_NOT_ARRIVE = 54;               //车辆未在预定时间到达
    public final static int REASON_OTHER = 58;                        //其它

    public final static int REASON_DISPATCH_NO_DRIVER             = 63001; //无车可派
    public final static int REASON_DISPATCH_NO_DRIVER_ACCEPT      = 63002; //有车无人接单(无有效司机接单)
    public final static int REASON_DISPATCH_SYSTEM_CANCEL         = 63003; //有司机接单，用户未选车，系统自动取消
    public final static int REASON_DISPATCH_APP_CANCEL            = 63004; //有司机接单，用户未选车，客户端自动取消
    public final static int REASON_DISPATCH_DRIVER_CANCEL         = 64001; //IVR联系乘客取消用车
    public final static int REASON_DISPATCH_UNCONTACT_CANCEL      = 64002; //IVR联系不上乘客
    public final static int REASON_DISPATCH_CANCEL_BY_DRIVER      = 65001; //司机自助取消订单
    public final static int REASON_DISPATCH_REDISPATCH_FAILED     = 66001; //改派失败（客服项目改派失败直接取消订单，不生成客服工单）

    public final static int REASON_SYSTEM     = 101; //saas系统取消

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
    public static final Long FLAG_IS_FAMILIAR = 0x400000000000000L;   // 是否熟司机

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
        return yopsaasRideOrderMapper.updateByPrimaryKeySelective(order);
    }

    public int updateByExample(YopsaasRideOrder order, YopsaasRideOrderExample example) {
        int time = YopsaasRideOrderService.getSecondTimestamp(new Date());
        order.setUpdateTime(time);
        return yopsaasRideOrderMapper.updateByExampleSelective(order, example);
    }

    public YopsaasRideOrder queryOneByExample(YopsaasRideOrderExample example) {
        return yopsaasRideOrderMapper.selectOneByExample(example);
    }

    public List<YopsaasRideOrder> queryByExample(YopsaasRideOrderExample example) {
        return yopsaasRideOrderMapper.selectByExample(example);
    }

    public int count(Long userId) {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        example.or().andUserIdEqualTo(userId);
        return (int) yopsaasRideOrderMapper.countByExample(example);
    }

    public YopsaasRideOrder findById(Long rideOrderId) {
        return yopsaasRideOrderMapper.selectByPrimaryKey(rideOrderId);
    }

    public YopsaasRideOrder findById(Long userId, Long rideOrderId) {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        example.or().andRideOrderIdEqualTo(rideOrderId).andUserIdEqualTo(userId);
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

    public List<YopsaasRideOrder> queryByPayStatus(Long userId, List<Byte> orderStatus, List<Byte> payStatus, Integer page, Integer limit, String sort, String order) {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        example.setOrderByClause(YopsaasRideOrder.Column.createTime.desc());
        YopsaasRideOrderExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        if (orderStatus != null) {
            criteria.andStatusIn(orderStatus);
        }
        if (payStatus != null) {
            criteria.andPayStatusIn(payStatus);
        }
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return yopsaasRideOrderMapper.selectByExample(example);
    }

    public static List<Byte> getCurrentTripStatus() {
        List<Byte> orderStatus = new ArrayList<Byte>();
        orderStatus.add(YopsaasRideOrderService.ORDER_STATUS_SERVICEREADY);
        orderStatus.add(YopsaasRideOrderService.ORDER_STATUS_ARRIVED);
        orderStatus.add(YopsaasRideOrderService.ORDER_STATUS_SERVICESTART);
        return orderStatus;
    }

    public List<YopsaasRideOrder> queryByOrderStatus(Long userId, List<Byte> orderStatus) {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        YopsaasRideOrderExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        if (orderStatus != null) {
            criteria.andStatusIn(orderStatus);
        }

        return yopsaasRideOrderMapper.selectByExample(example);
    }

    public List<YopsaasRideOrder> queryByPayStatus(Long userId, List<Byte> payStatus) {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        YopsaasRideOrderExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        List<Byte> status = new ArrayList<>();
        status.add(YopsaasRideOrderService.ORDER_STATUS_SERVICEEND);
        status.add(YopsaasRideOrderService.ORDER_STATUS_CANCELLED);
        criteria.andStatusIn(status);
        criteria.andPayStatusIn(payStatus);

        return yopsaasRideOrderMapper.selectByExample(example);
    }

    public List<YopsaasRideOrder> querySelective(Long userId, Long rideOrderId, int start, int end, List<Byte> orderStatusArray, Integer page, Integer limit, String sort, String order) {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        YopsaasRideOrderExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
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
        if (orderStatusArray != null && orderStatusArray.size() != 0) {
            criteria.andStatusIn(orderStatusArray);
        }

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        logger.debug(example.toString());
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

    public List<YopsaasRideOrder> getUnpayOrder(Long userId, YopsaasRideOrder.Column ... selective) {
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        YopsaasRideOrderExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        List<Byte> status = new ArrayList<>();
        status.add(YopsaasRideOrderService.ORDER_STATUS_SERVICEEND);
        status.add(YopsaasRideOrderService.ORDER_STATUS_CANCELLED);
        criteria.andStatusIn(status);
        List<Byte> payStatus = new ArrayList<>();
        payStatus.add(YopsaasRideOrderService.PAY_STATUS_NONE);
        payStatus.add(YopsaasRideOrderService.PAY_STATUS_PORTION);
        criteria.andPayStatusIn(payStatus);

        return yopsaasRideOrderMapper.selectByExampleSelective(example, selective);
    }

    public List<YopsaasRideOrder> getCurrentTrip(Long userId, YopsaasRideOrder.Column ... selective) {
        List<Byte> status = new ArrayList<>();
        YopsaasRideOrderExample example = new YopsaasRideOrderExample();
        YopsaasRideOrderExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        status.add(YopsaasRideOrderService.ORDER_STATUS_SERVICEREADY);
        status.add(YopsaasRideOrderService.ORDER_STATUS_ARRIVED);
        status.add(YopsaasRideOrderService.ORDER_STATUS_SERVICESTART);
        criteria.andStatusIn(status);
        return yopsaasRideOrderMapper.selectByExampleSelective(example, selective);
    }

    public Map<Object, Object> orderInfo(Long userId) {
        List<YopsaasRideOrder> unpayOrders = this.getUnpayOrder(userId, YopsaasRideOrder.Column.status, YopsaasRideOrder.Column.payStatus);
        List<YopsaasRideOrder> currentOrders = this.getCurrentTrip(userId, YopsaasRideOrder.Column.status, YopsaasRideOrder.Column.payStatus);

        int unpaid = 0;
        int currentTrip = 0;
        int historyTrip = 0;
        unpaid = unpayOrders.size();
        currentTrip = currentOrders.size();

        Map<Object, Object> orderInfo = new HashMap<Object, Object>();
        orderInfo.put("unpaid", unpaid);
        orderInfo.put("currentTrip", currentTrip);
        orderInfo.put("historyTrip", historyTrip);
        return orderInfo;

    }
}
