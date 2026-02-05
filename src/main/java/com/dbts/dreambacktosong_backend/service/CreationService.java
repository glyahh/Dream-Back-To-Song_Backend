package com.dbts.dreambacktosong_backend.service;

import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.domain.entity.Creation;
import com.dbts.dreambacktosong_backend.mapper.CreationMapper;
import com.dbts.dreambacktosong_backend.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 创作业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreationService {

    private final CreationMapper creationMapper;

    public List<Creation> listMyCreations() {
        Long userId = requireLogin();
        return creationMapper.findByUserId(userId);
    }

    public Creation create(String title, String type, String content) {
        Long userId = requireLogin();
        Creation c = new Creation();
        c.setUserId(userId);
        c.setTitle(title);
        c.setType(type);
        c.setContent(content);
        // 简单根据内容生成摘要与字数
        c.setWords(content != null ? content.length() : 0);
        c.setExcerpt(content != null && content.length() > 50 ? content.substring(0, 50) : content);
        creationMapper.insert(c);
        return c;
    }

    public Creation getDetail(Long id) {
        Long userId = requireLogin();
        Creation c = creationMapper.findById(id);
        if (c == null || !userId.equals(c.getUserId())) {
            throw new BizException(404, "创作不存在");
        }
        return c;
    }

    public void delete(Long id) {
        Long userId = requireLogin();
        creationMapper.deleteByIdAndUserId(id, userId);
    }

    private Long requireLogin() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BizException(401, "未登录");
        }
        return userId;
    }
}

