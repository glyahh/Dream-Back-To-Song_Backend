package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.UserSettings;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户设置相关数据库操作
 */
@Mapper
public interface UserSettingsMapper {

    /** 根据用户 ID 查询设置 */
    UserSettings findByUserId(@Param("userId") Long userId);

    /** 新增用户设置 */
    int insert(UserSettings settings);

    /** 更新用户设置 */
    int update(UserSettings settings);
}

