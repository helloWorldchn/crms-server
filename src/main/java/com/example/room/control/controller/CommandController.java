package com.example.room.control.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.room.control.entity.Command;
import com.example.room.control.entity.param.CommandQuery;
import com.example.room.control.service.CommandService;
import com.example.room.util.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/service/command")
public class CommandController {

    @Resource
    private CommandService commandService;

    @ApiOperation(value = "所有指令列表")
    @GetMapping("findAll")
    public Result<List<Command>> findAll() {
        List<Command> list = commandService.list();
        return Result.ok(list);
    }

    @ApiOperation(value = "逻辑删除")
    @DeleteMapping("{id}")
    public Result<String> removeCommand(@ApiParam(name = "id", value = "指令id", required = true) @PathVariable String id) {
        boolean flag = commandService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("pageCommandCondition")
    public Result<Page<Command>> pageCommandCondition(@RequestBody CommandQuery commandQuery) {
        // 调用方法，实现分页查询
        Page<Command> resultPage = commandService.pageQuery(commandQuery);
        return Result.ok(resultPage);
    }

    @ApiOperation("根据ID查询反记录数据")
    @GetMapping("getCommand/{id}")
    public Result<Command> getCommand(@PathVariable String id) {
        Command byId = commandService.getById(id);
        return Result.ok(byId);
    }

    @ApiOperation("修改反记录数据")
    @PostMapping("updateCommand")
    public Result<String> updateCommand(@RequestBody Command command) {
        boolean b = commandService.updateById(command);
        if (b) {
            return Result.ok();
        } else
            return Result.fail("修改失败");
    }
}