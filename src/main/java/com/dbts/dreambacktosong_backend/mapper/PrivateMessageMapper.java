package com.dbts.dreambacktosong_backend.mapper;

import com.dbts.dreambacktosong_backend.domain.entity.PrivateMessage;
import org.apache.ibatis.annotations.Mapper;

/**
 * 私信相关数据库操作
 */
@Mapper
public interface PrivateMessageMapper {

    int insert(PrivateMessage message);
}

