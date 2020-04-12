package com.yongche.yopsaas.db.service;

import org.apache.ibatis.annotations.Param;
import com.yongche.yopsaas.db.dao.GoodsProductMapper;
import com.yongche.yopsaas.db.dao.LitemallGoodsProductMapper;
import com.yongche.yopsaas.db.domain.LitemallGoodsProduct;
import com.yongche.yopsaas.db.domain.LitemallGoodsProductExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YopsaasGoodsProductService {
    @Resource
    private LitemallGoodsProductMapper yopsaasGoodsProductMapper;
    @Resource
    private GoodsProductMapper goodsProductMapper;

    public List<LitemallGoodsProduct> queryByGid(Integer gid) {
        LitemallGoodsProductExample example = new LitemallGoodsProductExample();
        example.or().andGoodsIdEqualTo(gid).andDeletedEqualTo(false);
        return yopsaasGoodsProductMapper.selectByExample(example);
    }

    public LitemallGoodsProduct findById(Integer id) {
        return yopsaasGoodsProductMapper.selectByPrimaryKey(id);
    }

    public void deleteById(Integer id) {
        yopsaasGoodsProductMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(LitemallGoodsProduct goodsProduct) {
        goodsProduct.setAddTime(LocalDateTime.now());
        goodsProduct.setUpdateTime(LocalDateTime.now());
        yopsaasGoodsProductMapper.insertSelective(goodsProduct);
    }

    public int count() {
        LitemallGoodsProductExample example = new LitemallGoodsProductExample();
        example.or().andDeletedEqualTo(false);
        return (int) yopsaasGoodsProductMapper.countByExample(example);
    }

    public void deleteByGid(Integer gid) {
        LitemallGoodsProductExample example = new LitemallGoodsProductExample();
        example.or().andGoodsIdEqualTo(gid);
        yopsaasGoodsProductMapper.logicalDeleteByExample(example);
    }

    public int addStock(Integer id, Short num){
        return goodsProductMapper.addStock(id, num);
    }

    public int reduceStock(Integer id, Short num){
        return goodsProductMapper.reduceStock(id, num);
    }

    public void updateById(LitemallGoodsProduct product) {
        product.setUpdateTime(LocalDateTime.now());
        yopsaasGoodsProductMapper.updateByPrimaryKeySelective(product);
    }
}