package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.PostLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 帖子点赞相关数据库操作
 */
@Mapper
public interface PostLikeMapper {

    PostLike find(@Param("postId") Long postId, @Param("userId") Long userId);

    int insert(PostLike like);

    int delete(@Param("postId") Long postId, @Param("userId") Long userId);
}

