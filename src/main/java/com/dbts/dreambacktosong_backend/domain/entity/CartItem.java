package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 购物车明细实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：cart_items
 * <p>用途：映射购物车明细表数据，存储用户加入购物车的商品及数量
 * <p>唯一约束：同用户同商品同款式仅一条记录（通过唯一索引 uk_user_product_style 保证）
 *
 * @author Dream-Back-To-Song
 */
@Data
public class CartItem {

    /** 主键 ID */
    private Long id;

    /** 用户 ID（外键关联 users.id） */
    private Long userId;

    /** 商品 ID（外键关联 products.id） */
    private Long productId;

    /** 款式 ID（外键关联 product_styles.id，可为空） */
    private Long styleId;

    /** 数量（默认 1） */
    private Integer qty;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间（自动更新） */
    private LocalDateTime updatedAt;
}
