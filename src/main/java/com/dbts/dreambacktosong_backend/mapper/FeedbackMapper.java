package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.Feedback;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 意见反馈相关数据库操作
 */
@Mapper
public interface FeedbackMapper {

    int insert(Feedback feedback);

    List<Feedback> findByUserId(@Param("userId") Long userId);
}

