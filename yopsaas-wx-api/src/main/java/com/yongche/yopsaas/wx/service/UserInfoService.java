package com.yongche.yopsaas.wx.service;

import com.yongche.yopsaas.db.domain.LitemallUser;
import com.yongche.yopsaas.db.service.LitemallUserService;
import com.yongche.yopsaas.wx.dto.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@Service
public class UserInfoService {
    @Autowired
    private LitemallUserService userService;


    public UserInfo getInfo(Integer userId) {
        LitemallUser user = userService.findById(userId);
        Assert.state(user != null, "用户不存在");
        UserInfo userInfo = new UserInfo();
        userInfo.setNickName(user.getNickname());
        userInfo.setAvatarUrl(user.getAvatar());
        return userInfo;
    }
}
