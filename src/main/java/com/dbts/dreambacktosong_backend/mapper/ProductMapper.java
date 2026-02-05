package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品相关数据库操作
 */
@Mapper
public interface ProductMapper {

    /** 按条件分页查询商品列表 */
    List<Product> findPage(@Param("type") String type,
                           @Param("keyword") String keyword,
                           @Param("offset") int offset,
                           @Param("limit") int limit);

    /** 统计商品总数 */
    long count(@Param("type") String type,
               @Param("keyword") String keyword);

    /** 根据主键查询商品 */
    Product findById(@Param("id") Long id);

    /** 获取热门或轮播商品列表 */
    List<Product> findFeatured(@Param("type") String type);

    /** 根据一组 ID 批量查询商品 */
    List<Product> findByIds(@Param("ids") java.util.List<Long> ids);
}

