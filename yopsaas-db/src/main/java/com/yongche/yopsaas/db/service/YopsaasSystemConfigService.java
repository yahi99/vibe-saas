package com.yongche.yopsaas.db.service;

import com.yongche.yopsaas.db.dao.YopsaasSystemMapper;
import com.yongche.yopsaas.db.domain.YopsaasSystem;
import com.yongche.yopsaas.db.domain.YopsaasSystemExample;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class YopsaasSystemConfigService {
    @Resource
    private YopsaasSystemMapper systemMapper;

    public Map<String, String> queryAll() {
        YopsaasSystemExample example = new YopsaasSystemExample();
        example.or().andDeletedEqualTo(false);

        List<YopsaasSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> systemConfigs = new HashMap<>();
        for (YopsaasSystem item : systemList) {
            systemConfigs.put(item.getKeyName(), item.getKeyValue());
        }

        return systemConfigs;
    }

    public Map<String, String> listMail() {
        YopsaasSystemExample example = new YopsaasSystemExample();
        example.or().andKeyNameLike("yopsaas_mall_%").andDeletedEqualTo(false);
        List<YopsaasSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(YopsaasSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public Map<String, String> listWx() {
        YopsaasSystemExample example = new YopsaasSystemExample();
        example.or().andKeyNameLike("yopsaas_wx_%").andDeletedEqualTo(false);
        List<YopsaasSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(YopsaasSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public Map<String, String> listOrder() {
        YopsaasSystemExample example = new YopsaasSystemExample();
        example.or().andKeyNameLike("yopsaas_order_%").andDeletedEqualTo(false);
        List<YopsaasSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(YopsaasSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public Map<String, String> listExpress() {
        YopsaasSystemExample example = new YopsaasSystemExample();
        example.or().andKeyNameLike("yopsaas_express_%").andDeletedEqualTo(false);
        List<YopsaasSystem> systemList = systemMapper.selectByExample(example);
        Map<String, String> data = new HashMap<>();
        for(YopsaasSystem system : systemList){
            data.put(system.getKeyName(), system.getKeyValue());
        }
        return data;
    }

    public void updateConfig(Map<String, String> data) {
        for (Map.Entry<String, String> entry : data.entrySet()) {
            YopsaasSystemExample example = new YopsaasSystemExample();
            example.or().andKeyNameEqualTo(entry.getKey()).andDeletedEqualTo(false);

            YopsaasSystem system = new YopsaasSystem();
            system.setKeyName(entry.getKey());
            system.setKeyValue(entry.getValue());
            system.setUpdateTime(LocalDateTime.now());
            systemMapper.updateByExampleSelective(system, example);
        }

    }

    public void addConfig(String key, String value) {
        YopsaasSystem system = new YopsaasSystem();
        system.setKeyName(key);
        system.setKeyValue(value);
        system.setAddTime(LocalDateTime.now());
        system.setUpdateTime(LocalDateTime.now());
        systemMapper.insertSelective(system);
    }
}
