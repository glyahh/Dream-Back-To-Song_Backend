package com.dbts.dreambacktosong_backend.controller.follow;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.service.FollowService;
import com.dbts.dreambacktosong_backend.service.MessageService;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 我的关注 / 私信接口
 *
 * <p>对应前端文档 11.x：
 * 11.1 GET  /following
 * 11.2 POST /following/{userId}/message
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/following")
@RequiredArgsConstructor
public class FollowingController {

    private final FollowService followService;
    private final MessageService messageService;

    @GetMapping
    public ApiResponse<Map<String, Object>> list() {
        List<Map<String, Object>> list = followService.listFollowing();
        return ApiResponse.success(Map.of("list", list));
    }

    @PostMapping("/{userId}/message")
    public ApiResponse<Void> sendMessage(@PathVariable("userId") Long userId,
                                         @RequestBody SendMessageRequest request) {
        messageService.sendPrivateMessage(userId, request.getContent());
        return ApiResponse.success();
    }

    @Data
    public static class SendMessageRequest {
        @NotBlank(message = "内容不能为空")
        private String content;
    }
}

