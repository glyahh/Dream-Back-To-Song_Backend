package com.dbts.dreambacktosong_backend.service;

import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.domain.entity.Post;
import com.dbts.dreambacktosong_backend.domain.entity.PostLike;
import com.dbts.dreambacktosong_backend.mapper.PostLikeMapper;
import com.dbts.dreambacktosong_backend.mapper.PostMapper;
import com.dbts.dreambacktosong_backend.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 发布 / 我的发布 / 点赞 业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostMapper postMapper;
    private final PostLikeMapper postLikeMapper;

    public List<Map<String, Object>> listMyPosts() {
        Long userId = requireLogin();
        List<Post> list = postMapper.findByUserId(userId);
        return list.stream()
                .map(p -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", "p_" + p.getId());
                    map.put("content", p.getContent());
                    map.put("time", p.getCreatedAt());
                    map.put("likes", p.getLikesCount());
                    map.put("liked", false);
                    map.put("topic", p.getTopic());
                    map.put("createdAt", p.getCreatedAt());
                    return map;
                })
                .toList();
    }

    public Post create(String content, String topic) {
        Long userId = requireLogin();
        Post p = new Post();
        p.setUserId(userId);
        p.setContent(content);
        p.setTopic(topic);
        postMapper.insert(p);
        return p;
    }

    public void delete(Long id) {
        Long userId = requireLogin();
        int rows = postMapper.deleteByIdAndUserId(id, userId);
        if (rows == 0) {
            throw new BizException(404, "发布不存在");
        }
    }

    public Map<String, Object> likeOrUnlike(Long postId, String action) {
        Long userId = requireLogin();
        PostLike exist = postLikeMapper.find(postId, userId);
        boolean liked;
        int likes;
        if ("like".equals(action)) {
            if (exist == null) {
                PostLike like = new PostLike();
                like.setPostId(postId);
                like.setUserId(userId);
                postLikeMapper.insert(like);
                postMapper.updateLikesCount(postId, 1);
            }
            liked = true;
        } else {
            if (exist != null) {
                postLikeMapper.delete(postId, userId);
                postMapper.updateLikesCount(postId, -1);
            }
            liked = false;
        }
        Post post = postMapper.findById(postId);
        likes = post != null ? post.getLikesCount() : 0;
        Map<String, Object> map = new HashMap<>();
        map.put("likes", likes);
        map.put("liked", liked);
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

