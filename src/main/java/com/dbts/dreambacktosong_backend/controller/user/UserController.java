package com.dbts.dreambacktosong_backend.controller.user;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.domain.entity.User;
import com.dbts.dreambacktosong_backend.domain.entity.UserSettings;
import com.dbts.dreambacktosong_backend.service.UserService;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.Map;

/**
 * 用户模块接口
 *
 * <p>对应前端文档：
 * 3.1 获取当前用户资料 GET /user/profile
 * 3.2 更新用户资料 PUT /user/profile
 * 3.3 上传头像 POST /user/upload-avatar
 * 3.4 同步用户设置 PUT /user/settings
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 获取当前用户资料
     */
    @GetMapping("/profile")
    public ApiResponse<Map<String, Object>> getProfile() {
        User user = userService.getCurrentUserProfile();
        return ApiResponse.success(toProfileMap(user));
    }

    /**
     * 更新用户资料
     */
    @PutMapping("/profile")
    public ApiResponse<Map<String, Object>> updateProfile(@RequestBody UpdateProfileRequest request) {
        User updated = userService.updateProfile(request.getNickname(), request.getAvatar(), request.getBio());
        return ApiResponse.success(toProfileMap(updated));
    }

    /**
     * 上传头像
     *
     * <p>说明：
     * - 这里为简化示例，暂未接入真实文件存储服务；
     * - 可以根据实际情况把文件上传到云存储（OSS、COS）后返回 URL；
     * - 当前实现：仅返回一个占位 URL，并打印日志。
     */
    @PostMapping("/upload-avatar")
    public ApiResponse<Map<String, String>> uploadAvatar(@RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ApiResponse.error(400, "上传文件不能为空");
        }
        String filename = file.getOriginalFilename();
        log.info("收到头像上传请求，文件名={}，大小={} 字节", filename, file.getSize());
        // 占位：实际应上传到对象存储并返回可访问地址
        String url = "https://cdn.example.com/avatars/" + System.currentTimeMillis() + "_" + filename;
        return ApiResponse.success(Map.of("url", url));
    }

    /**
     * 同步用户设置
     */
    @PutMapping("/settings")
    public ApiResponse<Void> updateSettings(@RequestBody UpdateSettingsRequest request) {
        userService.updateSettings(request.getNotify(), request.getSound(), request.getDataSaver());
        return ApiResponse.success();
    }

    private Map<String, Object> toProfileMap(User user) {
        LocalDateTime createdAt = user.getCreatedAt();
        LocalDateTime updatedAt = user.getUpdatedAt();
        return Map.of(
                "id", user.getId(),
                "phone", user.getPhone(),
                "nickname", user.getNickname(),
                "avatar", user.getAvatar(),
                "bio", user.getBio(),
                "createdAt", createdAt,
                "updatedAt", updatedAt
        );
    }

    /** 更新资料请求体 */
    @Data
    public static class UpdateProfileRequest {
        @Size(max = 16, message = "昵称最多 16 个字符")
        private String nickname;

        private String avatar;

        @Size(max = 40, message = "签名最多 40 个字符")
        private String bio;
    }

    /** 更新设置请求体 */
    @Data
    public static class UpdateSettingsRequest {
        private Boolean notify;
        private Boolean sound;
        private Boolean dataSaver;
    }
}

