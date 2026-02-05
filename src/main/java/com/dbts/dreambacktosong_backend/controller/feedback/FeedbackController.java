package com.dbts.dreambacktosong_backend.controller.feedback;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.service.FeedbackService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 意见反馈模块接口
 *
 * <p>对应前端文档 13.x：
 * 13.1 POST /feedback
 * 13.2 GET  /feedback/history
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService feedbackService;

    @PostMapping
    public ApiResponse<Void> submit(@RequestBody SubmitRequest request) {
        // tags 以 JSON 字符串形式存储，调用方可以直接传 JSON.stringify 后的结果
        String tagsJson = request.getTags() != null
                ? new com.fasterxml.jackson.databind.ObjectMapper().valueToTree(request.getTags()).toString()
                : null;
        feedbackService.submit(request.getRate(), tagsJson, request.getContent(), request.getContact());
        return ApiResponse.success();
    }

    @GetMapping("/history")
    public ApiResponse<Map<String, Object>> history() {
        List<Map<String, Object>> list = feedbackService.history();
        return ApiResponse.success(Map.of("list", list));
    }

    @Data
    public static class SubmitRequest {
        @Min(1)
        @Max(5)
        private Integer rate;
        private List<String> tags;
        @NotBlank(message = "反馈内容不能为空")
        private String content;
        private String contact;
    }
}

