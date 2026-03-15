package com.example.room.login.controller;

import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.room.environment.entity.Environment;
import com.example.room.environment.entity.dto.AccountQuery;
import com.example.room.environment.entity.dto.EnvironmentQuery;
import com.example.room.login.entity.Account;
import com.example.room.login.service.AccountService;
import com.example.room.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 用户表 控制类
 */
@RestController
@Slf4j
@RequestMapping("/account")
public class AccountController {

    @Resource
    private AccountService accountService;

    /**
     * 获取用户列表
     */
    @GetMapping("/list")
    public Result<List<Account>> list() {
        return Result.ok(accountService.list());
    }

    /**
     * 根据用户id获取用户
     */
    @GetMapping("/getAccount/{id}")
    public Result<Account> getAccount(@PathVariable Integer id) {
        return Result.ok(accountService.getById(id));
    }
    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("pageAccountCondition")
    public Result<Page<Account>> pageAccountCondition(@RequestBody(required = false) AccountQuery accountQuery) {
        // 调用方法，实现分页查询
        Page<Account> resultPage = accountService.pageQuery(accountQuery);
        return Result.ok(resultPage);
    }

    /**
     * 新增用户
     */
    @PostMapping("/addAccount")
    public Result<Boolean> addAccount(@RequestBody Account account) {
        account.setGmtCreated(new Date());
        account.setGmtModified(new Date());
        return Result.ok(accountService.save(account));
    }

    /**
     * 修改用户
     */
    @PostMapping("/updateAccount")
    public Result<Boolean> updateAccount(@RequestBody Account account) {
        account.setGmtModified(new Date());
        boolean b = accountService.updateById(account);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail("修改失败");
        }
    }


    /**
     * 根据用户id删除用户
     */
    /*
    @GetMapping("/delete")
    public Result<Boolean> delete(@RequestParam Integer id) {
        return Result.ok(accountService.removeById(id));
    }
    */
    @ApiOperation(value = "逻辑删除")
    @DeleteMapping("{id}")
    public Result<String> removeAccount(@ApiParam(name = "id", value = "数据id", required = true) @PathVariable String id) {
        boolean flag = accountService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }
    @PostMapping("/updatePassword")
    public Result<String> updatePassword(@RequestBody Map<String, Object> params) {
        Integer id = (Integer) params.get("id");
        String oldPassword = (String) params.get("oldPassword");
        String newPassword = (String) params.get("newPassword");
        Account account = accountService.getUserInfoById(id);
        if (account == null || !Objects.equals(account.getPassword(), oldPassword)) {
            return Result.fail("旧密码输入错误！");
        }
        // 密码加密（推荐 BCrypt）
        // String encodedPassword = new BCryptPasswordEncoder().encode(newPassword);
        // 调用 service 更新
        boolean b = accountService.updatePassword(id, newPassword);
        if (b) {
            return Result.ok();
        } else {
            return Result.fail("修改失败");
        }
    }
}
