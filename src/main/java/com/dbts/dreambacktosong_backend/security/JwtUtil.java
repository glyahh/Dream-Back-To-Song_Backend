package com.dbts.dreambacktosong_backend.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.dbts.dreambacktosong_backend.config.JwtProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 工具类
 *
 * <p>负责生成与校验登录 Token。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {

    // 校验tocken配置的密钥
    private final JwtProperties jwtProperties;

    /**
     * 生成 Token（基础版本，仅包含 userId 和 phone）
     *
     * @param userId 用户 ID
     * @param phone  手机号
     */
    public String generateToken(Long userId, String phone) {
        return generateToken(userId, phone, null);
    }

    /**
     * 生成 Token（支持自定义声明）
     *
     * <p>自定义声明（Private Claims）示例：
     * <pre>
     * Map&lt;String, Object&gt; claims = new HashMap&lt;&gt;();
     * claims.put("nickname", "烟雨朦胧");
     * claims.put("role", "user");
     * claims.put("vip", true);
     * String token = jwtUtil.generateToken(userId, phone, claims);
     * </pre>
     *
     * @param userId 用户 ID（会作为 subject）
     * @param phone  手机号（会作为 "phone" 声明）
     * @param customClaims 自定义声明 Map，可为 null（key 为声明名，value 为声明值，支持 String/Number/Boolean）
     */
    public String generateToken(Long userId, String phone, Map<String, Object> customClaims) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(jwtProperties.getExpiresIn());
        Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
        
        var builder = JWT.create()
                // 签发时间
                .withIssuedAt(Date.from(now))
                // 过期时间
                .withExpiresAt(Date.from(expiresAt))
                // 主题, 存储用户唯一标识ID
                .withSubject(String.valueOf(userId))
                // 单独声明一个phone类型的全局存储字段
                .withClaim("phone", phone);
        
        // 添加自定义声明（Private Claims）
        if (customClaims != null && !customClaims.isEmpty()) {
            // 将方法传入的参数的map<>全部取出, 挨个判断类型并添加到payload中
            // 由于是map, 也不怕重复添加, 重复的key会被覆盖=没有
            for (Map.Entry<String, Object> entry : customClaims.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                // 跳过已存在的标准字段，避免覆盖
                if ("phone".equals(key) || "sub".equals(key) || "iat".equals(key) || "exp".equals(key)) {
                    continue;
                }

                // 根据类型添加声明
                if (value instanceof String) {
                    builder.withClaim(key, (String) value);
                } else if (value instanceof Integer) {
                    builder.withClaim(key, (Integer) value);
                } else if (value instanceof Long) {
                    builder.withClaim(key, (Long) value);
                } else if (value instanceof Boolean) {
                    builder.withClaim(key, (Boolean) value);
                } else if (value instanceof Double) {
                    builder.withClaim(key, (Double) value);
                } else {
                    // 其他类型转为字符串
                    builder.withClaim(key, value.toString());
                }
            }
        }

        // 将userID翻译成token
        return builder.sign(algorithm);
    }

    /**
     * 校验并解析 Token
     *
     * @param token Bearer Token（不含前缀）
     * @return 解析后的 JWT
     */
    public DecodedJWT verifyToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(jwtProperties.getSecret());
            return JWT.require(algorithm).build().verify(token);
        } catch (JWTVerificationException e) {
            log.warn("Token 校验失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从 Token 中提取用户简单信息（基础字段）
     */
    public Map<String, Object> parseUserInfo(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("userId", Long.valueOf(jwt.getSubject()));
        map.put("phone", jwt.getClaim("phone").asString());
        return map;
    }

    /**
     * 从 Token 中获取指定自定义声明的值
     *
     * <p>使用示例：
     * <pre>
     * String nickname = jwtUtil.getClaim(token, "nickname", String.class);
     * Boolean vip = jwtUtil.getClaim(token, "vip", Boolean.class);
     * </pre>
     *
     * @param token Token 字符串
     * @param claimName 声明名称
     * @param clazz 期望的类型
     * @return 声明值，如果不存在或类型不匹配则返回 null
     */
    public <T> T getClaim(String token, String claimName, Class<T> clazz) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt == null) {
            return null;
        }
        var claim = jwt.getClaim(claimName);
        if (claim.isNull()) {
            return null;
        }
        try {
            if (clazz == String.class) {
                return clazz.cast(claim.asString());
            } else if (clazz == Integer.class) {
                return clazz.cast(claim.asInt());
            } else if (clazz == Long.class) {
                return clazz.cast(claim.asLong());
            } else if (clazz == Boolean.class) {
                return clazz.cast(claim.asBoolean());
            } else if (clazz == Double.class) {
                return clazz.cast(claim.asDouble());
            }
        } catch (Exception e) {
            log.warn("获取声明失败 claimName={}, expectedType={}", claimName, clazz.getSimpleName());
            return null;
        }
        return null;
    }

    /**
     * 从 Token 中提取所有自定义声明（不包括标准字段 sub/iat/exp）
     *
     * @param token Token 字符串
     * @return 自定义声明 Map，key 为声明名，value 为声明值（Object 类型）
     */
    public Map<String, Object> getAllCustomClaims(String token) {
        DecodedJWT jwt = verifyToken(token);
        if (jwt == null) {
            return new HashMap<>();
        }
        Map<String, Object> claims = new HashMap<>();
        // 获取所有声明
        jwt.getClaims().forEach((key, claim) -> {
            // 排除标准字段
            if (!"sub".equals(key) && !"iat".equals(key) && !"exp".equals(key)) {
                if (!claim.isNull()) {
                    // 根据类型提取值
                    if (claim.asString() != null) {
                        claims.put(key, claim.asString());
                    } else if (claim.asInt() != null) {
                        claims.put(key, claim.asInt());
                    } else if (claim.asLong() != null) {
                        claims.put(key, claim.asLong());
                    } else if (claim.asBoolean() != null) {
                        claims.put(key, claim.asBoolean());
                    } else if (claim.asDouble() != null) {
                        claims.put(key, claim.asDouble());
                    }
                }
            }
        });
        return claims;
    }
}

