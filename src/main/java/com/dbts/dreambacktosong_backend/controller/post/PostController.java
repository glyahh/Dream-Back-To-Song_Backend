package com.dbts.dreambacktosong_backend.controller.post;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.service.PostService;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 发布 / 我的发布 / 点赞接口
 *
 * <p>对应前端文档 10.x：
 * 10.1 GET    /posts/my
 * 10.2 POST   /posts
 * 10.3 DELETE /posts/{id}
 * 10.4 POST   /posts/{id}/like
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/my")
    public ApiResponse<Map<String, Object>> listMy() {
        List<Map<String, Object>> list = postService.listMyPosts();
        return ApiResponse.success(Map.of("list", list));
    }

    @PostMapping
    public ApiResponse<Map<String, Object>> create(@RequestBody CreateRequest request) {
        var p = postService.create(request.getContent(), request.getTopic());
        Map<String, Object> data = Map.of(
                "id", "p_" + p.getId(),
                "content", p.getContent(),
                "topic", p.getTopic(),
                "createdAt", p.getCreatedAt()
        );
        return ApiResponse.success(data);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        postService.delete(id);
        return ApiResponse.success();
    }

    @PostMapping("/{id}/like")
    public ApiResponse<Map<String, Object>> like(@PathVariable("id") Long id,
                                                 @RequestBody LikeRequest request) {
        Map<String, Object> data = postService.likeOrUnlike(id, request.getAction());
        return ApiResponse.success(data);
    }

    @Data
    public static class CreateRequest {
        @NotBlank(message = "内容不能为空")
        private String content;
        private String topic;
        private java.util.List<String> images;
    }

    @Data
    public static class LikeRequest {
        @NotBlank(message = "action 不能为空")
        private String action;
    }
}

