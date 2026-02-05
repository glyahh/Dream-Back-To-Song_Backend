package com.dbts.dreambacktosong_backend.controller.creation;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.domain.entity.Creation;
import com.dbts.dreambacktosong_backend.service.CreationService;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 我的创作模块接口
 *
 * <p>对应前端文档 9.x：
 * 9.1 GET    /creations
 * 9.2 POST   /creations
 * 9.3 GET    /creations/{id}
 * 9.4 DELETE /creations/{id}
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/creations")
@RequiredArgsConstructor
public class CreationController {

    private final CreationService creationService;

    @GetMapping
    public ApiResponse<Object> list() {
        List<Creation> list = creationService.listMyCreations();
        return ApiResponse.success(
                java.util.Map.of("list", list)
        );
    }

    @PostMapping
    public ApiResponse<Creation> create(@RequestBody CreateRequest request) {
        Creation c = creationService.create(request.getTitle(), request.getType(), request.getContent());
        return ApiResponse.success(c);
    }

    @GetMapping("/{id}")
    public ApiResponse<Creation> detail(@PathVariable("id") Long id) {
        return ApiResponse.success(creationService.getDetail(id));
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable("id") Long id) {
        creationService.delete(id);
        return ApiResponse.success();
    }

    @Data
    public static class CreateRequest {
        @NotBlank(message = "标题不能为空")
        private String title;
        @NotBlank(message = "类型不能为空")
        private String type;
        @NotBlank(message = "内容不能为空")
        private String content;
    }
}

