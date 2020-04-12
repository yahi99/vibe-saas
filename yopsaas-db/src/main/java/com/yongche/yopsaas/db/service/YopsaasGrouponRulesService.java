package com.yongche.yopsaas.db.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasGoodsMapper;
import com.yongche.yopsaas.db.dao.YopsaasGrouponRulesMapper;
import com.yongche.yopsaas.db.domain.YopsaasGoods;
import com.yongche.yopsaas.db.domain.YopsaasGrouponRules;
import com.yongche.yopsaas.db.domain.YopsaasGrouponRulesExample;
import com.yongche.yopsaas.db.util.GrouponConstant;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YopsaasGrouponRulesService {
    @Resource
    private YopsaasGrouponRulesMapper mapper;
    @Resource
    private YopsaasGoodsMapper goodsMapper;
    private YopsaasGoods.Column[] goodsColumns = new YopsaasGoods.Column[]{YopsaasGoods.Column.id, YopsaasGoods.Column.name, YopsaasGoods.Column.brief, YopsaasGoods.Column.picUrl, YopsaasGoods.Column.counterPrice, YopsaasGoods.Column.retailPrice};

    public int createRules(YopsaasGrouponRules rules) {
        rules.setAddTime(LocalDateTime.now());
        rules.setUpdateTime(LocalDateTime.now());
        return mapper.insertSelective(rules);
    }

    /**
     * 根据ID查找对应团购项
     *
     * @param id
     * @return
     */
    public YopsaasGrouponRules findById(Integer id) {
        return mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询某个商品关联的团购规则
     *
     * @param goodsId
     * @return
     */
    public List<YopsaasGrouponRules> queryByGoodsId(Integer goodsId) {
        YopsaasGrouponRulesExample example = new YopsaasGrouponRulesExample();
        example.or().andGoodsIdEqualTo(goodsId).andStatusEqualTo(GrouponConstant.RULE_STATUS_ON).andDeletedEqualTo(false);
        return mapper.selectByExample(example);
    }

    public int countByGoodsId(Integer goodsId) {
        YopsaasGrouponRulesExample example = new YopsaasGrouponRulesExample();
        example.or().andGoodsIdEqualTo(goodsId).andStatusEqualTo(GrouponConstant.RULE_STATUS_ON).andDeletedEqualTo(false);
        return (int)mapper.countByExample(example);
    }

    public List<YopsaasGrouponRules> queryByStatus(Short status) {
        YopsaasGrouponRulesExample example = new YopsaasGrouponRulesExample();
        example.or().andStatusEqualTo(status).andDeletedEqualTo(false);
        return mapper.selectByExample(example);
    }

    /**
     * 获取首页团购规则列表
     *
     * @param page
     * @param limit
     * @return
     */
    public List<YopsaasGrouponRules> queryList(Integer page, Integer limit) {
        return queryList(page, limit, "add_time", "desc");
    }

    public List<YopsaasGrouponRules> queryList(Integer page, Integer limit, String sort, String order) {
        YopsaasGrouponRulesExample example = new YopsaasGrouponRulesExample();
        example.or().andStatusEqualTo(GrouponConstant.RULE_STATUS_ON).andDeletedEqualTo(false);
        example.setOrderByClause(sort + " " + order);
        PageHelper.startPage(page, limit);
        return mapper.selectByExample(example);
    }

    /**
     * 判断某个团购规则是否已经过期
     *
     * @return
     */
    public boolean isExpired(YopsaasGrouponRules rules) {
        return (rules == null || rules.getExpireTime().isBefore(LocalDateTime.now()));
    }

    /**
     * 获取团购规则列表
     *
     * @param goodsId
     * @param page
     * @param size
     * @param sort
     * @param order
     * @return
     */
    public List<YopsaasGrouponRules> querySelective(String goodsId, Integer page, Integer size, String sort, String order) {
        YopsaasGrouponRulesExample example = new YopsaasGrouponRulesExample();
        example.setOrderByClause(sort + " " + order);

        YopsaasGrouponRulesExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(goodsId)) {
            criteria.andGoodsIdEqualTo(Integer.parseInt(goodsId));
        }
        criteria.andDeletedEqualTo(false);

        PageHelper.startPage(page, size);
        return mapper.selectByExample(example);
    }

    public void delete(Integer id) {
        mapper.logicalDeleteByPrimaryKey(id);
    }

    public int updateById(YopsaasGrouponRules grouponRules) {
        grouponRules.setUpdateTime(LocalDateTime.now());
        return mapper.updateByPrimaryKeySelective(grouponRules);
    }
}