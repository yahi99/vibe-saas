package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasStorageMapper;
import com.yongche.yopsaas.db.domain.YopsaasStorage;
import com.yongche.yopsaas.db.domain.YopsaasStorageExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class YopsaasStorageService {
    @Autowired
    private YopsaasStorageMapper storageMapper;

    public void deleteByKey(String key) {
        YopsaasStorageExample example = new YopsaasStorageExample();
        example.or().andKeyEqualTo(key);
        storageMapper.logicalDeleteByExample(example);
    }

    public void add(YopsaasStorage storageInfo) {
        storageInfo.setAddTime(LocalDateTime.now());
        storageInfo.setUpdateTime(LocalDateTime.now());
        storageMapper.insertSelective(storageInfo);
    }

    public YopsaasStorage findByKey(String key) {
        YopsaasStorageExample example = new YopsaasStorageExample();
        example.or().andKeyEqualTo(key).andDeletedEqualTo(false);
        return storageMapper.selectOneByExample(example);
    }

    public int update(YopsaasStorage storageInfo) {
        storageInfo.setUpdateTime(LocalDateTime.now());
        return storageMapper.updateByPrimaryKeySelective(storageInfo);
    }

    public YopsaasStorage findById(Integer id) {
        return storageMapper.selectByPrimaryKey(id);
    }

    public List<YopsaasStorage> querySelective(String key, String name, Integer page, Integer limit, String sort, String order) {
        YopsaasStorageExample example = new YopsaasStorageExample();
        YopsaasStorageExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(key)) {
            criteria.andKeyEqualTo(key);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return storageMapper.selectByExample(example);
    }
}
