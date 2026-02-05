package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.CartItem;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 购物车相关数据库操作
 */
@Mapper
public interface CartItemMapper {

    /** 查询当前用户的所有购物车项 */
    List<CartItem> findByUserId(@Param("userId") Long userId);

    /** 根据用户 + 商品 + 款式查询单条记录 */
    CartItem findByUserAndProduct(@Param("userId") Long userId,
                                  @Param("productId") Long productId,
                                  @Param("styleId") Long styleId);

    /** 新增购物车项 */
    int insert(CartItem item);

    /** 更新数量 */
    int updateQty(@Param("id") Long id, @Param("qty") Integer qty);

    /** 根据 ID 删除 */
    int deleteById(@Param("id") Long id);

    /** 根据用户 + 商品 + 款式删除（用于 qty=0 的情况） */
    int deleteByUserAndProduct(@Param("userId") Long userId,
                               @Param("productId") Long productId,
                               @Param("styleId") Long styleId);
}

