package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.Address;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 收货地址相关数据库操作
 */
@Mapper
public interface AddressMapper {

    Address findById(@Param("id") Long id);

    List<Address> findByUserId(@Param("userId") Long userId);

    int insert(Address address);

    int update(Address address);
}

