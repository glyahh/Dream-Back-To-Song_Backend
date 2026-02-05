package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

/**
 * 轮播图实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：carousel
 * <p>用途：映射轮播图表数据，用于发现页顶部轮播展示，通过 sort_order 排序
 *
 * @author Dream-Back-To-Song
 */
@Data
public class Carousel {

    /** 主键 ID */
    private Long id;

    /** 轮播图片 URL */
    private String image;

    /** 轮播图标题（可为空） */
    private String title;

    /** 跳转链接（可为空） */
    private String link;

    /** 排序值（数值越小越靠前） */
    private Integer sortOrder;
}
