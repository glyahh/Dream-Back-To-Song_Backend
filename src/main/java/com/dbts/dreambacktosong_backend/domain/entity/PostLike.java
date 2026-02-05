package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 帖子点赞实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：post_likes
 * <p>用途：映射帖子点赞表数据，记录用户对帖子的点赞关系
 * <p>唯一约束：同一用户对同一帖子只能点赞一次（通过唯一索引 uk_post_user 保证）
 *
 * @author Dream-Back-To-Song
 */
@Data
public class PostLike {

    /** 主键 ID */
    private Long id;

    /** 帖子 ID（外键关联 posts.id） */
    private Long postId;

    /** 用户 ID（外键关联 users.id） */
    private Long userId;

    /** 点赞时间 */
    private LocalDateTime createdAt;
}
