package com.dbts.dreambacktosong_backend.controller.message;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.common.PageResponse;
import com.dbts.dreambacktosong_backend.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 消息通知模块接口
 *
 * <p>对应前端文档 12.x：
 * 12.1 GET  /messages
 * 12.2 POST /messages/{id}/read
 * 12.3 GET  /messages/unread-count
 */
@Slf4j
@RestController
@RequestMapping("/messages")
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @GetMapping
    public ApiResponse<PageResponse<Map<String, Object>>> list(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {
        return ApiResponse.success(messageService.listMessages(category, page, pageSize));
    }

    @PostMapping("/{id}/read")
    public ApiResponse<Void> markRead(@PathVariable("id") Long id) {
        messageService.markRead(id);
        return ApiResponse.success();
    }

    @GetMapping("/unread-count")
    public ApiResponse<Map<String, Object>> unreadCount() {
        return ApiResponse.success(messageService.unreadCount());
    }
}

