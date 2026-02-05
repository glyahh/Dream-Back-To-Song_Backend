package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：products
 * <p>用途：映射商品表数据，存储商品基本信息、价格、标签等
 *
 * @author Dream-Back-To-Song
 */
@Data
public class Product {

    /** 主键 ID */
    private Long id;

    /** 商品名称 */
    private String name;

    /** 副标题 */
    private String subtitle;

    /** 商品描述 */
    private String description;

    /** 主图 URL（冗余字段，便于列表查询） */
    private String mainImg;

    /** 现价（DECIMAL(10,2)） */
    private BigDecimal price;

    /** 原价（DECIMAL(10,2)，可为空） */
    private BigDecimal originPrice;

    /** 单品标签（如：新品/热卖/自营/精选） */
    private String tag;

    /** 销量文案（如：已售800+） */
    private String salesText;

    /** 店铺名称 */
    private String shopName;

    /** 发货地 */
    private String shippingFrom;

    /** 服务说明（如：破损包退・极速退款・售后保障） */
    private String service;

    /** 商品类型（new=新品，series=系列，normal=普通） */
    private String type;

    /** 是否推荐/热门（0=否，1=是） */
    private Boolean isFeatured;

    /** 是否轮播图（0=否，1=是） */
    private Boolean isCarousel;

    /** 排序值（数值越小越靠前） */
    private Integer sortOrder;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间（自动更新） */
    private LocalDateTime updatedAt;
}
