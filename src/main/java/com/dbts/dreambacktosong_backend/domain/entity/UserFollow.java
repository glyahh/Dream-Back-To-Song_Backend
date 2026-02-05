package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 关注关系实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：user_follows
 * <p>用途：映射用户关注关系表数据，记录用户之间的关注关系
 * <p>唯一约束：同一用户不能重复关注同一用户（通过唯一索引 uk_follow 保证）
 * <p>业务约束：follower_id != following_id（不能关注自己）
 *
 * @author Dream-Back-To-Song
 */
@Data
public class UserFollow {

    /** 主键 ID */
    private Long id;

    /** 关注者用户 ID（外键关联 users.id，即"我"） */
    private Long followerId;

    /** 被关注者用户 ID（外键关联 users.id，即"被关注的人"） */
    private Long followingId;

    /** 关注时间 */
    private LocalDateTime createdAt;
}
