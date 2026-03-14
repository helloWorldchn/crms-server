package com.example.room.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.room.login.entity.User;
import com.example.room.login.mapper.UserMapper;
import com.example.room.login.service.UserService;
import com.example.room.util.JwtUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public User getUserByUserName(String username) {
        // 2. 查询用户
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getUsername, username);
        return this.getOne(queryWrapper);
    }

    @Override
    public String login(User user) {
        // 生成JWT token
        return JwtUtil.generateToken(user.getId(), user.getUsername());
    }

    @Override
    public Map<String, Object> info(Integer userId) {
        User user = this.getUserInfoById(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("roles", "[admin]");
        map.put("name", user.getUsername());
        map.put("avatar", user.getAvatar());
        return map;
    }

    @Override
    public User getUserInfoById(Integer userId) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getId, userId);
        return this.getOne(queryWrapper);
    }
}
