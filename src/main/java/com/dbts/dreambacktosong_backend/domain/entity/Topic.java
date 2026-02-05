package com.dbts.dreambacktosong_backend.domain.entity;

import lombok.Data;

/**
 * 话题实体类（Entity）
 *
 * <p>分类：Entity - 数据库实体类
 * <p>对应数据库表：topics
 * <p>用途：映射话题表数据，用于发现页话题广场展示，通过 sort_order 排序
 * <p>话题键示例：life（生活在宋朝）、clothing（宋韵服饰大赏）、food（宋朝美食盛宴）、overview（宋代衣食住行）
 *
 * @author Dream-Back-To-Song
 */
@Data
public class Topic {

    /** 主键 ID */
    private Long id;

    /** 话题键（唯一索引，如：life/clothing/food/overview） */
    private String key;

    /** 话题标题（如：#生活在宋朝） */
    private String title;

    /** 话题图片 URL（可为空） */
    private String image;

    /** 跳转路径（可为空，如：/pages/discover/topic_life） */
    private String link;

    /** 排序值（数值越小越靠前） */
    private Integer sortOrder;
}
