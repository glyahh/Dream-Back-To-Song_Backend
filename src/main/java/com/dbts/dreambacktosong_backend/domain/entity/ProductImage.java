package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

/**
 * 商品图片实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：product_images
 * <p>用途：映射商品图片表数据，一个商品可有多张图片，通过 sort_order 排序
 *
 * @author Dream-Back-To-Song
 */
@Data
public class ProductImage {

    /** 主键 ID */
    private Long id;

    /** 商品 ID（外键关联 products.id） */
    private Long productId;

    /** 图片 URL 地址 */
    private String url;

    /** 排序值（数值越小越靠前） */
    private Integer sortOrder;
}
