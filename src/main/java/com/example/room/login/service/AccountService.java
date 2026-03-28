package com.example.room.login.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.room.login.entity.dto.AccountQuery;
import com.example.room.login.entity.Account;

import java.util.Map;

/**
 * 用户表 服务类
 */
public interface AccountService extends IService<Account>{

    String login(Account account);

    Account getUserByUserName(String username);

    Map<String, Object> info(Long userId);

    Account getUserInfoById(Long userId);

    Page<Account> pageQuery(AccountQuery accountQuery);

    boolean updatePassword(Long id, String encodedPassword);
}
