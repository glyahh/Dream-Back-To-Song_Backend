package com.dbts.dreambacktosong_backend.service;

import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.domain.entity.User;
import com.dbts.dreambacktosong_backend.domain.entity.UserSettings;
import com.dbts.dreambacktosong_backend.mapper.UserMapper;
import com.dbts.dreambacktosong_backend.mapper.UserSettingsMapper;
import com.dbts.dreambacktosong_backend.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 用户相关业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserMapper userMapper;
    private final UserSettingsMapper userSettingsMapper;

    /**
     * 获取当前登录用户信息
     */
    public User getCurrentUserProfile() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BizException(401, "未登录");
        }
        User user = userMapper.findById(userId);
        if (user == null) {
            throw new BizException(404, "用户不存在");
        }
        return user;
    }

    /**
     * 更新当前登录用户资料
     */
    public User updateProfile(String nickname, String avatar, String bio) {
        User user = getCurrentUserProfile();
        if (StringUtils.hasText(nickname)) {
            user.setNickname(nickname);
        }
        if (StringUtils.hasText(avatar)) {
            user.setAvatar(avatar);
        }
        if (bio != null) {
            user.setBio(bio);
        }
        userMapper.update(user);
        return userMapper.findById(user.getId());
    }

    /**
     * 获取当前用户设置
     */
    public UserSettings getCurrentSettings() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BizException(401, "未登录");
        }
        UserSettings settings = userSettingsMapper.findByUserId(userId);
        if (settings == null) {
            // 若不存在则初始化一条
            settings = new UserSettings();
            settings.setUserId(userId);
            settings.setNotify(true);
            settings.setSound(true);
            settings.setDataSaver(false);
            userSettingsMapper.insert(settings);
        }
        return settings;
    }

    /**
     * 更新当前用户设置
     */
    public void updateSettings(Boolean notify, Boolean sound, Boolean dataSaver) {
        UserSettings settings = getCurrentSettings();
        if (notify != null) {
            settings.setNotify(notify);
        }
        if (sound != null) {
            settings.setSound(sound);
        }
        if (dataSaver != null) {
            settings.setDataSaver(dataSaver);
        }
        userSettingsMapper.update(settings);
    }
}

