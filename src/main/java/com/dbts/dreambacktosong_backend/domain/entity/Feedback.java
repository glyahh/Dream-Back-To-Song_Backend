package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 意见反馈实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：feedback
 * <p>用途：映射意见反馈表数据，存储用户的反馈信息（评分、标签、描述、联系方式等）
 * <p>说明：userId 可为空，支持匿名反馈
 *
 * @author Dream-Back-To-Song
 */
@Data
public class Feedback {

    /** 主键 ID */
    private Long id;

    /** 用户 ID（外键关联 users.id，可为空，支持匿名反馈） */
    private Long userId;

    /** 评分（1-5 星，默认 5） */
    private Integer rate;

    /** 反馈标签（JSON 字符串，如：["界面设计","交互体验"]） */
    private String tags;

    /** 反馈描述内容（最多 300 字） */
    private String content;

    /** 联系方式（可为空） */
    private String contact;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
