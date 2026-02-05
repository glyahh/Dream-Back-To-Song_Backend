package com.dbts.dreambacktosong_backend.service;

import com.dbts.dreambacktosong_backend.domain.entity.Feedback;
import com.dbts.dreambacktosong_backend.mapper.FeedbackMapper;
import com.dbts.dreambacktosong_backend.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 意见反馈业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackMapper feedbackMapper;

    public void submit(Integer rate, String tagsJson, String content, String contact) {
        Long userId = UserContext.getUserId();
        Feedback f = new Feedback();
        f.setUserId(userId);
        f.setRate(rate);
        f.setTags(tagsJson);
        f.setContent(content);
        f.setContact(contact);
        feedbackMapper.insert(f);
    }

    public List<Map<String, Object>> history() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            return List.of();
        }
        List<Feedback> list = feedbackMapper.findByUserId(userId);
        return list.stream()
                .map(f -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", "f_" + f.getId());
                    map.put("time", f.getCreatedAt());
                    map.put("content", f.getContent());
                    return map;
                })
                .toList();
    }
}

