package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 物流信息实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：logistics
 * <p>用途：映射物流信息表数据，存储订单的物流公司、单号、轨迹等信息
 * <p>说明：一个订单对应一条物流记录（通过唯一索引 uk_order_id 保证）
 *
 * @author Dream-Back-To-Song
 */
@Data
public class Logistics {

    /** 主键 ID */
    private Long id;

    /** 订单 ID（外键关联 orders.id，唯一索引） */
    private Long orderId;

    /** 物流公司名称（如：汴京驿站） */
    private String company;

    /** 物流单号（如：SF1234567890） */
    private String trackingNo;

    /** 物流轨迹（JSON 字符串，格式：[{time,desc}]） */
    private String traces;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间（自动更新） */
    private LocalDateTime updatedAt;
}
