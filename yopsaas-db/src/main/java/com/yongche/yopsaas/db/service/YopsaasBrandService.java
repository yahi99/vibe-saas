package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasBrandMapper;
import com.yongche.yopsaas.db.domain.YopsaasBrand;
import com.yongche.yopsaas.db.domain.YopsaasBrand.Column;
import com.yongche.yopsaas.db.domain.YopsaasBrandExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YopsaasBrandService {
    @Resource
    private YopsaasBrandMapper brandMapper;
    private Column[] columns = new Column[]{Column.id, Column.name, Column.desc, Column.picUrl, Column.floorPrice};

    public List<YopsaasBrand> query(Integer page, Integer limit, String sort, String order) {
        YopsaasBrandExample example = new YopsaasBrandExample();
        example.or().andDeletedEqualTo(false);
        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }
        PageHelper.startPage(page, limit);
        return brandMapper.selectByExampleSelective(example, columns);
    }

    public List<YopsaasBrand> query(Integer page, Integer limit) {
        return query(page, limit, null, null);
    }

    public YopsaasBrand findById(Integer id) {
        return brandMapper.selectByPrimaryKey(id);
    }

    public List<YopsaasBrand> querySelective(String id, String name, Integer page, Integer size, String sort, String order) {
        YopsaasBrandExample example = new YopsaasBrandExample();
        YopsaasBrandExample.Criteria criteria = example.createCriteria();

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
        return brandMapper.selectByExample(example);
    }

    public int updateById(YopsaasBrand brand) {
        brand.setUpdateTime(LocalDateTime.now());
        return brandMapper.updateByPrimaryKeySelective(brand);
    }

    public void deleteById(Integer id) {
        brandMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(YopsaasBrand brand) {
        brand.setAddTime(LocalDateTime.now());
        brand.setUpdateTime(LocalDateTime.now());
        brandMapper.insertSelective(brand);
    }

    public List<YopsaasBrand> all() {
        YopsaasBrandExample example = new YopsaasBrandExample();
        example.or().andDeletedEqualTo(false);
        return brandMapper.selectByExample(example);
    }
}
