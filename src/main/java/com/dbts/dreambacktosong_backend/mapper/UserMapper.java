package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户相关数据库操作
 */
@Mapper
public interface UserMapper {

    /** 根据主键 ID 查询用户 */
    User findById(@Param("id") Long id);

    /** 根据手机号查询用户 */
    User findByPhone(@Param("phone") String phone);

    /** 根据一组 ID 批量查询用户 */
    java.util.List<User> findByIds(@Param("ids") java.util.List<Long> ids);

    /** 新增用户 */
    int insert(User user);

    /** 更新用户信息 */
    int update(User user);
}

