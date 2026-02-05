package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.Logistics;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 物流信息相关数据库操作
 */
@Mapper
public interface LogisticsMapper {

    Logistics findByOrderId(@Param("orderId") Long orderId);

    int insert(Logistics logistics);

    int update(Logistics logistics);
}

