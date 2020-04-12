package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasOrderMapper;
import com.yongche.yopsaas.db.dao.OrderMapper;
import com.yongche.yopsaas.db.domain.YopsaasOrder;
import com.yongche.yopsaas.db.domain.YopsaasOrderExample;
import com.yongche.yopsaas.db.util.OrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
public class YopsaasOrderService {
    @Resource
    private YopsaasOrderMapper yopsaasOrderMapper;
    @Resource
    private OrderMapper orderMapper;

    public int add(YopsaasOrder order) {
        order.setAddTime(LocalDateTime.now());
        order.setUpdateTime(LocalDateTime.now());
        return yopsaasOrderMapper.insertSelective(order);
    }

    public int count(Integer userId) {
        YopsaasOrderExample example = new YopsaasOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return (int) yopsaasOrderMapper.countByExample(example);
    }

    public YopsaasOrder findById(Integer orderId) {
        return yopsaasOrderMapper.selectByPrimaryKey(orderId);
    }

    public YopsaasOrder findById(Integer userId, Integer orderId) {
        YopsaasOrderExample example = new YopsaasOrderExample();
        example.or().andIdEqualTo(orderId).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return yopsaasOrderMapper.selectOneByExample(example);
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

    public int countByOrderSn(Integer userId, String orderSn) {
        YopsaasOrderExample example = new YopsaasOrderExample();
        example.or().andUserIdEqualTo(userId).andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return (int) yopsaasOrderMapper.countByExample(example);
    }

    // TODO 这里应该产生一个唯一的订单，但是实际上这里仍然存在两个订单相同的可能性
    public String generateOrderSn(Integer userId) {
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyyMMdd");
        String now = df.format(LocalDate.now());
        String orderSn = now + getRandomNum(6);
        while (countByOrderSn(userId, orderSn) != 0) {
            orderSn = now + getRandomNum(6);
        }
        return orderSn;
    }

    public List<YopsaasOrder> queryByOrderStatus(Integer userId, List<Short> orderStatus, Integer page, Integer limit, String sort, String order) {
        YopsaasOrderExample example = new YopsaasOrderExample();
        example.setOrderByClause(YopsaasOrder.Column.addTime.desc());
        YopsaasOrderExample.Criteria criteria = example.or();
        criteria.andUserIdEqualTo(userId);
        if (orderStatus != null) {
            criteria.andOrderStatusIn(orderStatus);
        }
        criteria.andDeletedEqualTo(false);
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return yopsaasOrderMapper.selectByExample(example);
    }

    public List<YopsaasOrder> querySelective(Integer userId, String orderSn, LocalDateTime start, LocalDateTime end, List<Short> orderStatusArray, Integer page, Integer limit, String sort, String order) {
        YopsaasOrderExample example = new YopsaasOrderExample();
        YopsaasOrderExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(orderSn)) {
            criteria.andOrderSnEqualTo(orderSn);
        }
        if(start != null){
            criteria.andAddTimeGreaterThanOrEqualTo(start);
        }
        if(end != null){
            criteria.andAddTimeLessThanOrEqualTo(end);
        }
        if (orderStatusArray != null && orderStatusArray.size() != 0) {
            criteria.andOrderStatusIn(orderStatusArray);
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return yopsaasOrderMapper.selectByExample(example);
    }

    public int updateWithOptimisticLocker(YopsaasOrder order) {
        LocalDateTime preUpdateTime = order.getUpdateTime();
        order.setUpdateTime(LocalDateTime.now());
        return orderMapper.updateWithOptimisticLocker(preUpdateTime, order);
    }

    public void deleteById(Integer id) {
        yopsaasOrderMapper.logicalDeleteByPrimaryKey(id);
    }

    public int count() {
        YopsaasOrderExample example = new YopsaasOrderExample();
        example.or().andDeletedEqualTo(false);
        return (int) yopsaasOrderMapper.countByExample(example);
    }

    public List<YopsaasOrder> queryUnpaid(int minutes) {
        YopsaasOrderExample example = new YopsaasOrderExample();
        example.or().andOrderStatusEqualTo(OrderUtil.STATUS_CREATE).andDeletedEqualTo(false);
        return yopsaasOrderMapper.selectByExample(example);
    }

    public List<YopsaasOrder> queryUnconfirm(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.minusDays(days);
        YopsaasOrderExample example = new YopsaasOrderExample();
        example.or().andOrderStatusEqualTo(OrderUtil.STATUS_SHIP).andShipTimeLessThan(expired).andDeletedEqualTo(false);
        return yopsaasOrderMapper.selectByExample(example);
    }

    public YopsaasOrder findBySn(String orderSn) {
        YopsaasOrderExample example = new YopsaasOrderExample();
        example.or().andOrderSnEqualTo(orderSn).andDeletedEqualTo(false);
        return yopsaasOrderMapper.selectOneByExample(example);
    }

    public Map<Object, Object> orderInfo(Integer userId) {
        YopsaasOrderExample example = new YopsaasOrderExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        List<YopsaasOrder> orders = yopsaasOrderMapper.selectByExampleSelective(example, YopsaasOrder.Column.orderStatus, YopsaasOrder.Column.comments);

        int unpaid = 0;
        int unship = 0;
        int unrecv = 0;
        int uncomment = 0;
        for (YopsaasOrder order : orders) {
            if (OrderUtil.isCreateStatus(order)) {
                unpaid++;
            } else if (OrderUtil.isPayStatus(order)) {
                unship++;
            } else if (OrderUtil.isShipStatus(order)) {
                unrecv++;
            } else if (OrderUtil.isConfirmStatus(order) || OrderUtil.isAutoConfirmStatus(order)) {
                uncomment += order.getComments();
            } else {
                // do nothing
            }
        }

        Map<Object, Object> orderInfo = new HashMap<Object, Object>();
        orderInfo.put("unpaid", unpaid);
        orderInfo.put("unship", unship);
        orderInfo.put("unrecv", unrecv);
        orderInfo.put("uncomment", uncomment);
        return orderInfo;

    }

    public List<YopsaasOrder> queryComment(int days) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.minusDays(days);
        YopsaasOrderExample example = new YopsaasOrderExample();
        example.or().andCommentsGreaterThan((short) 0).andConfirmTimeLessThan(expired).andDeletedEqualTo(false);
        return yopsaasOrderMapper.selectByExample(example);
    }

    public void updateAftersaleStatus(Integer orderId, Short statusReject) {
        YopsaasOrder order = new YopsaasOrder();
        order.setId(orderId);
        order.setAftersaleStatus(statusReject);
        order.setUpdateTime(LocalDateTime.now());
        yopsaasOrderMapper.updateByPrimaryKeySelective(order);
    }
}
