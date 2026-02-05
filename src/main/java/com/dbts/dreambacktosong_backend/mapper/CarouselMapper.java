package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.Carousel;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 轮播图相关数据库操作
 */
@Mapper
public interface CarouselMapper {

    List<Carousel> findAll();
}

