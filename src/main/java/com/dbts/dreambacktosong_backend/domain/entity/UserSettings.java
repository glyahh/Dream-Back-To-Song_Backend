package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户设置实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：user_settings
 * <p>用途：映射用户设置表数据，存储用户的消息通知、声音提示等偏好设置
 *
 * @author Dream-Back-To-Song
 */
@Data
public class UserSettings {

    /** 主键 ID */
    private Long id;

    /** 用户 ID（外键关联 users.id，唯一索引，每用户一条记录） */
    private Long userId;

    /** 是否接收消息通知（0=关，1=开，默认 1） */
    private Boolean notify;

    /** 是否开启声音提示（0=关，1=开，默认 1） */
    private Boolean sound;

    /** 是否开启节省流量模式（0=关，1=开，默认 0） */
    private Boolean dataSaver;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间（自动更新） */
    private LocalDateTime updatedAt;
}
