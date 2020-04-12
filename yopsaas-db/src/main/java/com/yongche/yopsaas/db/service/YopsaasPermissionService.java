package com.yongche.yopsaas.db.service;

import com.yongche.yopsaas.db.dao.YopsaasPermissionMapper;
import com.yongche.yopsaas.db.dao.YopsaasRoleMapper;
import com.yongche.yopsaas.db.domain.YopsaasPermission;
import com.yongche.yopsaas.db.domain.YopsaasPermissionExample;
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
public class YopsaasPermissionService {
    @Resource
    private YopsaasPermissionMapper permissionMapper;

    public Set<String> queryByRoleIds(Integer[] roleIds) {
        Set<String> permissions = new HashSet<String>();
        if(roleIds.length == 0){
            return permissions;
        }

        YopsaasPermissionExample example = new YopsaasPermissionExample();
        example.or().andRoleIdIn(Arrays.asList(roleIds)).andDeletedEqualTo(false);
        List<YopsaasPermission> permissionList = permissionMapper.selectByExample(example);

        for(YopsaasPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }


    public Set<String> queryByRoleId(Integer roleId) {
        Set<String> permissions = new HashSet<String>();
        if(roleId == null){
            return permissions;
        }

        YopsaasPermissionExample example = new YopsaasPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        List<YopsaasPermission> permissionList = permissionMapper.selectByExample(example);

        for(YopsaasPermission permission : permissionList){
            permissions.add(permission.getPermission());
        }

        return permissions;
    }

    public boolean checkSuperPermission(Integer roleId) {
        if(roleId == null){
            return false;
        }

        YopsaasPermissionExample example = new YopsaasPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andPermissionEqualTo("*").andDeletedEqualTo(false);
        return permissionMapper.countByExample(example) != 0;
    }

    public void deleteByRoleId(Integer roleId) {
        YopsaasPermissionExample example = new YopsaasPermissionExample();
        example.or().andRoleIdEqualTo(roleId).andDeletedEqualTo(false);
        permissionMapper.logicalDeleteByExample(example);
    }

    public void add(YopsaasPermission yopsaasPermission) {
        yopsaasPermission.setAddTime(LocalDateTime.now());
        yopsaasPermission.setUpdateTime(LocalDateTime.now());
        permissionMapper.insertSelective(yopsaasPermission);
    }
}
