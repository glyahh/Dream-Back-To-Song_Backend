# Domain 包结构说明

## 包分类

`domain` 包按照 DDD（领域驱动设计）原则，将类分为三个子包：

### 1. `entity` 包 - 数据库实体类
- **用途**：映射数据库表结构，用于 MyBatis 查询与持久化
- **特点**：与数据库表一一对应，包含所有字段映射
- **位置**：`com.dbts.dreambacktosong_backend.domain.entity`

**包含的实体类**：
- `User` - 用户表
- `UserSettings` - 用户设置表
- `Product` - 商品表
- `ProductImage` - 商品图片表
- `ProductStyle` - 商品款式表
- `CartItem` - 购物车明细表
- `Address` - 收货地址表
- `Order` - 订单主表
- `OrderItem` - 订单明细表
- `Logistics` - 物流信息表
- `Carousel` - 轮播图表
- `Topic` - 话题表
- `Post` - 帖子/动态表
- `PostImage` - 帖子图片表
- `PostLike` - 帖子点赞表
- `UserFollow` - 关注关系表
- `Collection` - 收藏表
- `Creation` - 创作表
- `Message` - 消息通知表
- `PrivateMessage` - 私信表
- `Feedback` - 意见反馈表

### 2. `dto` 包 - 数据传输对象（Data Transfer Object）
- **用途**：从前端接收数据的类
- **特点**：用于 Controller 层接收请求参数
- **位置**：`com.dbts.dreambacktosong_backend.domain.dto`
- **状态**：待创建（根据前端接口文档逐步添加）

### 3. `vo` 包 - 视图对象（View Object）
- **用途**：返回给前端的数据类
- **特点**：用于 Controller 层返回响应数据，可能包含多个 Entity 的组合或转换
- **位置**：`com.dbts.dreambacktosong_backend.domain.vo`
- **状态**：待创建（根据前端接口文档逐步添加）

## 分类原则

- **Entity**：与数据库表结构完全对应，用于数据持久化
- **DTO**：从前端传入的数据结构，可能只包含部分字段或不同的字段名
- **VO**：返回给前端的数据结构，可能包含计算字段、关联数据等

## 使用建议

1. **Service 层**：主要使用 Entity 类进行数据库操作
2. **Controller 层**：接收 DTO，返回 VO
3. **Mapper 层**：使用 Entity 类进行数据库映射
