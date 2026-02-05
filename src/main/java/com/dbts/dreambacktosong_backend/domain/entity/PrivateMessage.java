package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 私信实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：private_messages
 * <p>用途：映射私信表数据，存储用户之间的私信消息
 *
 * @author Dream-Back-To-Song
 */
@Data
public class PrivateMessage {

    /** 主键 ID */
    private Long id;

    /** 发送者用户 ID（外键关联 users.id） */
    private Long senderId;

    /** 接收者用户 ID（外键关联 users.id） */
    private Long receiverId;

    /** 私信内容 */
    private String content;

    /** 是否已读（0=未读，1=已读，默认 0） */
    private Boolean isRead;

    /** 创建时间 */
    private LocalDateTime createdAt;
}
