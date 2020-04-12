package com.yongche.yopsaas.wx.service;

import com.yongche.yopsaas.db.domain.YopsaasUser;
import com.yongche.yopsaas.db.service.YopsaasUserService;
import com.yongche.yopsaas.wx.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserInfoService {
    @Autowired
    private YopsaasUserService userService;


    public UserInfo getInfo(Integer userId) {
        YopsaasUser user = userService.findById(userId);
        Assert.state(user != null, "用户不存在");
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(user.getNickname());
        userInfo.setAvatarUrl(user.getAvatar());
        return userInfo;
    }
}
