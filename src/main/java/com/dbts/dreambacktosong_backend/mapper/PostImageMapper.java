package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.PostImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 帖子图片相关数据库操作
 */
@Mapper
public interface PostImageMapper {

    List<PostImage> findByPostId(@Param("postId") Long postId);
}

