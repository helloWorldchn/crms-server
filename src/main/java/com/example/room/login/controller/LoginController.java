package com.example.room.login.controller;

import com.example.room.login.entity.Account;
import com.example.room.login.entity.dto.LoginParam;
import com.example.room.login.service.AccountService;
import com.example.room.util.JwtUtil;
import com.example.room.util.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户表 控制类
 */
@RestController
@Slf4j
@RequestMapping("/index")
public class LoginController {

    @Resource
    private AccountService accountService;

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginParam param) {
        // 1. 参数校验
        if (StringUtils.isEmpty(param.getUsername()) || StringUtils.isEmpty(param.getPassword())) {
            return Result.fail("用户名或密码不能为空");
        }
        Account account = accountService.getUserByUserName(param.getUsername());
        if (account == null) {
            return Result.fail("用户不存在");
        }
        if (!account.getPassword().equals(param.getPassword())) {
            return Result.fail("密码错误");
        }
        String token = accountService.login(account);
        Map<String, Object> map = new HashMap<>() ;
        map.put("token", token);
        return Result.ok(map);
    }

    @GetMapping("info")
    public Result<Map<String, Object>> info(HttpServletRequest request){
        //获取当前登录用户用户名
        Integer userId = JwtUtil.getAccountIdByJwtToken(request);
        Map<String, Object> map = accountService.info(userId);
        return Result.ok(map);
    }
    @PostMapping("logout")
    public Result<String> logout(){
        return Result.ok();
    }

}
