package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasNoticeMapper;
import com.yongche.yopsaas.db.domain.YopsaasNotice;
import com.yongche.yopsaas.db.domain.YopsaasNoticeAdmin;
import com.yongche.yopsaas.db.domain.YopsaasNoticeAdminExample;
import com.yongche.yopsaas.db.domain.YopsaasNoticeExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YopsaasNoticeService {
    @Resource
    private YopsaasNoticeMapper noticeMapper;


    public List<YopsaasNotice> querySelective(String title, String content, Integer page, Integer limit, String sort, String order) {
        YopsaasNoticeExample example = new YopsaasNoticeExample();
        YopsaasNoticeExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(title)) {
            criteria.andTitleLike("%" + title + "%");
        }
        if (!StringUtils.isEmpty(content)) {
            criteria.andContentLike("%" + content + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return noticeMapper.selectByExample(example);
    }

    public int updateById(YopsaasNotice notice) {
        notice.setUpdateTime(LocalDateTime.now());
        return noticeMapper.updateByPrimaryKeySelective(notice);
    }

    public void deleteById(Integer id) {
        noticeMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(YopsaasNotice notice) {
        notice.setAddTime(LocalDateTime.now());
        notice.setUpdateTime(LocalDateTime.now());
        noticeMapper.insertSelective(notice);
    }

    public YopsaasNotice findById(Integer id) {
        return noticeMapper.selectByPrimaryKey(id);
    }

    public void deleteByIds(List<Integer> ids) {
        YopsaasNoticeExample example = new YopsaasNoticeExample();
        example.or().andIdIn(ids).andDeletedEqualTo(false);
        YopsaasNotice notice = new YopsaasNotice();
        notice.setUpdateTime(LocalDateTime.now());
        notice.setDeleted(true);
        noticeMapper.updateByExampleSelective(notice, example);
    }
}
