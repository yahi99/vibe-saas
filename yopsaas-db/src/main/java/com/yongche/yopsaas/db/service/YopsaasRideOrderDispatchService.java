package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasRideOrderDispatchMapper;
import com.yongche.yopsaas.db.domain.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class YopsaasRideOrderDispatchService {
    private final Log logger = LogFactory.getLog(YopsaasRideOrderDispatchService.class);

    @Resource
    private YopsaasRideOrderDispatchMapper yopsaasRideOrderDispatchMapper;

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

    public int add(YopsaasRideOrderDispatch orderDispatch) {
        int time = YopsaasRideOrderDispatchService.getSecondTimestamp(new Date());
        orderDispatch.setCreateTime(time);
        return yopsaasRideOrderDispatchMapper.insertSelective(orderDispatch);
    }

    public YopsaasRideOrderDispatch findById(Long orderDispatchId) {
        return yopsaasRideOrderDispatchMapper.selectByPrimaryKey(orderDispatchId);
    }

    public List<YopsaasRideOrderDispatch> querySelective(Long rideOrderId, Long rideOrderDispatchId, int start, int end, List<Byte> orderStatusArray, Integer page, Integer limit, String sort, String order) {
        YopsaasRideOrderDispatchExample example = new YopsaasRideOrderDispatchExample();
        YopsaasRideOrderDispatchExample.Criteria criteria = example.createCriteria();

        if (rideOrderId != null) {
            criteria.andRideOrderIdEqualTo(rideOrderId);
        }
        if (rideOrderDispatchId != null) {
            criteria.andRideOrderDispatchIdEqualTo(rideOrderDispatchId);
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
        return yopsaasRideOrderDispatchMapper.selectByExample(example);
    }

    public void deleteById(Long rideOrderDispatchId) {
        //not support
    }
}
