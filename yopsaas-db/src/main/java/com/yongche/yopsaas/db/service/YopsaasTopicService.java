package com.yongche.yopsaas.db.service;

import com.github.pagehelper.PageHelper;
import com.yongche.yopsaas.db.dao.YopsaasTopicMapper;
import com.yongche.yopsaas.db.domain.YopsaasGroupon;
import com.yongche.yopsaas.db.domain.YopsaasTopic;
import com.yongche.yopsaas.db.domain.YopsaasTopic.Column;
import com.yongche.yopsaas.db.domain.YopsaasTopicExample;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class YopsaasTopicService {
    @Resource
    private YopsaasTopicMapper topicMapper;
    private Column[] columns = new Column[]{Column.id, Column.title, Column.subtitle, Column.price, Column.picUrl, Column.readCount};

    public List<YopsaasTopic> queryList(int offset, int limit) {
        return queryList(offset, limit, "add_time", "desc");
    }

    public List<YopsaasTopic> queryList(int offset, int limit, String sort, String order) {
        YopsaasTopicExample example = new YopsaasTopicExample();
        example.or().andDeletedEqualTo(false);
        example.setOrderByClause(sort + " " + order);
        PageHelper.startPage(offset, limit);
        return topicMapper.selectByExampleSelective(example, columns);
    }

    public int queryTotal() {
        YopsaasTopicExample example = new YopsaasTopicExample();
        example.or().andDeletedEqualTo(false);
        return (int) topicMapper.countByExample(example);
    }

    public YopsaasTopic findById(Integer id) {
        YopsaasTopicExample example = new YopsaasTopicExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        return topicMapper.selectOneByExampleWithBLOBs(example);
    }

    public List<YopsaasTopic> queryRelatedList(Integer id, int offset, int limit) {
        YopsaasTopicExample example = new YopsaasTopicExample();
        example.or().andIdEqualTo(id).andDeletedEqualTo(false);
        List<YopsaasTopic> topics = topicMapper.selectByExample(example);
        if (topics.size() == 0) {
            return queryList(offset, limit, "add_time", "desc");
        }
        YopsaasTopic topic = topics.get(0);

        example = new YopsaasTopicExample();
        example.or().andIdNotEqualTo(topic.getId()).andDeletedEqualTo(false);
        PageHelper.startPage(offset, limit);
        List<YopsaasTopic> relateds = topicMapper.selectByExampleWithBLOBs(example);
        if (relateds.size() != 0) {
            return relateds;
        }

        return queryList(offset, limit, "add_time", "desc");
    }

    public List<YopsaasTopic> querySelective(String title, String subtitle, Integer page, Integer limit, String sort, String order) {
        YopsaasTopicExample example = new YopsaasTopicExample();
        YopsaasTopicExample.Criteria criteria = example.createCriteria();

        if (!StringUtils.isEmpty(title)) {
            criteria.andTitleLike("%" + title + "%");
        }
        if (!StringUtils.isEmpty(subtitle)) {
            criteria.andSubtitleLike("%" + subtitle + "%");
        }
        criteria.andDeletedEqualTo(false);

        if (!StringUtils.isEmpty(sort) && !StringUtils.isEmpty(order)) {
            example.setOrderByClause(sort + " " + order);
        }

        PageHelper.startPage(page, limit);
        return topicMapper.selectByExampleWithBLOBs(example);
    }

    public int updateById(YopsaasTopic topic) {
        topic.setUpdateTime(LocalDateTime.now());
        YopsaasTopicExample example = new YopsaasTopicExample();
        example.or().andIdEqualTo(topic.getId());
        return topicMapper.updateByExampleSelective(topic, example);
    }

    public void deleteById(Integer id) {
        topicMapper.logicalDeleteByPrimaryKey(id);
    }

    public void add(YopsaasTopic topic) {
        topic.setAddTime(LocalDateTime.now());
        topic.setUpdateTime(LocalDateTime.now());
        topicMapper.insertSelective(topic);
    }


    public void deleteByIds(List<Integer> ids) {
        YopsaasTopicExample example = new YopsaasTopicExample();
        example.or().andIdIn(ids).andDeletedEqualTo(false);
        YopsaasTopic topic = new YopsaasTopic();
        topic.setUpdateTime(LocalDateTime.now());
        topic.setDeleted(true);
        topicMapper.updateByExampleSelective(topic, example);
    }
}
