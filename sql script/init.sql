-- ============================================
-- 梦回宋朝 · 数据库建表脚本
-- 数据库：MySQL 5.7+ / 8.0
-- 字符集：utf8mb4
-- ============================================

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================
-- 3.1 用户与认证
-- ============================================

-- 用户表
CREATE TABLE `users` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号，唯一',
  `nickname` VARCHAR(32) NOT NULL COMMENT '昵称，最多16字',
  `avatar` VARCHAR(512) DEFAULT NULL COMMENT '头像URL',
  `bio` VARCHAR(80) DEFAULT NULL COMMENT '签名，最多40字',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_phone` (`phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 用户设置表
CREATE TABLE `user_settings` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `notify` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '消息通知：0关1开',
  `sound` TINYINT(1) NOT NULL DEFAULT 1 COMMENT '声音提示：0关1开',
  `data_saver` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '节省流量：0关1开',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_id` (`user_id`),
  CONSTRAINT `fk_settings_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户设置表';

-- 短信验证码表（可选）
CREATE TABLE `sms_codes` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
  `code` VARCHAR(6) NOT NULL COMMENT '验证码',
  `expires_at` DATETIME NOT NULL COMMENT '过期时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_phone_expires` (`phone`, `expires_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='短信验证码表';

-- 登录Token表（可选，若采用JWT无状态鉴权可省略）
CREATE TABLE `auth_tokens` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `token` VARCHAR(128) NOT NULL COMMENT 'Token字符串',
  `expires_at` DATETIME NOT NULL COMMENT '过期时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_token` (`token`),
  CONSTRAINT `fk_token_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='登录Token表';

-- ============================================
-- 3.2 商品与集市
-- ============================================

-- 商品表
CREATE TABLE `products` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键，自增',
  `name` VARCHAR(128) NOT NULL COMMENT '商品名称',
  `subtitle` VARCHAR(128) DEFAULT NULL COMMENT '副标题',
  `description` VARCHAR(512) DEFAULT NULL COMMENT '描述',
  `main_img` VARCHAR(512) DEFAULT NULL COMMENT '主图URL（冗余，便于列表展示）',
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '现价',
  `origin_price` DECIMAL(10,2) DEFAULT NULL COMMENT '原价',
  `tag` VARCHAR(16) DEFAULT NULL COMMENT '单品标签：新品/热卖/自营/精选',
  `sales_text` VARCHAR(32) DEFAULT NULL COMMENT '销量文案：已售800+',
  `shop_name` VARCHAR(64) DEFAULT NULL COMMENT '店铺名',
  `shipping_from` VARCHAR(64) DEFAULT NULL COMMENT '发货地',
  `service` VARCHAR(256) DEFAULT NULL COMMENT '服务说明',
  `type` VARCHAR(16) NOT NULL DEFAULT 'normal' COMMENT '类型：new/series/normal',
  `is_featured` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否推荐/热门：0否1是',
  `is_carousel` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否轮播图：0否1是',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序，数值越小越靠前',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_type` (`type`),
  KEY `idx_featured` (`is_featured`),
  KEY `idx_carousel` (`is_carousel`),
  KEY `idx_sort` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品表';

-- 商品图片表
CREATE TABLE `product_images` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `url` VARCHAR(512) NOT NULL COMMENT '图片URL',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_image_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品图片表';

-- 商品款式/规格表
CREATE TABLE `product_styles` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `name` VARCHAR(64) NOT NULL COMMENT '款式名',
  `thumb` VARCHAR(512) DEFAULT NULL COMMENT '缩略图URL',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_style_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品款式/规格表';

-- 商品标签关联表（可选，也可用products.tags JSON字段简化）
CREATE TABLE `product_tags` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `tag` VARCHAR(32) NOT NULL COMMENT '标签：包邮/满减/换购等',
  PRIMARY KEY (`id`),
  KEY `idx_product_id` (`product_id`),
  CONSTRAINT `fk_tag_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='商品标签关联表';

-- ============================================
-- 3.3 购物车
-- ============================================

-- 购物车明细表
CREATE TABLE `cart_items` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `style_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '款式ID',
  `qty` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '数量',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_product_style` (`user_id`, `product_id`, `style_id`),
  CONSTRAINT `fk_cart_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cart_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_cart_style` FOREIGN KEY (`style_id`) REFERENCES `product_styles` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='购物车明细表';

-- ============================================
-- 3.4 地址与订单
-- ============================================

-- 收货地址表
CREATE TABLE `addresses` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `name` VARCHAR(32) NOT NULL COMMENT '收货人',
  `phone` VARCHAR(11) NOT NULL COMMENT '手机号',
  `province` VARCHAR(32) NOT NULL COMMENT '省',
  `city` VARCHAR(32) NOT NULL COMMENT '市',
  `district` VARCHAR(32) DEFAULT NULL COMMENT '区',
  `detail` VARCHAR(256) NOT NULL COMMENT '详细地址',
  `is_default` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否默认：0否1是',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  CONSTRAINT `fk_address_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收货地址表';

-- 订单主表
CREATE TABLE `orders` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_no` VARCHAR(32) NOT NULL COMMENT '订单号，唯一',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `address_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '收货地址ID',
  `total_amount` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '订单总金额',
  `status` VARCHAR(16) NOT NULL DEFAULT 'pending' COMMENT '状态：pending/paid/shipped/finished/cancelled',
  `remark` VARCHAR(256) DEFAULT NULL COMMENT '买家备注',
  `pay_type` VARCHAR(16) DEFAULT NULL COMMENT '支付方式：wechat/alipay',
  `pay_at` DATETIME DEFAULT NULL COMMENT '支付时间',
  `ship_at` DATETIME DEFAULT NULL COMMENT '发货时间',
  `confirm_at` DATETIME DEFAULT NULL COMMENT '确认收货时间',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_no` (`order_no`),
  KEY `idx_user_status` (`user_id`, `status`),
  KEY `idx_created` (`created_at`),
  CONSTRAINT `fk_order_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_address` FOREIGN KEY (`address_id`) REFERENCES `addresses` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单主表';

-- 订单明细表
CREATE TABLE `order_items` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `product_id` BIGINT UNSIGNED NOT NULL COMMENT '商品ID',
  `style_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '款式ID',
  `product_snapshot` JSON DEFAULT NULL COMMENT '下单时商品快照：name,img,price等',
  `qty` INT UNSIGNED NOT NULL DEFAULT 1 COMMENT '数量',
  `price` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '单价',
  `subtotal` DECIMAL(10,2) NOT NULL DEFAULT 0 COMMENT '小计',
  PRIMARY KEY (`id`),
  KEY `idx_order_id` (`order_id`),
  CONSTRAINT `fk_order_item_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_item_product` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_order_item_style` FOREIGN KEY (`style_id`) REFERENCES `product_styles` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='订单明细表';

-- 物流信息表
CREATE TABLE `logistics` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `order_id` BIGINT UNSIGNED NOT NULL COMMENT '订单ID',
  `company` VARCHAR(64) DEFAULT NULL COMMENT '物流公司',
  `tracking_no` VARCHAR(64) DEFAULT NULL COMMENT '物流单号',
  `traces` JSON DEFAULT NULL COMMENT '轨迹：[{time,desc}]',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_order_id` (`order_id`),
  CONSTRAINT `fk_logistics_order` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物流信息表';

-- ============================================
-- 3.5 发现与内容
-- ============================================

-- 轮播图表
CREATE TABLE `carousel` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `image` VARCHAR(512) NOT NULL COMMENT '图片URL',
  `title` VARCHAR(64) DEFAULT NULL COMMENT '标题',
  `link` VARCHAR(256) DEFAULT NULL COMMENT '跳转链接',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_sort` (`sort_order`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='轮播图表';

-- 话题表
CREATE TABLE `topics` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `key` VARCHAR(32) NOT NULL COMMENT '话题键：life/clothing/food/overview',
  `title` VARCHAR(64) NOT NULL COMMENT '标题：#生活在宋朝',
  `image` VARCHAR(512) DEFAULT NULL COMMENT '图片URL',
  `link` VARCHAR(256) DEFAULT NULL COMMENT '跳转路径',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='话题表';

-- 帖子/动态表
CREATE TABLE `posts` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '发布者ID',
  `topic` VARCHAR(64) DEFAULT NULL COMMENT '话题：#生活在宋朝',
  `content` VARCHAR(256) NOT NULL COMMENT '正文，最多120字',
  `likes_count` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '点赞数（冗余，便于排序）',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created` (`created_at`),
  KEY `idx_likes` (`likes_count`),
  CONSTRAINT `fk_post_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子/动态表';

-- 帖子图片表
CREATE TABLE `post_images` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `post_id` BIGINT UNSIGNED NOT NULL COMMENT '帖子ID',
  `url` VARCHAR(512) NOT NULL COMMENT '图片URL',
  `sort_order` INT NOT NULL DEFAULT 0 COMMENT '排序',
  PRIMARY KEY (`id`),
  KEY `idx_post_id` (`post_id`),
  CONSTRAINT `fk_post_image_post` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子图片表';

-- 关注关系表
CREATE TABLE `user_follows` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `follower_id` BIGINT UNSIGNED NOT NULL COMMENT '关注者（我）',
  `following_id` BIGINT UNSIGNED NOT NULL COMMENT '被关注者',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_follow` (`follower_id`, `following_id`),
  KEY `idx_follower` (`follower_id`),
  KEY `idx_following` (`following_id`),
  CONSTRAINT `fk_follow_follower` FOREIGN KEY (`follower_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_follow_following` FOREIGN KEY (`following_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `chk_follow_not_self` CHECK (`follower_id` != `following_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关注关系表';

-- 帖子点赞表
CREATE TABLE `post_likes` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `post_id` BIGINT UNSIGNED NOT NULL COMMENT '帖子ID',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_post_user` (`post_id`, `user_id`),
  CONSTRAINT `fk_like_post` FOREIGN KEY (`post_id`) REFERENCES `posts` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_like_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子点赞表';

-- ============================================
-- 3.6 收藏
-- ============================================

-- 收藏表
CREATE TABLE `collections` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `type` VARCHAR(16) NOT NULL COMMENT '类型：goods/notes',
  `target_id` VARCHAR(64) NOT NULL COMMENT '目标ID：商品ID或帖子ID',
  `extra` JSON DEFAULT NULL COMMENT '扩展：notes时存title、excerpt、from',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_user_type_target` (`user_id`, `type`, `target_id`),
  KEY `idx_user_type` (`user_id`, `type`),
  CONSTRAINT `fk_collection_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='收藏表';

-- ============================================
-- 3.7 创作
-- ============================================

-- 创作表
CREATE TABLE `creations` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '用户ID',
  `title` VARCHAR(64) NOT NULL COMMENT '标题',
  `type` VARCHAR(32) NOT NULL COMMENT '类型：服饰设计/饮食随笔/建筑手稿/出行见闻/其他',
  `content` TEXT NOT NULL COMMENT '正文，最多400字',
  `excerpt` VARCHAR(128) DEFAULT NULL COMMENT '摘要（前50字）',
  `words` INT UNSIGNED NOT NULL DEFAULT 0 COMMENT '字数',
  `mood` VARCHAR(32) DEFAULT NULL COMMENT '心情标签',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created` (`created_at`),
  CONSTRAINT `fk_creation_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='创作表';

-- ============================================
-- 3.8 消息与反馈
-- ============================================

-- 消息通知表
CREATE TABLE `messages` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT UNSIGNED NOT NULL COMMENT '接收用户ID',
  `category` VARCHAR(16) NOT NULL COMMENT '分类：system/social/order',
  `title` VARCHAR(128) NOT NULL COMMENT '标题',
  `preview` VARCHAR(256) DEFAULT NULL COMMENT '摘要',
  `content` TEXT DEFAULT NULL COMMENT '完整内容',
  `is_read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读：0否1是',
  `related_id` VARCHAR(64) DEFAULT NULL COMMENT '关联ID：订单ID等',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_category` (`user_id`, `category`),
  KEY `idx_read` (`user_id`, `is_read`),
  CONSTRAINT `fk_message_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消息通知表';

-- 私信表
CREATE TABLE `private_messages` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `sender_id` BIGINT UNSIGNED NOT NULL COMMENT '发送者ID',
  `receiver_id` BIGINT UNSIGNED NOT NULL COMMENT '接收者ID',
  `content` VARCHAR(512) NOT NULL COMMENT '内容',
  `is_read` TINYINT(1) NOT NULL DEFAULT 0 COMMENT '是否已读',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_receiver` (`receiver_id`),
  KEY `idx_conversation` (`sender_id`, `receiver_id`),
  CONSTRAINT `fk_pm_sender` FOREIGN KEY (`sender_id`) REFERENCES `users` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_pm_receiver` FOREIGN KEY (`receiver_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='私信表';

-- 意见反馈表
CREATE TABLE `feedback` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_id` BIGINT UNSIGNED DEFAULT NULL COMMENT '用户ID（未登录可为空）',
  `rate` TINYINT UNSIGNED NOT NULL DEFAULT 5 COMMENT '评分1-5',
  `tags` JSON DEFAULT NULL COMMENT '标签：["界面设计","交互体验"]',
  `content` VARCHAR(600) NOT NULL COMMENT '描述，最多300字',
  `contact` VARCHAR(128) DEFAULT NULL COMMENT '联系方式',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_created` (`created_at`),
  CONSTRAINT `fk_feedback_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='意见反馈表';

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================
-- 脚本执行完成
-- ============================================
