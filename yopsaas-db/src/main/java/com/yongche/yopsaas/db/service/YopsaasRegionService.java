package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasRegionMapper;
import com.yongche.yopsaas.db.domain.YopsaasRegion;
import com.yongche.yopsaas.db.domain.YopsaasRegionExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class YopsaasRegionService {

    @Resource
    private YopsaasRegionMapper regionMapper;

    public List<YopsaasRegion> getAll(){
        YopsaasRegionExample example = new YopsaasRegionExample();
        byte b = 4;
        example.or().andTypeNotEqualTo(b);
        return regionMapper.selectByExample(example);
    }

    public List<YopsaasRegion> queryByPid(Integer parentId) {
        YopsaasRegionExample example = new YopsaasRegionExample();
        example.or().andPidEqualTo(parentId);
        return regionMapper.selectByExample(example);
    }

    public YopsaasRegion findById(Integer id) {
        return regionMapper.selectByPrimaryKey(id);
    }

    public List<YopsaasRegion> querySelective(String name, Integer code, Integer page, Integer size, String sort, String order) {
        YopsaasRegionExample example = new YopsaasRegionExample();
        YopsaasRegionExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(name)) {
            criteria.andNameLike("%" + name + "%");
        }
        if (!StringUtils.isEmpty(code)) {
            criteria.andCodeEqualTo(code);
        }

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, size);
        return regionMapper.selectByExample(example);
    }

}
