package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 消息通知实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：messages
 * <p>用途：映射消息通知表数据，存储系统消息、互动消息、订单消息等
 * <p>消息分类：system（系统通知）、social（互动消息）、order（订单消息）
 *
 * @author Dream-Back-To-Song
 */
@Data
public class Message {

    /** 主键 ID */
    private Long id;

    /** 接收用户 ID（外键关联 users.id） */
    private Long userId;

    /** 消息分类（system/social/order） */
    private String category;

    /** 消息标题 */
    private String title;

    /** 消息摘要/预览（可为空） */
    private String preview;

    /** 完整消息内容（可为空） */
    private String content;

    /** 是否已读（0=未读，1=已读，默认 0） */
    private Boolean isRead;

    /** 关联 ID（如订单 ID，可为空） */
    private String relatedId;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
