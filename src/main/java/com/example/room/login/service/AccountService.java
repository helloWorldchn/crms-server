package com.example.room.login.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.room.environment.entity.dto.AccountQuery;
import com.example.room.login.entity.Account;

import java.util.Map;

/**
 * 用户表 服务类
 */
public interface AccountService extends IService<Account>{

    String login(Account account);

    Account getUserByUserName(String username);

    Map<String, Object> info(Integer userId);

    Account getUserInfoById(Integer userId);

    Page<Account> pageQuery(AccountQuery accountQuery);

    boolean updatePassword(Integer id, String encodedPassword);
}
