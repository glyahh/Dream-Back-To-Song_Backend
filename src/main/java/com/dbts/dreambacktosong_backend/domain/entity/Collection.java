package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收藏实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：collections
 * <p>用途：映射收藏表数据，存储用户对商品或内容的收藏记录
 * <p>唯一约束：同用户同类型同目标仅一条记录（通过唯一索引 uk_user_type_target 保证）
 * <p>类型说明：type = "goods"（好物收藏）或 "notes"（内容收藏）
 *
 * @author Dream-Back-To-Song
 */
@Data
public class Collection {

    /** 主键 ID */
    private Long id;

    /** 用户 ID（外键关联 users.id） */
    private Long userId;

    /** 收藏类型（goods=好物，notes=内容） */
    private String type;

    /** 目标 ID（商品 ID 或帖子 ID，字符串类型） */
    private String targetId;

    /** 扩展信息（JSON 字符串，notes 类型时可存 title、excerpt、from 等） */
    private String extra;

    /** 收藏时间 */
    private LocalDateTime createdAt;
}
