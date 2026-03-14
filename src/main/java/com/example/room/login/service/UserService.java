package com.example.room.login.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.room.login.entity.User;

import java.util.Map;

/**
 * 用户表 服务类
 */
public interface UserService extends IService<User>{

    String login(User user);

    User getUserByUserName(String username);

    Map<String, Object> info(Integer userId);

    User getUserInfoById(Integer userId);
}
