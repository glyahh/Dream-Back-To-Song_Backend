package com.dbts.dreambacktosong_backend.service;

import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.domain.entity.CartItem;
import com.dbts.dreambacktosong_backend.domain.entity.Product;
import com.dbts.dreambacktosong_backend.mapper.CartItemMapper;
import com.dbts.dreambacktosong_backend.mapper.ProductMapper;
import com.dbts.dreambacktosong_backend.security.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 购物车业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemMapper cartItemMapper;
    private final ProductMapper productMapper;

    /**
     * 获取当前用户的购物车详情
     */
    public Map<String, Object> getCurrentCart() {
        Long userId = requireLogin();
        List<CartItem> items = cartItemMapper.findByUserId(userId);
        if (items.isEmpty()) {
            Map<String, Object> empty = new HashMap<>();
            empty.put("items", List.of());
            empty.put("totalCount", 0);
            empty.put("totalPrice", "0.00");
            empty.put("totalDiscount", "0.00");
            return empty;
        }
        // 批量查询商品信息
        List<Long> productIds = items.stream().map(CartItem::getProductId).distinct().toList();
        List<Product> products = productMapper.findByIds(productIds);
        Map<Long, Product> productMap = products.stream()
                .collect(Collectors.toMap(Product::getId, p -> p));

        List<Map<String, Object>> itemViews = new ArrayList<>();
        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem item : items) {
            Product p = productMap.get(item.getProductId());
            if (p == null) {
                continue;
            }
            BigDecimal price = Optional.ofNullable(p.getPrice()).orElse(BigDecimal.ZERO);
            BigDecimal originPrice = Optional.ofNullable(p.getOriginPrice()).orElse(price);
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(item.getQty()));
            totalPrice = totalPrice.add(subtotal);
            Map<String, Object> productView = new HashMap<>();
            productView.put("id", p.getId());
            productView.put("name", p.getName());
            productView.put("img", p.getMainImg());
            productView.put("price", price);
            productView.put("originPrice", originPrice);

            Map<String, Object> itemView = new HashMap<>();
            itemView.put("productId", String.valueOf(p.getId()));
            itemView.put("qty", item.getQty());
            itemView.put("product", productView);
            itemView.put("subtotal", subtotal.toString());
            itemViews.add(itemView);
        }

        // 折扣暂不计算，先返回 0
        Map<String, Object> result = new HashMap<>();
        result.put("items", itemViews);
        result.put("totalCount", itemViews.size());
        result.put("totalPrice", totalPrice.toString());
        result.put("totalDiscount", "0.00");
        return result;
    }

    /**
     * 更新单个购物车项（加购 / 减购 / 修改数量）
     */
    public Map<String, Object> updateCartItem(Long productId, Long styleId, int qty) {
        Long userId = requireLogin();
        if (qty < 0) {
            throw new BizException(400, "数量不能小于 0");
        }
        CartItem existing = cartItemMapper.findByUserAndProduct(userId, productId, styleId);
        if (qty == 0) {
            if (existing != null) {
                cartItemMapper.deleteById(existing.getId());
            }
        } else {
            if (existing == null) {
                CartItem item = new CartItem();
                item.setUserId(userId);
                item.setProductId(productId);
                item.setStyleId(styleId);
                item.setQty(qty);
                cartItemMapper.insert(item);
            } else {
                cartItemMapper.updateQty(existing.getId(), qty);
            }
        }
        return getCurrentCart();
    }

    /**
     * 批量更新购物车
     */
    public Map<String, Object> batchUpdate(List<ItemUpdate> items) {
        Long userId = requireLogin();
        for (ItemUpdate u : items) {
            if (u.qty() < 0) {
                throw new BizException(400, "数量不能小于 0");
            }
            if (u.qty() == 0) {
                cartItemMapper.deleteByUserAndProduct(userId, u.productId(), u.styleId());
            } else {
                CartItem existing = cartItemMapper.findByUserAndProduct(userId, u.productId(), u.styleId());
                if (existing == null) {
                    CartItem item = new CartItem();
                    item.setUserId(userId);
                    item.setProductId(u.productId());
                    item.setStyleId(u.styleId());
                    item.setQty(u.qty());
                    cartItemMapper.insert(item);
                } else {
                    cartItemMapper.updateQty(existing.getId(), u.qty());
                }
            }
        }
        return getCurrentCart();
    }

    private Long requireLogin() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BizException(401, "未登录");
        }
        return userId;
    }

    /**
     * 批量更新载体
     */
    public record ItemUpdate(Long productId, Long styleId, int qty) {
    }
}

