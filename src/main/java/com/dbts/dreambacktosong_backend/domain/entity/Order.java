package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 订单主表实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：orders
 * <p>用途：映射订单主表数据，存储订单基本信息、状态、金额等
 * <p>订单状态枚举：pending（待付款）、paid（已支付）、shipped（待收货）、finished（已完成）、cancelled（已取消）
 *
 * @author Dream-Back-To-Song
 */
@Data
public class Order {

    /** 主键 ID */
    private Long id;

    /** 订单号（唯一索引，格式如：O{userId}{timestamp}） */
    private String orderNo;

    /** 用户 ID（外键关联 users.id） */
    private Long userId;

    /** 收货地址 ID（外键关联 addresses.id，可为空） */
    private Long addressId;

    /** 订单总金额（DECIMAL(10,2)） */
    private BigDecimal totalAmount;

    /** 订单状态（pending/paid/shipped/finished/cancelled，默认 pending） */
    private String status;

    /** 买家备注（可为空） */
    private String remark;

    /** 支付方式（wechat/alipay，可为空） */
    private String payType;

    /** 支付时间（可为空） */
    private LocalDateTime payAt;

    /** 发货时间（可为空） */
    private LocalDateTime shipAt;

    /** 确认收货时间（可为空） */
    private LocalDateTime confirmAt;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间（自动更新） */
    private LocalDateTime updatedAt;
}
