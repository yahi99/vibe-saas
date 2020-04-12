package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasFeedbackMapper;
import com.yongche.yopsaas.db.domain.YopsaasFeedback;
import com.yongche.yopsaas.db.domain.YopsaasFeedbackExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author Peter
 * @date 2018/8/27 11:39
 */
@Service
public class YopsaasFeedbackService {
    @Autowired
    private YopsaasFeedbackMapper feedbackMapper;

    public Integer add(YopsaasFeedback feedback) {
        feedback.setAddTime(LocalDateTime.now());
        feedback.setUpdateTime(LocalDateTime.now());
        return feedbackMapper.insertSelective(feedback);
    }

    public List<YopsaasFeedback> querySelective(Integer userId, String username, Integer page, Integer limit, String sort, String order) {
        YopsaasFeedbackExample example = new YopsaasFeedbackExample();
        YopsaasFeedbackExample.Criteria criteria = example.createCriteria();

        if (userId != null) {
            criteria.andUserIdEqualTo(userId);
        }
        if (!StringUtils.isEmpty(username)) {
            criteria.andUsernameLike("%" + username + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return feedbackMapper.selectByExample(example);
    }
}
