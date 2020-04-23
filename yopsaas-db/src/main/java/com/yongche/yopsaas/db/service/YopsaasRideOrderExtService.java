package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.RideOrderMapper;
import com.yongche.yopsaas.db.dao.YopsaasRideOrderExtMapper;
import com.yongche.yopsaas.db.dao.YopsaasRideOrderMapper;
import com.yongche.yopsaas.db.domain.*;
import com.yongche.yopsaas.db.util.RideOrderUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Service
public class YopsaasRideOrderExtService {
    @Resource
    private YopsaasRideOrderExtMapper yopsaasRideOrderExtMapper;

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

    public int add(YopsaasRideOrderExtWithBLOBs orderExt) {
        int time = YopsaasRideOrderExtService.getSecondTimestamp(new Date());
        orderExt.setUpdateTime(time);
        return yopsaasRideOrderExtMapper.insertSelective(orderExt);
    }

    public YopsaasRideOrderExt findById(Long orderId) {
        YopsaasRideOrderExtExample example = new YopsaasRideOrderExtExample();
        example.or().andRideOrderIdEqualTo(orderId);
        return yopsaasRideOrderExtMapper.selectByExampleSelective(example).get(0);
    }

    public void deleteById(Long rideOrderId) {
        //not support
    }
}
