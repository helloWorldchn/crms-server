package com.example.room.login.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.room.environment.entity.dto.AccountQuery;
import com.example.room.login.entity.Account;
import com.example.room.login.mapper.AccountMapper;
import com.example.room.login.service.AccountService;
import com.example.room.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

    @Override
    public Account getUserByUserName(String username) {
        // 2. 查询用户
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Account::getUsername, username);
        return this.getOne(queryWrapper);
    }

    @Override
    public String login(Account account) {
        // 生成JWT token
        return JwtUtil.generateToken(account.getId(), account.getUsername());
    }

    @Override
    public Map<String, Object> info(Integer userId) {
        Account account = this.getUserInfoById(userId);
        Map<String, Object> map = new HashMap<>();
        map.put("roles", account.getRole());
        map.put("name", account.getUsername());
        map.put("avatar", account.getAvatar());
        return map;
    }

    @Override
    public Account getUserInfoById(Integer userId) {
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Account::getId, userId);
        return this.getOne(queryWrapper);
    }

    @Override
    public Page<Account> pageQuery(AccountQuery accountQuery) {
        // 创建page
        Page<Account> page = new Page<>(accountQuery.getCurrentPage(), accountQuery.getPageSize());
        // 构建条件
        QueryWrapper<Account> queryWrapper = new QueryWrapper<>();
        // MyBatis的动态sql
        String begin = accountQuery.getBegin();
        String end = accountQuery.getEnd();
        if (!StringUtils.isEmpty(accountQuery.getUsername())) {
            queryWrapper.lambda().eq(Account::getUsername, accountQuery.getUsername());
        }
        if (!StringUtils.isEmpty(accountQuery.getRole())) {
            queryWrapper.lambda().eq(Account::getRole, accountQuery.getRole());
        }
        if (!StringUtils.isEmpty(accountQuery.getNickname())) {
            queryWrapper.lambda().like(Account::getNickname, accountQuery.getNickname());
        }

        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.lambda().ge(Account::getGmtCreated, begin);
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.lambda().le(Account::getGmtCreated, end);
        }
        // 排序
        queryWrapper.lambda().orderByAsc(Account::getGmtCreated);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public boolean updatePassword(Integer id, String encodedPassword) {

        UpdateWrapper<Account> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().eq(Account::getId, id).set(Account::getPassword, encodedPassword);
        return this.update(updateWrapper);
    }
}
