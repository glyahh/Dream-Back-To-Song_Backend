# VO 包说明

## 用途
视图对象（View Object），用于返回给前端的数据结构。

## 命名规范
- 类名以 `VO` 结尾，如：`UserVO`、`OrderVO`
- 或者使用描述性名称，如：`UserInfo`、`OrderDetail`

## 使用场景
- Controller 层返回响应数据
- 数据转换（Entity → VO）
- 可能包含多个 Entity 的组合数据
- 可能包含计算字段、格式化字段等

## 示例
```java
@Data
public class UserVO {
    private Long id;
    private String nickname;
    private String avatar;
    private String bio;
    private Integer followCount;  // 关注数（计算字段）
    private Integer fansCount;   // 粉丝数（计算字段）
}
```
