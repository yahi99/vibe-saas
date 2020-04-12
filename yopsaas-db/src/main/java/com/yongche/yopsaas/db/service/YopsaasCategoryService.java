package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasCategoryMapper;
import com.yongche.yopsaas.db.domain.YopsaasCategory;
import com.yongche.yopsaas.db.domain.YopsaasCategoryExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YopsaasCategoryService {
    @Resource
    private YopsaasCategoryMapper categoryMapper;
    private YopsaasCategory.Column[] CHANNEL = {YopsaasCategory.Column.id, YopsaasCategory.Column.name, YopsaasCategory.Column.iconUrl};

    public List<YopsaasCategory> queryL1WithoutRecommend(int offset, int limit) {
        YopsaasCategoryExample example = new YopsaasCategoryExample();
        example.or().andLevelEqualTo("L1").andNameNotEqualTo("推荐").andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return categoryMapper.selectByExample(example);
    }

    public List<YopsaasCategory> queryL1(int offset, int limit) {
        YopsaasCategoryExample example = new YopsaasCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        return categoryMapper.selectByExample(example);
    }

    public List<YopsaasCategory> queryL1() {
        YopsaasCategoryExample example = new YopsaasCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public List<YopsaasCategory> queryByPid(Integer pid) {
        YopsaasCategoryExample example = new YopsaasCategoryExample();
        example.or().andPidEqualTo(pid).andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public List<YopsaasCategory> queryL2ByIds(List<Integer> ids) {
        YopsaasCategoryExample example = new YopsaasCategoryExample();
        example.or().andIdIn(ids).andLevelEqualTo("L2").andDeletedEqualTo(false);
        return categoryMapper.selectByExample(example);
    }

    public YopsaasCategory findById(Integer id) {
        return categoryMapper.selectByPrimaryKey(id);
    }

    public List<YopsaasCategory> querySelective(String id, String name, Integer page, Integer size, String sort, String order) {
        YopsaasCategoryExample example = new YopsaasCategoryExample();
        YopsaasCategoryExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(id)) {
            criteria.andIdEqualTo(Integer.valueOf(id));
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return categoryMapper.selectByExample(example);
    }

    public int updateById(YopsaasCategory category) {
        category.setUpdateTime(LocalDateTime.now());
        return categoryMapper.updateByPrimaryKeySelective(category);
    }

    public void deleteById(Integer id) {
        categoryMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(YopsaasCategory category) {
        category.setAddTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        categoryMapper.insertSelective(category);
    }

    public List<YopsaasCategory> queryChannel() {
        YopsaasCategoryExample example = new YopsaasCategoryExample();
        example.or().andLevelEqualTo("L1").andDeletedEqualTo(false);
        return categoryMapper.selectByExampleSelective(example, CHANNEL);
    }
}
