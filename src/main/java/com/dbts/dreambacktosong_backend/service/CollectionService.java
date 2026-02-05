package com.dbts.dreambacktosong_backend.service;

import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.domain.entity.Collection;
import com.dbts.dreambacktosong_backend.mapper.CollectionMapper;
import com.dbts.dreambacktosong_backend.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 收藏业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CollectionService {

    private final CollectionMapper collectionMapper;

    public List<Map<String, Object>> list(String type) {
        Long userId = requireLogin();
        List<Collection> list = collectionMapper.findByUserAndType(userId, type);
        return list.stream()
                .map(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", "c_" + c.getId());
                    map.put("type", c.getType());
                    map.put("targetId", c.getTargetId());
                    map.put("extra", c.getExtra());
                    map.put("time", c.getCreatedAt());
                    return map;
                })
                .toList();
    }

    public String add(String type, String targetId, String extraJson) {
        Long userId = requireLogin();
        Collection exist = collectionMapper.findOne(userId, type, targetId);
        if (exist != null) {
            return "c_" + exist.getId();
        }
        Collection c = new Collection();
        c.setUserId(userId);
        c.setType(type);
        c.setTargetId(targetId);
        c.setExtra(extraJson);
        collectionMapper.insert(c);
        return "c_" + c.getId();
    }

    public boolean remove(Long id) {
        Long userId = requireLogin();
        int rows = collectionMapper.deleteByIdAndUserId(id, userId);
        return rows > 0;
    }

    public Map<String, Object> check(String type, String targetId) {
        Long userId = requireLogin();
        Collection c = collectionMapper.findOne(userId, type, targetId);
        boolean collected = c != null;
        String cid = c != null ? "c_" + c.getId() : null;
        Map<String, Object> map = new HashMap<>();
        map.put("collected", collected);
        map.put("collectionId", cid);
        return map;
    }

    private Long requireLogin() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BizException(401, "未登录");
        }
        return userId;
    }
}

