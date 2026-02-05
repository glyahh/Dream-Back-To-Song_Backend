package com.dbts.dreambacktosong_backend.service;

import com.dbts.dreambacktosong_backend.common.PageResponse;
import com.dbts.dreambacktosong_backend.domain.entity.Product;
import com.dbts.dreambacktosong_backend.domain.entity.ProductImage;
import com.dbts.dreambacktosong_backend.domain.entity.ProductStyle;
import com.dbts.dreambacktosong_backend.mapper.ProductImageMapper;
import com.dbts.dreambacktosong_backend.mapper.ProductMapper;
import com.dbts.dreambacktosong_backend.mapper.ProductStyleMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 商品相关业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final ProductImageMapper productImageMapper;
    private final ProductStyleMapper productStyleMapper;

    /**
     * 分页获取商品列表
     */
    public PageResponse<Map<String, Object>> getProductPage(String type, String keyword, int page, int pageSize) {
        int pageNo = Math.max(page, 1);
        int size = pageSize <= 0 ? 10 : pageSize;
        int offset = (pageNo - 1) * size;

        List<Product> products = productMapper.findPage(type, keyword, offset, size);
        long total = productMapper.count(type, keyword);
        boolean hasMore = (long) pageNo * size < total;

        List<Map<String, Object>> list = products.stream()
                .map(this::toListItem)
                .collect(Collectors.toList());

        return new PageResponse<>(list, total, hasMore);
    }

    /**
     * 获取商品详情
     */
    public Map<String, Object> getProductDetail(Long id) {
        Product product = productMapper.findById(id);
        if (product == null) {
            return null;
        }
        List<ProductImage> images = productImageMapper.findByProductId(id);
        List<ProductStyle> styles = productStyleMapper.findByProductId(id);
        Map<String, Object> map = new HashMap<>();
        map.put("id", product.getId());
        map.put("name", product.getName());
        map.put("subtitle", product.getSubtitle());
        map.put("desc", product.getDescription());
        map.put("price", product.getPrice());
        map.put("originPrice", product.getOriginPrice());
        map.put("images", images.stream().map(ProductImage::getUrl).toList());
        map.put("styles", styles.stream()
                .map(s -> {
                    Map<String, Object> styleMap = new HashMap<>();
                    styleMap.put("id", s.getId());
                    styleMap.put("name", s.getName());
                    styleMap.put("thumb", s.getThumb());
                    return styleMap;
                })
                .toList());
        map.put("tagLabels", List.of()); // 可根据 product_tags 或其他规则补充
        map.put("sales", product.getSalesText());
        map.put("shop", product.getShopName());
        map.put("shippingFrom", product.getShippingFrom());
        map.put("service", product.getService());
        return map;
    }

    /**
     * 获取热门或轮播商品
     */
    public List<Map<String, Object>> getFeatured(String type) {
        List<Product> products = productMapper.findFeatured(type);
        if ("carousel".equals(type)) {
            // 轮播图结构：{ image, title, link }
            return products.stream()
                    .map(p -> {
                        Map<String, Object> map = new HashMap<>();
                        map.put("image", p.getMainImg());
                        map.put("title", p.getName());
                        map.put("link", "");
                        return map;
                    })
                    .toList();
        }
        // 热门商品使用与列表相同结构
        return products.stream()
                .map(this::toListItem)
                .toList();
    }

    /**
     * 列表元素映射为前端需要的字段
     */
    private Map<String, Object> toListItem(Product p) {
        List<ProductImage> images = productImageMapper.findByProductId(p.getId());
        List<ProductStyle> styles = productStyleMapper.findByProductId(p.getId());
        Map<String, Object> map = new HashMap<>();
        map.put("id", p.getId());
        map.put("name", p.getName());
        map.put("desc", p.getDescription());
        map.put("img", p.getMainImg());
        map.put("images", images.stream().map(ProductImage::getUrl).toList());
        map.put("price", p.getPrice());
        map.put("originPrice", p.getOriginPrice());
        map.put("tag", p.getTag());
        map.put("tags", List.of()); // 如需标签，可结合 product_tags 扩展
        map.put("sales", p.getSalesText());
        map.put("shop", p.getShopName());
        map.put("styles", styles.stream()
                .map(s -> {
                    Map<String, Object> styleMap = new HashMap<>();
                    styleMap.put("id", s.getId());
                    styleMap.put("name", s.getName());
                    styleMap.put("thumb", s.getThumb());
                    return styleMap;
                })
                .toList());
        return map;
    }
}

