package com.dbts.dreambacktosong_backend.service;

import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.domain.entity.User;
import com.dbts.dreambacktosong_backend.domain.entity.UserFollow;
import com.dbts.dreambacktosong_backend.mapper.UserFollowMapper;
import com.dbts.dreambacktosong_backend.mapper.UserMapper;
import com.dbts.dreambacktosong_backend.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 关注 / 私信业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FollowService {

    private final UserFollowMapper userFollowMapper;
    private final UserMapper userMapper;

    public void followOrUnfollow(Long targetUserId, String action) {
        Long userId = requireLogin();
        if (userId.equals(targetUserId)) {
            throw new BizException(400, "不能关注自己");
        }
        UserFollow exist = userFollowMapper.find(userId, targetUserId);
        if ("follow".equals(action)) {
            if (exist == null) {
                UserFollow f = new UserFollow();
                f.setFollowerId(userId);
                f.setFollowingId(targetUserId);
                userFollowMapper.insert(f);
            }
        } else {
            if (exist != null) {
                userFollowMapper.delete(userId, targetUserId);
            }
        }
    }

    public List<Map<String, Object>> listFollowing() {
        Long userId = requireLogin();
        List<UserFollow> list = userFollowMapper.findByFollower(userId);
        List<Long> ids = list.stream().map(UserFollow::getFollowingId).toList();
        if (ids.isEmpty()) {
            return List.of();
        }
        List<User> users = userMapper.findByIds(ids);
        var map = users.stream().collect(Collectors.toMap(User::getId, u -> u));
        return list.stream()
                .map(f -> {
                    User u = map.get(f.getFollowingId());
                    Map<String, Object> result = new HashMap<>();
                    result.put("id", u != null ? "user_" + u.getId() : "user_" + f.getFollowingId());
                    result.put("name", u != null ? u.getNickname() : "");
                    result.put("avatar", u != null ? u.getAvatar() : "");
                    result.put("desc", "");
                    result.put("meta", "");
                    result.put("followed", true);
                    return result;
                })
                .toList();
    }

    private Long requireLogin() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BizException(401, "未登录");
        }
        return userId;
    }
}

