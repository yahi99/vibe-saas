package com.yongche.yopsaas.db.service;

import com.alibaba.druid.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasRoleMapper;
import com.yongche.yopsaas.db.domain.YopsaasRole;
import com.yongche.yopsaas.db.domain.YopsaasRoleExample;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class YopsaasRoleService {
    @Resource
    private YopsaasRoleMapper roleMapper;


    public Set<String> queryByIds(Integer[] roleIds) {
        Set<String> roles = new HashSet<String>();
        if(roleIds.length == 0){
            return roles;
        }

        YopsaasRoleExample example = new YopsaasRoleExample();
        example.or().andIdIn(Arrays.asList(roleIds)).andEnabledEqualTo(true).andDeletedEqualTo(false);
        List<YopsaasRole> roleList = roleMapper.selectByExample(example);

        for(YopsaasRole role : roleList){
            roles.add(role.getName());
        }

        return roles;

    }

    public List<YopsaasRole> querySelective(String name, Integer page, Integer limit, String sort, String order) {
        YopsaasRoleExample example = new YopsaasRoleExample();
        YopsaasRoleExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return roleMapper.selectByExample(example);
    }

    public YopsaasRole findById(Integer id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    public void add(YopsaasRole role) {
        role.setAddTime(LocalDateTime.now());
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.insertSelective(role);
    }

    public void deleteById(Integer id) {
        roleMapper.logicalDeleteByPrimaryKey(id);
    }

    public void updateById(YopsaasRole role) {
        role.setUpdateTime(LocalDateTime.now());
        roleMapper.updateByPrimaryKeySelective(role);
    }

    public boolean checkExist(String name) {
        YopsaasRoleExample example = new YopsaasRoleExample();
        example.or().andNameEqualTo(name).andDeletedEqualTo(false);
        return roleMapper.countByExample(example) != 0;
    }

    public List<YopsaasRole> queryAll() {
        YopsaasRoleExample example = new YopsaasRoleExample();
        example.or().andDeletedEqualTo(false);
        return roleMapper.selectByExample(example);
    }
}
