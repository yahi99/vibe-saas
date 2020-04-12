package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasAddressMapper;
import com.yongche.yopsaas.db.domain.YopsaasAddress;
import com.yongche.yopsaas.db.domain.YopsaasAddressExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YopsaasAddressService {
    @Resource
    private YopsaasAddressMapper addressMapper;

    public List<YopsaasAddress> queryByUid(Integer uid) {
        YopsaasAddressExample example = new YopsaasAddressExample();
        example.or().andUserIdEqualTo(uid).andDeletedEqualTo(false);
        return addressMapper.selectByExample(example);
    }

    public YopsaasAddress query(Integer userId, Integer id) {
        YopsaasAddressExample example = new YopsaasAddressExample();
        example.or().andIdEqualTo(id).andUserIdEqualTo(userId).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(example);
    }

    public int add(YopsaasAddress address) {
        address.setAddTime(LocalDateTime.now());
        address.setUpdateTime(LocalDateTime.now());
        return addressMapper.insertSelective(address);
    }

    public int update(YopsaasAddress address) {
        address.setUpdateTime(LocalDateTime.now());
        return addressMapper.updateByPrimaryKeySelective(address);
    }

    public void delete(Integer id) {
        addressMapper.logicalDeleteByPrimaryKey(id);
    }

    public YopsaasAddress findDefault(Integer userId) {
        YopsaasAddressExample example = new YopsaasAddressExample();
        example.or().andUserIdEqualTo(userId).andIsDefaultEqualTo(true).andDeletedEqualTo(false);
        return addressMapper.selectOneByExample(example);
    }

    public void resetDefault(Integer userId) {
        YopsaasAddress address = new YopsaasAddress();
        address.setIsDefault(false);
        address.setUpdateTime(LocalDateTime.now());
        YopsaasAddressExample example = new YopsaasAddressExample();
        example.or().andUserIdEqualTo(userId).andDeletedEqualTo(false);
        addressMapper.updateByExampleSelective(address, example);
    }

    public List<YopsaasAddress> querySelective(Integer userId, String name, Integer page, Integer limit, String sort, String order) {
        YopsaasAddressExample example = new YopsaasAddressExample();
        YopsaasAddressExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return addressMapper.selectByExample(example);
    }
}
