package com.dbts.dreambacktosong_backend.controller.auth;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.config.JwtProperties;
import com.dbts.dreambacktosong_backend.domain.entity.User;
import com.dbts.dreambacktosong_backend.service.AuthService;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 认证模块接口
 *
 * <p>对应前端文档：
 * 2.1 发送短信验证码 /auth/send-code
 * 2.2 验证码登录 /auth/login
 * 2.3 退出登录 /auth/logout
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtProperties jwtProperties;

    /**
     * 发送短信验证码
     */
    @PostMapping("/send-code")
    public ApiResponse<Void> sendCode(@RequestBody SendCodeRequest request) {
        if (request == null || request.getPhone() == null || request.getPhone().length() != 11) {
            throw new BizException(400, "请输入正确的手机号");
        }
        authService.sendLoginCode(request.getPhone());
        return ApiResponse.success("验证码已发送", null);
    }

    /**
     * 验证码登录
     */
    @PostMapping("/login")
    public ApiResponse<LoginResponse> login(@RequestBody LoginRequest request) {
        if (request == null || request.getPhone() == null || request.getCode() == null) {
            throw new BizException(400, "手机号和验证码不能为空");
        }
        AuthService.LoginResult result = authService.login(request.getPhone(), request.getCode());
        User user = result.user();
        LoginResponse resp = new LoginResponse();
        resp.setToken(result.token());
        resp.setExpiresIn(jwtProperties.getExpiresIn());
        resp.setUser(Map.of(
                "id", user.getId(),
                "phone", user.getPhone(),
                "nickname", user.getNickname(),
                "avatar", user.getAvatar(),
                "bio", user.getBio(),
                "createdAt", user.getCreatedAt()
        ));
        return ApiResponse.success("登录成功", resp);
    }

    /**
     * 退出登录
     *
     * <p>采用 JWT 无状态方案，服务端只要前端丢弃 Token 即可，
     * 此接口主要用于前端语义上的“退出”动作，直接返回成功。
     */
    @PostMapping("/logout")
    public ApiResponse<Void> logout() {
        return ApiResponse.success("已退出", null);
    }

    /** 发送验证码请求体 */
    @Data
    public static class SendCodeRequest {
        @NotBlank(message = "手机号不能为空")
        private String phone;
    }

    /** 登录请求体 */
    @Data
    public static class LoginRequest {
        @NotBlank(message = "手机号不能为空")
        private String phone;

        @NotBlank(message = "验证码不能为空")
        private String code;
    }

    /** 登录响应体 */
    @Data
    public static class LoginResponse {
        private String token;
        private long expiresIn;
        private Map<String, Object> user;
    }
}

