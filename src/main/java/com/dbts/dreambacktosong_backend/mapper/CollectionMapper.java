package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.Collection;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收藏相关数据库操作
 */
@Mapper
public interface CollectionMapper {

    List<Collection> findByUserAndType(@Param("userId") Long userId,
                                       @Param("type") String type);

    Collection findOne(@Param("userId") Long userId,
                       @Param("type") String type,
                       @Param("targetId") String targetId);

    int insert(Collection collection);

    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}

