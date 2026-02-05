package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * 收货地址实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：addresses
 * <p>用途：映射收货地址表数据，存储用户的收货地址信息
 *
 * @author Dream-Back-To-Song
 */
@Data
public class Address {

    /** 主键 ID */
    private Long id;

    /** 用户 ID（外键关联 users.id） */
    private Long userId;

    /** 收货人姓名 */
    private String name;

    /** 收货人手机号 */
    private String phone;

    /** 省份 */
    private String province;

    /** 城市 */
    private String city;

    /** 区/县（可为空） */
    private String district;

    /** 详细地址 */
    private String detail;

    /** 是否默认地址（0=否，1=是） */
    private Boolean isDefault;

    /** 创建时间 */
    private LocalDateTime createdAt;

    /** 更新时间（自动更新） */
    private LocalDateTime updatedAt;
}
