package com.dbts.dreambacktosong_backend.controller.market;

import com.dbts.dreambacktosong_backend.common.ApiResponse;
import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.common.PageResponse;
import com.dbts.dreambacktosong_backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 商品 / 集市模块接口
 *
 * <p>对应前端文档：
 * 4.1 GET /products           获取商品列表
 * 4.2 GET /products/{id}      获取商品详情
 * 4.3 GET /products/featured  获取热门/轮播商品
 */
@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    /**
     * 获取商品列表
     */
    @GetMapping
    public ApiResponse<PageResponse<Map<String, Object>>> listProducts(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize) {

        PageResponse<Map<String, Object>> pageData = productService.getProductPage(type, keyword, page, pageSize);
        return ApiResponse.success(pageData);
    }

    /**
     * 获取商品详情
     */
    @GetMapping("/{id}")
    public ApiResponse<Map<String, Object>> getProduct(@PathVariable("id") Long id) {
        Map<String, Object> detail = productService.getProductDetail(id);
        if (detail == null) {
            throw new BizException(404, "商品不存在");
        }
        return ApiResponse.success(detail);
    }

    /**
     * 获取热门/轮播商品
     */
    @GetMapping("/featured")
    public ApiResponse<List<Map<String, Object>>> getFeatured(@RequestParam(required = false) String type) {
        List<Map<String, Object>> list = productService.getFeatured(type);
        return ApiResponse.success(list);
    }
}

