package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasRideDriverMapper;
import com.yongche.yopsaas.db.domain.YopsaasRideDriver;
import com.yongche.yopsaas.db.domain.YopsaasRideDriverExample;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Service
public class YopsaasRideDriverService {
    private final Log logger = LogFactory.getLog(YopsaasRideDriverService.class);

    @Resource
    private YopsaasRideDriverMapper yopsaasRideDriverMapper;

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

    public int add(YopsaasRideDriver driver) {
        int time = YopsaasRideDriverService.getSecondTimestamp(new Date());
        driver.setCreateTime(time);
        return yopsaasRideDriverMapper.insertSelective(driver);
    }

    public YopsaasRideDriver findById(Long driverId) {
        return yopsaasRideDriverMapper.selectByPrimaryKey(driverId);
    }

    public YopsaasRideDriver findByYcDriverId(Integer ycDriverId) {
        YopsaasRideDriverExample example = new YopsaasRideDriverExample();
        example.or().andYcDriverIdEqualTo(ycDriverId);
        return yopsaasRideDriverMapper.selectOneByExample(example);
    }

    public List<YopsaasRideDriver> querySelective(Integer ycDriverId, String cellphone, int start, int end, Integer page, Integer limit, String sort, String order) {
        YopsaasRideDriverExample example = new YopsaasRideDriverExample();
        YopsaasRideDriverExample.Criteria criteria = example.createCriteria();

        if (ycDriverId != null) {
            criteria.andYcDriverIdEqualTo(ycDriverId);
        }
        if (cellphone != null) {
            criteria.andCellphoneEqualTo(cellphone);
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
        return yopsaasRideDriverMapper.selectByExample(example);
    }

    public void deleteById(Long rideOrderDispatchId) {
        //not support
    }
}
