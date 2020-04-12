package com.yongche.yopsaas.db.service;

import com.yongche.yopsaas.db.dao.YopsaasGoodsAttributeMapper;
import com.yongche.yopsaas.db.domain.YopsaasGoodsAttribute;
import com.yongche.yopsaas.db.domain.YopsaasGoodsAttributeExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YopsaasGoodsAttributeService {
    @Resource
    private YopsaasGoodsAttributeMapper goodsAttributeMapper;

    public List<YopsaasGoodsAttribute> queryByGid(Integer goodsId) {
        YopsaasGoodsAttributeExample example = new YopsaasGoodsAttributeExample();
        example.or().andGoodsIdEqualTo(goodsId).andDeletedEqualTo(false);
        return goodsAttributeMapper.selectByExample(example);
    }

    public void add(YopsaasGoodsAttribute goodsAttribute) {
        goodsAttribute.setAddTime(LocalDateTime.now());
        goodsAttribute.setUpdateTime(LocalDateTime.now());
        goodsAttributeMapper.insertSelective(goodsAttribute);
    }

    public YopsaasGoodsAttribute findById(Integer id) {
        return goodsAttributeMapper.selectByPrimaryKey(id);
    }

    public void deleteByGid(Integer gid) {
        YopsaasGoodsAttributeExample example = new YopsaasGoodsAttributeExample();
        example.or().andGoodsIdEqualTo(gid);
        goodsAttributeMapper.logicalDeleteByExample(example);
    }

    public void deleteById(Integer id) {
        goodsAttributeMapper.logicalDeleteByPrimaryKey(id);
    }

    public void updateById(YopsaasGoodsAttribute attribute) {
        attribute.setUpdateTime(LocalDateTime.now());
        goodsAttributeMapper.updateByPrimaryKeySelective(attribute);
    }
}
