package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.UserFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关注关系相关数据库操作
 */
@Mapper
public interface UserFollowMapper {

    UserFollow find(@Param("followerId") Long followerId,
                    @Param("followingId") Long followingId);

    int insert(UserFollow follow);

    int delete(@Param("followerId") Long followerId,
               @Param("followingId") Long followingId);

    List<UserFollow> findByFollower(@Param("followerId") Long followerId);
}

