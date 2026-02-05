package com.dbts.dreambacktosong_backend.service;

import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.common.PageResponse;
import com.dbts.dreambacktosong_backend.domain.entity.Carousel;
import com.dbts.dreambacktosong_backend.domain.entity.Post;
import com.dbts.dreambacktosong_backend.domain.entity.PostImage;
import com.dbts.dreambacktosong_backend.domain.entity.Topic;
import com.dbts.dreambacktosong_backend.domain.entity.User;
import com.dbts.dreambacktosong_backend.domain.entity.UserFollow;
import com.dbts.dreambacktosong_backend.mapper.*;
import com.dbts.dreambacktosong_backend.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 发现 / 内容流相关业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DiscoverService {

    private final CarouselMapper carouselMapper;
    private final TopicMapper topicMapper;
    private final PostMapper postMapper;
    private final PostImageMapper postImageMapper;
    private final UserFollowMapper userFollowMapper;
    private final UserMapper userMapper;

    /**
     * 获取轮播图
     */
    public List<Map<String, Object>> getCarousel() {
        List<Carousel> list = carouselMapper.findAll();
        return list.stream()
                .map(c -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("image", c.getImage());
                    map.put("title", c.getTitle());
                    map.put("link", c.getLink());
                    return map;
                })
                .toList();
    }

    /**
     * 获取话题列表
     */
    public List<Map<String, Object>> getTopics() {
        List<Topic> list = topicMapper.findAll();
        return list.stream()
                .map(t -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("key", t.getKey());
                    map.put("title", t.getTitle());
                    map.put("image", t.getImage());
                    map.put("link", t.getLink());
                    return map;
                })
                .toList();
    }

    /**
     * 获取内容流（热门 / 关注）
     */
    public PageResponse<Map<String, Object>> getFeed(String type, String keyword, int page, int pageSize) {
        int pageNo = Math.max(page, 1);
        int size = pageSize <= 0 ? 10 : pageSize;
        int offset = (pageNo - 1) * size;
        List<Post> posts;
        long total;

        if ("follow".equals(type)) {
            Long userId = UserContext.getUserId();
            if (userId == null) {
                throw new BizException(401, "未登录");
            }
            List<UserFollow> follows = userFollowMapper.findByFollower(userId);
            List<Long> userIds = follows.stream().map(UserFollow::getFollowingId).toList();
            if (userIds.isEmpty()) {
                return new PageResponse<>(List.of(), 0, false);
            }
            posts = postMapper.findByUserIds(userIds, offset, size);
            // total 简化为当前查询结果数量，hasMore 直接根据数量判断
            total = posts.size();
        } else {
            posts = postMapper.findHot(keyword, offset, size);
            // 热门流这里 total 简化为当前页数量，hasMore 不做精确计算
            total = posts.size();
        }

        // 批量查询用户与图片
        List<Long> userIds = posts.stream().map(Post::getUserId).distinct().toList();
        Map<Long, User> userMap = userIds.isEmpty()
                ? new HashMap<>()
                : userMapper.findByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        List<Map<String, Object>> list = new ArrayList<>();
        for (Post p : posts) {
            List<PostImage> images = postImageMapper.findByPostId(p.getId());
            User author = userMap.get(p.getUserId());
            Map<String, Object> map = new HashMap<>();
            map.put("id", "post_" + p.getId());
            map.put("username", author != null ? author.getNickname() : "");
            map.put("avatar", author != null ? author.getAvatar() : "");
            map.put("time", ""); // 前端可根据 createdAt 自行格式化
            map.put("text", p.getContent());
            map.put("images", images.stream().map(PostImage::getUrl).toList());
            map.put("followed", false);
            list.add(map);
        }
        boolean hasMore = list.size() == size;
        return new PageResponse<>(list, total, hasMore);
    }
}

