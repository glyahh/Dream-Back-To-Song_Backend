# DTO 包说明

## 用途
数据传输对象（Data Transfer Object），用于从前端接收请求数据。

## 命名规范
- 类名以 `DTO` 结尾，如：`LoginDTO`、`CreateOrderDTO`
- 或者使用描述性名称，如：`LoginRequest`、`OrderCreateRequest`

## 使用场景
- Controller 层接收前端请求参数
- 参数验证（使用 `@Valid` 和 `jakarta.validation` 注解）
- 数据转换（DTO → Entity）

## 示例
```java
@Data
public class LoginDTO {
    @NotBlank(message = "手机号不能为空")
    private String phone;
    
    @NotBlank(message = "验证码不能为空")
    private String code;
}
```
