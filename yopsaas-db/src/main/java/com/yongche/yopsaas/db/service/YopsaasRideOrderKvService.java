package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasRideOrderKvMapper;
import com.yongche.yopsaas.db.domain.YopsaasRideDriver;
import com.yongche.yopsaas.db.domain.YopsaasRideDriverExample;
import com.yongche.yopsaas.db.domain.YopsaasRideOrderKv;
import com.yongche.yopsaas.db.domain.YopsaasRideOrderKvExample;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class YopsaasRideOrderKvService {
    private final Log logger = LogFactory.getLog(YopsaasRideOrderKvService.class);

    public final static String KEY_ORDER_SNAP = "snap";

    public final static Byte TYPE_NORMAL = 0;
    public final static Byte TYPE_JSON = 1;

    @Resource
    private YopsaasRideOrderKvMapper yopsaasRideOrderKvMapper;

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

    public int add(YopsaasRideOrderKv rideOrderKv) {
        int time = YopsaasRideOrderKvService.getSecondTimestamp(new Date());
        rideOrderKv.setCreateTime(time);
        return yopsaasRideOrderKvMapper.insertSelective(rideOrderKv);
    }

    public int update(YopsaasRideOrderKv rideOrderKv, Long rideOrderId, String key) {
        YopsaasRideOrderKvExample example = new YopsaasRideOrderKvExample();
        example.or().andRideOrderIdEqualTo(rideOrderId)
                .andKEqualTo(key);
        int time = YopsaasRideOrderKvService.getSecondTimestamp(new Date());
        rideOrderKv.setUpdateTime(time);
        return yopsaasRideOrderKvMapper.updateByExampleSelective(rideOrderKv, example);
    }

    public List<YopsaasRideOrderKv> queryById(Long rideOrderId) {
        YopsaasRideOrderKvExample example = new YopsaasRideOrderKvExample();
        example.or().andRideOrderIdEqualTo(rideOrderId);
        return yopsaasRideOrderKvMapper.selectByExample(example);
    }

    public YopsaasRideOrderKv find(Long rideOrderId, String key) {
        YopsaasRideOrderKvExample example = new YopsaasRideOrderKvExample();
        example.or().andRideOrderIdEqualTo(rideOrderId)
                .andKEqualTo(key);
        return yopsaasRideOrderKvMapper.selectOneByExample(example);
    }
}
