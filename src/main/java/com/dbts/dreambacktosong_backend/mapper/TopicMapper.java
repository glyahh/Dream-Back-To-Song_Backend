package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.Topic;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 话题相关数据库操作
 */
@Mapper
public interface TopicMapper {

    List<Topic> findAll();
}

