package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.ProductStyle;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品款式相关数据库操作
 */
@Mapper
public interface ProductStyleMapper {

    /** 根据商品 ID 查询所有款式 */
    List<ProductStyle> findByProductId(@Param("productId") Long productId);
}

