package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

/**
 * 商品款式/规格实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：product_styles
 * <p>用途：映射商品款式表数据，一个商品可有多款样式（如颜色、尺寸等），通过 sort_order 排序
 *
 * @author Dream-Back-To-Song
 */
@Data
public class ProductStyle {

    /** 主键 ID */
    private Long id;

    /** 商品 ID（外键关联 products.id） */
    private Long productId;

    /** 款式名称（如：青山远黛、青绿流苏） */
    private String name;

    /** 款式缩略图 URL */
    private String thumb;

    /** 排序值（数值越小越靠前） */
    private Integer sortOrder;
}
