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
    @Resource
    private YopsaasRideOrderMapper yopsaasRideOrderMapper;
    @Resource
    private RideOrderMapper rideOrderMapper;

    /**
     * 获取精确到秒的时间戳
     * @return
     */
    public static int getSecondTimestamp(Date date){
        if (null == date) {
            return 0;
        }
        String timestamp = String.valueOf(date.getTime());
        int length = timestamp.length();
        if (length > 3) {
            return Integer.valueOf(timestamp.substring(0,length-3));
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
        if(start != 0){
            criteria.andCreateTimeLessThanOrEqualTo(start);
        }
        if(end != 0){
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
