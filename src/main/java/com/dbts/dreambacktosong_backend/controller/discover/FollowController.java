package com.dbts.dreambacktosong_backend.controller.discover;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.service.FollowService;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 发现模块中的关注/取关接口
 *
 * <p>对应前端文档 7.4：
 * POST /discover/follow
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/discover")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/follow")
    public ApiResponse<Void> follow(@RequestBody FollowRequest request) {
        if (request.getTargetUserId() == null) {
            throw new BizException(400, "targetUserId 不能为空");
        }
        followService.followOrUnfollow(request.getTargetUserId(), request.getAction());
        return ApiResponse.success();
    }

    @Data
    public static class FollowRequest {
        private Long targetUserId;
        @NotBlank(message = "action 不能为空")
        private String action;
    }
}

