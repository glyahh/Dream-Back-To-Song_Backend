package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：users
 * <p>用途：映射用户表数据，用于 MyBatis 查询与持久化
 *
 * @author Dream-Back-To-Song
 */
@Data
public class User {

    /** 主键 ID */
    private Long id;

    /** 手机号（唯一索引） */
    private String phone;

    /** 昵称（最多 16 字） */
    private String nickname;

    /** 头像 URL 地址 */
    private String avatar;

    /** 个性签名（最多 40 字） */
    private String bio;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间（自动更新） */
    private LocalDateTime updatedAt;
}
