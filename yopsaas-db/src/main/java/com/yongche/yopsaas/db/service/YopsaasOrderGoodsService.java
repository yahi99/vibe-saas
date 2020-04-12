package com.yongche.yopsaas.db.service;

import com.yongche.yopsaas.db.dao.YopsaasOrderGoodsMapper;
import com.yongche.yopsaas.db.domain.YopsaasOrderGoods;
import com.yongche.yopsaas.db.domain.YopsaasOrderGoodsExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YopsaasOrderGoodsService {
    @Resource
    private YopsaasOrderGoodsMapper orderGoodsMapper;

    public int add(YopsaasOrderGoods orderGoods) {
        orderGoods.setAddTime(LocalDateTime.now());
        orderGoods.setUpdateTime(LocalDateTime.now());
        return orderGoodsMapper.insertSelective(orderGoods);
    }

    public List<YopsaasOrderGoods> queryByOid(Integer orderId) {
        YopsaasOrderGoodsExample example = new YopsaasOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        return orderGoodsMapper.selectByExample(example);
    }

    public List<YopsaasOrderGoods> findByOidAndGid(Integer orderId, Integer goodsId) {
        YopsaasOrderGoodsExample example = new YopsaasOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return orderGoodsMapper.selectByExample(example);
    }

    public YopsaasOrderGoods findById(Integer id) {
        return orderGoodsMapper.selectByPrimaryKey(id);
    }

    public void updateById(YopsaasOrderGoods orderGoods) {
        orderGoods.setUpdateTime(LocalDateTime.now());
        orderGoodsMapper.updateByPrimaryKeySelective(orderGoods);
    }

    public Short getComments(Integer orderId) {
        YopsaasOrderGoodsExample example = new YopsaasOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        long count = orderGoodsMapper.countByExample(example);
        return (short) count;
    }

    public boolean checkExist(Integer goodsId) {
        YopsaasOrderGoodsExample example = new YopsaasOrderGoodsExample();
        example.or().andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return orderGoodsMapper.countByExample(example) != 0;
    }

    public void deleteByOrderId(Integer orderId) {
        YopsaasOrderGoodsExample example = new YopsaasOrderGoodsExample();
        example.or().andOrderIdEqualTo(orderId).andDeletedEqualTo(false);
        orderGoodsMapper.logicalDeleteByExample(example);
    }
}
