package com.dbts.dreambacktosong_backend.service;

import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.common.PageResponse;
import com.dbts.dreambacktosong_backend.domain.entity.Message;
import com.dbts.dreambacktosong_backend.domain.entity.PrivateMessage;
import com.dbts.dreambacktosong_backend.mapper.MessageMapper;
import com.dbts.dreambacktosong_backend.mapper.PrivateMessageMapper;
import com.dbts.dreambacktosong_backend.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息与私信业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageMapper messageMapper;
    private final PrivateMessageMapper privateMessageMapper;

    public PageResponse<Map<String, Object>> listMessages(String category, int page, int pageSize) {
        Long userId = requireLogin();
        int pageNo = Math.max(page, 1);
        int size = pageSize <= 0 ? 10 : pageSize;
        int offset = (pageNo - 1) * size;
        List<Message> list = messageMapper.findPage(userId, category, offset, size);
        long total = messageMapper.count(userId, category);
        boolean hasMore = (long) pageNo * size < total;
        List<Map<String, Object>> data = list.stream()
                .map(m -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", "m_" + m.getId());
                    map.put("category", m.getCategory());
                    map.put("title", m.getTitle());
                    map.put("preview", m.getPreview());
                    map.put("content", m.getContent());
                    map.put("time", m.getCreatedAt());
                    map.put("read", m.getIsRead());
                    return map;
                })
                .toList();
        return new PageResponse<>(data, total, hasMore);
    }

    public void markRead(Long id) {
        Long userId = requireLogin();
        messageMapper.markRead(id, userId);
    }

    public Map<String, Object> unreadCount() {
        Long userId = requireLogin();
        long system = messageMapper.countUnreadByCategory(userId, "system");
        long social = messageMapper.countUnreadByCategory(userId, "social");
        long order = messageMapper.countUnreadByCategory(userId, "order");
        long total = system + social + order;
        Map<String, Object> map = new HashMap<>();
        map.put("system", system);
        map.put("social", social);
        map.put("order", order);
        map.put("total", total);
        return map;
    }

    public void sendPrivateMessage(Long targetUserId, String content) {
        Long userId = requireLogin();
        if (userId.equals(targetUserId)) {
            throw new BizException(400, "不能给自己发送私信");
        }
        PrivateMessage pm = new PrivateMessage();
        pm.setSenderId(userId);
        pm.setReceiverId(targetUserId);
        pm.setContent(content);
        pm.setIsRead(false);
        privateMessageMapper.insert(pm);
    }

    private Long requireLogin() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BizException(401, "未登录");
        }
        return userId;
    }
}

