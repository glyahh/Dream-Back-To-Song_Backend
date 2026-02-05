package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.Order;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单主表相关数据库操作
 */
@Mapper
public interface OrderMapper {

    int insert(Order order);

    Order findById(@Param("id") Long id);

    Order findByOrderNo(@Param("orderNo") String orderNo);

    List<Order> findPageByStatus(@Param("userId") Long userId,
                                 @Param("status") String status,
                                 @Param("offset") int offset,
                                 @Param("limit") int limit);

    long countByStatus(@Param("userId") Long userId,
                       @Param("status") String status);

    int updateStatus(@Param("id") Long id,
                     @Param("status") String status);

    int updatePayInfo(@Param("id") Long id,
                      @Param("payType") String payType,
                      @Param("status") String status);
}

