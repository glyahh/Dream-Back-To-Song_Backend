package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.Creation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 创作相关数据库操作
 */
@Mapper
public interface CreationMapper {

    int insert(Creation creation);

    Creation findById(@Param("id") Long id);

    List<Creation> findByUserId(@Param("userId") Long userId);

    int deleteByIdAndUserId(@Param("id") Long id, @Param("userId") Long userId);
}

