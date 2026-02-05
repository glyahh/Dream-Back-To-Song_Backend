package com.dbts.dreambacktosong_backend.controller.order;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.common.PageResponse;
import com.dbts.dreambacktosong_backend.service.OrderService;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 订单模块接口
 *
 * <p>对应前端文档 6.x：
 * 6.1 POST   /orders              创建订单
 * 6.2 GET    /orders              获取订单列表
 * 6.3 GET    /orders/{id}         获取订单详情
 * 6.4 POST   /orders/{id}/cancel  取消订单
 * 6.5 POST   /orders/{id}/pay     支付订单（简化）
 * 6.6 POST   /orders/{id}/confirm 确认收货
 * 6.7 GET    /orders/{id}/logistics 获取物流信息
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    /**
     * 创建订单
     */
    @PostMapping
    public ApiResponse<Map<String, Object>> createOrder(@RequestBody CreateOrderRequest request) {
        OrderService.CreateOrderParam param = new OrderService.CreateOrderParam(
                request.getItems().stream()
                        .map(i -> new OrderService.ItemParam(
                                Long.valueOf(i.getProductId()),
                                i.getStyleId(),
                                i.getQty()
                        ))
                        .toList(),
                request.getAddressId(),
                request.getRemark()
        );
        Map<String, Object> data = orderService.createOrder(param);
        return ApiResponse.success(data);
    }

    /**
     * 获取订单列表
     */
    @GetMapping
    public ApiResponse<PageResponse<Map<String, Object>>> listOrders(
            @RequestParam(required = false, defaultValue = "all") String status,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int pageSize) {
        PageResponse<Map<String, Object>> data = orderService.getOrderPage(status, page, pageSize);
        return ApiResponse.success(data);
    }

    /**
     * 获取订单详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> getOrder(@PathVariable("id") Long id) {
        return ApiResponse.success(orderService.getOrderDetail(id));
    }

    /**
     * 取消订单
     */
    @PostMapping("/{id}/cancel")
    public ApiResponse<Void> cancelOrder(@PathVariable("id") Long id) {
        orderService.cancelOrder(id);
        return ApiResponse.success();
    }

    /**
     * 支付订单（简化）
     */
    @PostMapping("/{id}/pay")
    public ApiResponse<Void> payOrder(@PathVariable("id") Long id,
                                      @RequestBody PayRequest request) {
        orderService.payOrder(id, request.getPayType());
        return ApiResponse.success();
    }

    /**
     * 确认收货
     */
    @PostMapping("/{id}/confirm")
    public ApiResponse<Void> confirmOrder(@PathVariable("id") Long id) {
        orderService.confirmOrder(id);
        return ApiResponse.success();
    }

    /**
     * 获取物流信息
     */
    @GetMapping("/{id}/logistics")
    public ApiResponse<Map<String, Object>> getLogistics(@PathVariable("id") Long id) {
        return ApiResponse.success(orderService.getLogistics(id));
    }

    /** 创建订单请求体 */
    @Data
    public static class CreateOrderRequest {
        @NotEmpty(message = "订单商品不能为空")
        private List<Item> items;
        private Long addressId;
        private String remark;

        @Data
        public static class Item {
            private String productId;
            private Long styleId;
            private int qty;
        }
    }

    /** 支付请求体 */
    @Data
    public static class PayRequest {
        private String payType;
    }
}

