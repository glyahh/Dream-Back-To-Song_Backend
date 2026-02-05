package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 系统消息相关数据库操作
 */
@Mapper
public interface MessageMapper {

    List<Message> findPage(@Param("userId") Long userId,
                           @Param("category") String category,
                           @Param("offset") int offset,
                           @Param("limit") int limit);

    long count(@Param("userId") Long userId,
               @Param("category") String category);

    int markRead(@Param("id") Long id, @Param("userId") Long userId);

    long countUnreadByCategory(@Param("userId") Long userId,
                               @Param("category") String category);
}

