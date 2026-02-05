package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.math.BigDecimal;

/**
 * 订单明细实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：order_items
 * <p>用途：映射订单明细表数据，存储订单中的商品明细信息
 * <p>说明：product_snapshot 字段保存下单时的商品快照（JSON），避免后续商品变更影响历史订单展示
 *
 * @author Dream-Back-To-Song
 */
@Data
public class OrderItem {

    /** 主键 ID */
    private Long id;

    /** 订单 ID（外键关联 orders.id） */
    private Long orderId;

    /** 商品 ID（外键关联 products.id） */
    private Long productId;

    /** 款式 ID（外键关联 product_styles.id，可为空） */
    private Long styleId;

    /** 商品快照（JSON 字符串，保存下单时的商品 name/img/price 等信息） */
    private String productSnapshot;

    /** 数量 */
    private Integer qty;

    /** 单价（DECIMAL(10,2)，下单时的价格） */
    private BigDecimal price;

    /** 小计（DECIMAL(10,2)，price * qty） */
    private BigDecimal subtotal;
}
