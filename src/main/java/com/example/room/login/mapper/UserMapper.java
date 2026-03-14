package com.example.room.login.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.room.login.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户表 Mapper 接口
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
