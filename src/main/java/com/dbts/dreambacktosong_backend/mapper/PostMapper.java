package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.Post;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子相关数据库操作
 */
@Mapper
public interface PostMapper {

    int insert(Post post);

    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);

    Post findById(@Param("id") Long id);

    List<Post> findHot(@Param("keyword") String keyword,
                       @Param("offset") int offset,
                       @Param("limit") int limit);

    List<Post> findByUserIds(@Param("userIds") java.util.List<Long> userIds,
                             @Param("offset") int offset,
                             @Param("limit") int limit);

    List<Post> findByUserId(@Param("userId") Long userId);

    int updateLikesCount(@Param("id") Long id, @Param("delta") int delta);
}

