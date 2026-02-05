package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帖子/动态实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：posts
 * <p>用途：映射帖子表数据，存储用户发布的动态内容
 * <p>说明：likes_count 为冗余字段，便于按热度排序，实际点赞数通过 post_likes 表统计
 *
 * @author Dream-Back-To-Song
 */
@Data
public class Post {

    /** 主键 ID */
    private Long id;

    /** 发布者用户 ID（外键关联 users.id） */
    private Long userId;

    /** 话题标签（如：#生活在宋朝，可为空） */
    private String topic;

    /** 正文内容（最多 120 字） */
    private String content;

    /** 点赞数（冗余字段，便于排序，实际值需结合 post_likes 表） */
    private Integer likesCount;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间（自动更新） */
    private LocalDateTime updatedAt;
}
