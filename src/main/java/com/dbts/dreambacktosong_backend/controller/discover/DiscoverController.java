package com.dbts.dreambacktosong_backend.controller.discover;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.common.PageResponse;
import com.dbts.dreambacktosong_backend.service.DiscoverService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 发现 / 内容流接口
 *
 * <p>对应前端文档 7.x：
 * 7.1 GET /discover/carousel
 * 7.2 GET /discover/topics
 * 7.3 GET /discover/feed
 */
@Slf4j
@RestController
@RequestMapping("/discover")
@RequiredArgsConstructor
public class DiscoverController {

    private final DiscoverService discoverService;

    /** 顶部轮播 */
    @GetMapping("/carousel")
    public ApiResponse<List<Map<String, Object>>> getCarousel() {
        return ApiResponse.success(discoverService.getCarousel());
    }

    /** 话题列表 */
    @GetMapping("/topics")
    public ApiResponse<List<Map<String, Object>>> getTopics() {
        return ApiResponse.success(discoverService.getTopics());
    }

    /** 内容流（热门 / 关注） */
    @GetMapping("/feed")
    public ApiResponse<PageResponse<Map<String, Object>>> getFeed(
            @RequestParam String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(discoverService.getFeed(type, keyword, page, pageSize));
    }
}

