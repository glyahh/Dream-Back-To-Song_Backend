package com.dbts.dreambacktosong_backend.service;

import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.domain.entity.User;
import com.dbts.dreambacktosong_backend.domain.entity.UserSettings;
import com.dbts.dreambacktosong_backend.mapper.UserMapper;
import com.dbts.dreambacktosong_backend.mapper.UserSettingsMapper;
import com.dbts.dreambacktosong_backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 认证与登录相关业务
 *
 * <p>说明：
 * 1. 短信验证码这里采用内存 Map 简单存储（phone -> code, expiresAt），避免引入额外表；
 * 2. 实际生产环境建议改为 Redis 或独立表，并接入真实短信服务。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    /**
     * 短信验证码缓存（仅用于开发/测试）
     * key: phone, value: { code, expiresAt(秒) }
     */
    private final Map<String, CodeRecord> codeStore = new ConcurrentHashMap<>();

    private final UserMapper userMapper;
    private final UserSettingsMapper userSettingsMapper;
    private final JwtUtil jwtUtil;

    /**
     * 发送登录验证码
     */
    public void sendLoginCode(String phone) {
        // 简单 6 位数字验证码
        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
        long expiresAt = Instant.now().getEpochSecond() + 5 * 60;
        codeStore.put(phone, new CodeRecord(code, expiresAt));
        // 实际应调用短信服务商 SDK，这里仅打印日志方便开发联调
        log.info("发送登录验证码 phone={}, code={}", phone, code);
    }

    /**
     * 使用手机号 + 验证码登录，返回 token 与用户信息
     */
    public LoginResult login(String phone, String code) {
        CodeRecord record = codeStore.get(phone);
        long now = Instant.now().getEpochSecond();
        if (record == null || record.expiresAt() < now || !record.code().equals(code)) {
            throw new BizException(400, "验证码错误或已过期");
        }
        // 验证通过后可删除验证码
        codeStore.remove(phone);

        // 查询或创建用户
        User user = userMapper.findByPhone(phone);
        if (user == null) {
            user = new User();
            user.setPhone(phone);
            user.setNickname(maskPhoneAsNickname(phone));
            userMapper.insert(user);
            // 初始化用户设置
            UserSettings settings = new UserSettings();
            settings.setUserId(user.getId());
            settings.setNotify(true);
            settings.setSound(true);
            settings.setDataSaver(false);
            userSettingsMapper.insert(settings);
        }

        // 签发 JWT
        // 方式1：基础用法（仅包含 userId 和 phone）
        String token = jwtUtil.generateToken(user.getId(), user.getPhone());
        
        // 方式2：添加自定义声明（Private Claims）示例：
        // Map<String, Object> customClaims = new HashMap<>();
        // customClaims.put("nickname", user.getNickname());
        // customClaims.put("avatar", user.getAvatar());
        // customClaims.put("role", "user");
        // String token = jwtUtil.generateToken(user.getId(), user.getPhone(), customClaims);
        
        return new LoginResult(token, user);
    }

    /**
     * 将手机号简单脱敏作为默认昵称
     */
    private String maskPhoneAsNickname(String phone) {
        if (phone == null || phone.length() != 11) {
            return "宋人_" + System.currentTimeMillis();
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }

    /**
     * 登录结果载体
     */
    public record LoginResult(String token, User user) {
    }

    /**
     * 验证码记录
     *
     * @param code      6 位验证码
     * @param expiresAt 过期时间戳（秒）
     */
    private record CodeRecord(String code, long expiresAt) {
    }
}

