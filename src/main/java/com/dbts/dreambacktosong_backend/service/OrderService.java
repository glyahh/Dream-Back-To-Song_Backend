package com.dbts.dreambacktosong_backend.service;

import com.dbts.dreambacktosong_backend.common.BizException;
import com.dbts.dreambacktosong_backend.common.PageResponse;
import com.dbts.dreambacktosong_backend.domain.entity.Address;
import com.dbts.dreambacktosong_backend.domain.entity.Logistics;
import com.dbts.dreambacktosong_backend.domain.entity.Order;
import com.dbts.dreambacktosong_backend.domain.entity.OrderItem;
import com.dbts.dreambacktosong_backend.domain.entity.Product;
import com.dbts.dreambacktosong_backend.mapper.*;
import com.dbts.dreambacktosong_backend.security.UserContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 订单相关业务逻辑
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final AddressMapper addressMapper;
    private final LogisticsMapper logisticsMapper;
    private final ProductMapper productMapper;

    /** 用于将商品快照对象转成 JSON 字符串 */
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 创建订单
     */
    @Transactional
    public Map<String, Object> createOrder(CreateOrderParam param) {
        Long userId = requireLogin();
        if (param.items() == null || param.items().isEmpty()) {
            throw new BizException(400, "订单商品不能为空");
        }
        Address address = addressMapper.findById(param.addressId());
        if (address == null || !Objects.equals(address.getUserId(), userId)) {
            throw new BizException(400, "收货地址无效");
        }

        // 查询商品信息并计算金额
        List<Long> productIds = param.items().stream().map(ItemParam::productId).distinct().toList();
        List<Product> products = productMapper.findByIds(productIds);
        Map<Long, Product> productMap = new HashMap<>();
        for (Product p : products) {
            productMap.put(p.getId(), p);
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();
        for (ItemParam item : param.items()) {
            Product p = productMap.get(item.productId());
            if (p == null) {
                throw new BizException(400, "商品不存在或已下架");
            }
            BigDecimal price = Optional.ofNullable(p.getPrice()).orElse(BigDecimal.ZERO);
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(item.qty()));
            totalAmount = totalAmount.add(subtotal);

            Map<String, Object> snapshot = new HashMap<>();
            snapshot.put("id", p.getId());
            snapshot.put("name", p.getName());
            snapshot.put("img", p.getMainImg());
            snapshot.put("price", price);
            OrderItem oi = new OrderItem();
            oi.setProductId(p.getId());
            oi.setStyleId(item.styleId());
            oi.setQty(item.qty());
            oi.setPrice(price);
            oi.setSubtotal(subtotal);
            // 使用 ObjectMapper 将快照 Map 转为 JSON 字符串
            oi.setProductSnapshot(OBJECT_MAPPER.valueToTree(snapshot).toString());
            orderItems.add(oi);
        }

        // 创建订单主记录
        Order order = new Order();
        order.setOrderNo(generateOrderNo(userId));
        order.setUserId(userId);
        order.setAddressId(address.getId());
        order.setTotalAmount(totalAmount);
        order.setStatus("pending");
        order.setRemark(param.remark());
        orderMapper.insert(order);

        // 填充 orderId 并批量写入明细
        for (OrderItem oi : orderItems) {
            oi.setOrderId(order.getId());
        }
        orderItemMapper.insertBatch(orderItems);

        Map<String, Object> result = new HashMap<>();
        result.put("orderId", "order_" + order.getId());
        result.put("amount", totalAmount);
        result.put("status", order.getStatus());
        result.put("payUrl", "");
        return result;
    }

    /**
     * 获取订单列表
     */
    public PageResponse<Map<String, Object>> getOrderPage(String status, int page, int pageSize) {
        Long userId = requireLogin();
        int pageNo = Math.max(page, 1);
        int size = pageSize <= 0 ? 10 : pageSize;
        int offset = (pageNo - 1) * size;

        List<Order> orders = orderMapper.findPageByStatus(userId, status, offset, size);
        long total = orderMapper.countByStatus(userId, status);
        boolean hasMore = (long) pageNo * size < total;

        List<Map<String, Object>> list = new ArrayList<>();
        for (Order o : orders) {
            List<OrderItem> items = orderItemMapper.findByOrderId(o.getId());
            String title = "";
            String desc = "";
            String img = "";
            int count = 0;
            if (!items.isEmpty()) {
                OrderItem first = items.get(0);
                title = "[商品]" + first.getProductId();
                count = items.stream().mapToInt(OrderItem::getQty).sum();
            }
            LocalDateTime createdAt = o.getCreatedAt();
            String timeText = createdAt != null ? "下单时间：" + createdAt : "";
            Map<String, Object> map = new HashMap<>();
            map.put("id", "order_" + o.getId());
            map.put("title", title);
            map.put("desc", desc);
            map.put("img", img);
            map.put("time", timeText);
            map.put("status", o.getStatus());
            map.put("count", count);
            map.put("price", o.getTotalAmount());
            map.put("shopName", "");
            list.add(map);
        }
        return new PageResponse<>(list, total, hasMore);
    }

    /**
     * 获取订单详情
     */
    public Map<String, Object> getOrderDetail(Long id) {
        Long userId = requireLogin();
        Order order = orderMapper.findById(id);
        if (order == null || !Objects.equals(order.getUserId(), userId)) {
            throw new BizException(404, "订单不存在");
        }
        List<OrderItem> items = orderItemMapper.findByOrderId(order.getId());
        Address address = order.getAddressId() != null ? addressMapper.findById(order.getAddressId()) : null;
        Logistics logistics = logisticsMapper.findByOrderId(order.getId());

        List<Map<String, Object>> itemViews = new ArrayList<>();
        for (OrderItem item : items) {
            Map<String, Object> map = new HashMap<>();
            map.put("productId", item.getProductId());
            map.put("styleId", item.getStyleId());
            map.put("qty", item.getQty());
            map.put("price", item.getPrice());
            map.put("subtotal", item.getSubtotal());
            map.put("snapshot", item.getProductSnapshot());
            itemViews.add(map);
        }

        Map<String, Object> addr = null;
        if (address != null) {
            addr = new HashMap<>();
            addr.put("name", address.getName());
            addr.put("phone", address.getPhone());
            addr.put("full", address.getProvince() + address.getCity() +
                    Optional.ofNullable(address.getDistrict()).orElse("") +
                    address.getDetail());
        }

        Map<String, Object> logi = null;
        if (logistics != null) {
            logi = new HashMap<>();
            logi.put("company", logistics.getCompany());
            logi.put("trackingNo", logistics.getTrackingNo());
            logi.put("traces", logistics.getTraces());
        }

        Map<String, Object> result = new HashMap<>();
        result.put("id", "order_" + order.getId());
        result.put("orderNo", order.getOrderNo());
        result.put("status", order.getStatus());
        result.put("amount", order.getTotalAmount());
        result.put("items", itemViews);
        result.put("address", addr);
        result.put("logistics", logi);
        result.put("remark", order.getRemark());
        result.put("createdAt", order.getCreatedAt());
        return result;
    }

    /**
     * 取消订单
     */
    public void cancelOrder(Long id) {
        updateStatusWithCheck(id, "pending", "cancelled", "仅待付款订单可取消");
    }

    /**
     * 支付订单（简化版）
     */
    public void payOrder(Long id, String payType) {
        Long userId = requireLogin();
        Order order = orderMapper.findById(id);
        if (order == null || !Objects.equals(order.getUserId(), userId)) {
            throw new BizException(404, "订单不存在");
        }
        if (!"pending".equals(order.getStatus())) {
            throw new BizException(400, "订单当前状态不可支付");
        }
        orderMapper.updatePayInfo(id, payType, "paid");
    }

    /**
     * 确认收货
     */
    public void confirmOrder(Long id) {
        updateStatusWithCheck(id, "shipped", "finished", "仅待收货订单可确认收货");
    }

    /**
     * 获取物流信息
     */
    public Map<String, Object> getLogistics(Long id) {
        Long userId = requireLogin();
        Order order = orderMapper.findById(id);
        if (order == null || !Objects.equals(order.getUserId(), userId)) {
            throw new BizException(404, "订单不存在");
        }
        Logistics logistics = logisticsMapper.findByOrderId(order.getId());
        if (logistics == null) {
            Map<String, Object> empty = new HashMap<>();
            empty.put("company", "");
            empty.put("trackingNo", "");
            empty.put("traces", List.of());
            return empty;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("company", logistics.getCompany());
        map.put("trackingNo", logistics.getTrackingNo());
        map.put("traces", logistics.getTraces());
        return map;
    }

    private void updateStatusWithCheck(Long id, String requiredStatus, String newStatus, String errorMsg) {
        Long userId = requireLogin();
        Order order = orderMapper.findById(id);
        if (order == null || !Objects.equals(order.getUserId(), userId)) {
            throw new BizException(404, "订单不存在");
        }
        if (!requiredStatus.equals(order.getStatus())) {
            throw new BizException(400, errorMsg);
        }
        orderMapper.updateStatus(id, newStatus);
    }

    private Long requireLogin() {
        Long userId = UserContext.getUserId();
        if (userId == null) {
            throw new BizException(401, "未登录");
        }
        return userId;
    }

    private String generateOrderNo(Long userId) {
        return "O" + userId + System.currentTimeMillis();
    }

    /**
     * 创建订单参数
     */
    public record CreateOrderParam(List<ItemParam> items, Long addressId, String remark) {
    }

    /**
     * 订单项参数
     */
    public record ItemParam(Long productId, Long styleId, int qty) {
    }
}

