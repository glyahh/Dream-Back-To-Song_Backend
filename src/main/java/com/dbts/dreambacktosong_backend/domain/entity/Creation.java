package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 创作实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：creations
 * <p>用途：映射创作表数据，存储用户的创作内容（服饰设计、饮食随笔、建筑手稿等）
 * <p>创作类型：服饰设计、饮食随笔、建筑手稿、出行见闻、其他
 *
 * @author Dream-Back-To-Song
 */
@Data
public class Creation {

    /** 主键 ID */
    private Long id;

    /** 用户 ID（外键关联 users.id） */
    private Long userId;

    /** 创作标题 */
    private String title;

    /** 创作类型（服饰设计/饮食随笔/建筑手稿/出行见闻/其他） */
    private String type;

    /** 正文内容（最多 400 字） */
    private String content;

    /** 摘要（前 50 字，可为空） */
    private String excerpt;

    /** 字数统计 */
    private Integer words;

    /** 心情标签（可为空） */
    private String mood;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间（自动更新） */
    private LocalDateTime updatedAt;
}
