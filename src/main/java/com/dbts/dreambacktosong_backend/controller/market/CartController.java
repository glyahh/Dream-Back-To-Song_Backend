package com.dbts.dreambacktosong_backend.controller.market;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.service.CartService;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 购物车模块接口
 *
 * <p>对应前端文档：
 * 5.1 GET  /cart       获取购物车
 * 5.2 POST /cart/items 更新单个购物车项
 * 5.3 PUT  /cart       批量更新购物车
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    /**
     * 获取购物车
     */
    @GetMapping
    public ApiResponse<Map<String, Object>> getCart() {
        return ApiResponse.success(cartService.getCurrentCart());
    }

    /**
     * 更新单个购物车项
     */
    @PostMapping("/items")
    public ApiResponse<Map<String, Object>> updateItem(@RequestBody UpdateItemRequest request) {
        Long productId = Long.valueOf(request.getProductId());
        Long styleId = request.getStyleId();
        Map<String, Object> data = cartService.updateCartItem(productId, styleId, request.getQty());
        return ApiResponse.success(data);
    }

    /**
     * 批量更新购物车
     */
    @PutMapping
    public ApiResponse<Map<String, Object>> batchUpdate(@RequestBody BatchUpdateRequest request) {
        List<CartService.ItemUpdate> list = request.getItems().stream()
                .map(i -> new CartService.ItemUpdate(
                        Long.valueOf(i.getProductId()),
                        i.getStyleId(),
                        i.getQty()
                ))
                .toList();
        Map<String, Object> data = cartService.batchUpdate(list);
        return ApiResponse.success(data);
    }

    /** 单项更新请求体 */
    @Data
    public static class UpdateItemRequest {
        private String productId;
        private Long styleId;
        @Min(value = 0, message = "数量不能小于 0")
        private int qty;
    }

    /** 批量更新请求体 */
    @Data
    public static class BatchUpdateRequest {
        private List<UpdateItemRequest> items;
    }
}

