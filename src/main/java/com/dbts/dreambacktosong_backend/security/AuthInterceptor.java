package com.dbts.dreambacktosong_backend.security;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 登录鉴权拦截器
 *
 * <p>从请求头中解析 Bearer Token，校验通过后在 UserContext 中保存当前用户信息。
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String path = request.getRequestURI();
        // 只要进入到这里的路径，都是需要鉴权的（具体哪些路径放行在 WebMvcConfig 中配置）
        // 拿到含有Jwt令牌的请求头
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            writeUnauthorized(response, "未登录或 token 缺失");
            return false;
        }
        // 去掉请求头中的Bearer前缀, 获取真正的纯token
        String token = authHeader.substring("Bearer ".length());

        // DecodedJWT -> 已解码验证的 JWT 令牌对象
        DecodedJWT jwt = jwtUtil.verifyToken(token);
        if (jwt == null) {
            writeUnauthorized(response, "token 无效或已过期");
            return false;
        }

        // 保存当前用户信息到 ThreadLocal
        // sub 字段通常用来标识当前令牌对应的唯一主体
        Long userId = Long.valueOf(jwt.getSubject());
        String phone = jwt.getClaim("phone").asString();
        UserContext.setUser(new UserContext.UserInfo(userId, phone));
        log.debug("Jwt图灵测试通过~~~，用户已登录, path={}, userId={}", path, userId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 清理 ThreadLocal，避免内存泄漏
        UserContext.clear();
    }

    private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setContentType("application/json;charset=UTF-8");
        ApiResponse<Void> body = ApiResponse.error(401, message);
        response.getWriter().write(objectMapper.writeValueAsString(body));
    }
}

