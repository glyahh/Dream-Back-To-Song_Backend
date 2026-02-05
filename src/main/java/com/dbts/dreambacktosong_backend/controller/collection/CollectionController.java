package com.dbts.dreambacktosong_backend.controller.collection;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.service.CollectionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 收藏模块接口
 *
 * <p>对应前端文档 8.x：
 * 8.1 GET    /collections
 * 8.2 POST   /collections
 * 8.3 DELETE /collections/{id}
 * 8.4 GET    /collections/check
 */
@Slf4j
@RestController
@RequestMapping("/collections")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    @GetMapping
    public ApiResponse<Map<String, Object>> list(@RequestParam String type) {
        List<Map<String, Object>> list = collectionService.list(type);
        return ApiResponse.success(Map.of("list", list));
    }

    @PostMapping
    public ApiResponse<Map<String, String>> add(@RequestBody AddRequest request) {
        String id = collectionService.add(request.getType(), request.getTargetId(), request.getExtra());
        return ApiResponse.success(Map.of("id", id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        collectionService.remove(id);
        return ApiResponse.success();
    }

    @GetMapping("/check")
    public ApiResponse<Map<String, Object>> check(@RequestParam String type,
                                                  @RequestParam String targetId) {
        return ApiResponse.success(collectionService.check(type, targetId));
    }

    @Data
    public static class AddRequest {
        private String type;
        private String targetId;
        /** JSON 字符串，内容收藏时可存 title/excerpt/from 等 */
        private String extra;
    }
}

