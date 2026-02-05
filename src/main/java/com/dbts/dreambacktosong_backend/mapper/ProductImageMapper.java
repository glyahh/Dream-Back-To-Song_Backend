package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.ProductImage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品图片相关数据库操作
 */
@Mapper
public interface ProductImageMapper {

    /** 根据商品 ID 查询所有图片 */
    List<ProductImage> findByProductId(@Param("productId") Long productId);
}

