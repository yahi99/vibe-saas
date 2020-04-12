package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasFootprintMapper;
import com.yongche.yopsaas.db.domain.YopsaasFootprint;
import com.yongche.yopsaas.db.domain.YopsaasFootprintExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YopsaasFootprintService {
    @Resource
    private YopsaasFootprintMapper footprintMapper;

    public List<YopsaasFootprint> queryByAddTime(Integer userId, Integer page, Integer size) {
        YopsaasFootprintExample example = new YopsaasFootprintExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        example.setOrderByClause(YopsaasFootprint.Column.addTime.desc());
        PageHelper.startPage(page, size);
        return footprintMapper.selectByExample(example);
    }

    public YopsaasFootprint findById(Integer id) {
        return footprintMapper.selectByPrimaryKey(id);
    }

    public YopsaasFootprint findById(Integer userId, Integer id) {
        YopsaasFootprintExample example = new YopsaasFootprintExample();
        example.or().andIdEqualTo(id).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return footprintMapper.selectOneByExample(example);
    }

    public void deleteById(Integer id) {
        footprintMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(YopsaasFootprint footprint) {
        footprint.setAddTime(LocalDateTime.now());
        footprint.setUpdateTime(LocalDateTime.now());
        footprintMapper.insertSelective(footprint);
    }

    public List<YopsaasFootprint> querySelective(String userId, String goodsId, Integer page, Integer size, String sort, String order) {
        YopsaasFootprintExample example = new YopsaasFootprintExample();
        YopsaasFootprintExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(userId)) {
            criteria.andUserIdEqualTo(Integer.valueOf(userId));
        }
        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(Integer.valueOf(goodsId));
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return footprintMapper.selectByExample(example);
    }
}
