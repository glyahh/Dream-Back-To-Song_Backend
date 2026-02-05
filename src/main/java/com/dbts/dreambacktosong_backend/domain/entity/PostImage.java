package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

/**
 * 帖子图片实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：post_images
 * <p>用途：映射帖子图片表数据，一个帖子可有多张图片，通过 sort_order 排序
 *
 * @author Dream-Back-To-Song
 */
@Data
public class PostImage {

    /** 主键 ID */
    private Long id;

    /** 帖子 ID（外键关联 posts.id） */
    private Long postId;

    /** 图片 URL 地址 */
    private String url;

    /** 排序值（数值越小越靠前） */
    private Integer sortOrder;
}
