package com.example.room.environment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.room.environment.entity.Environment;
import com.example.room.environment.entity.dto.EnvironmentQuery;
import com.example.room.environment.entity.dto.EnvironmentStatisticsQuery;
import com.example.room.environment.service.EnvironmentService;
import com.example.room.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 环境数据 前端控制器
 * </p>
 *
 * @author helloWorld
 * @since 2023-05-31
 */
@Api(description = "环境监测数据管理")
@RestController
@RequestMapping("/service/environment")
//@CrossOrigin // 解决跨域问题
public class EnvironmentController {
    // 把service注入
    @Resource
    private EnvironmentService environmentService;

    //1. 查询环境所有数据
    // restful风格
    // 访问地址  http://localhost:8001/service/environment/findAll
    @ApiOperation(value = "所有环境监测数据列表")
    @GetMapping("findAll")
    public Result<List<Environment>> findAll() {
        // 调用service的方法实现查询所有的操作
        List<Environment> list = environmentService.list();
        return Result.ok(list);
    }

    // 2.逻辑删除讲师方法
    @ApiOperation(value = "逻辑删除")
    @DeleteMapping("{id}")
    public Result<String> removeEnvironment(@ApiParam(name = "id", value = "环境数据id", required = true) @PathVariable String id) {
        boolean flag = environmentService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 4.添加查询带分页的方法
    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("pageEnvironmentCondition/{current}/{limit}")
    public Result<Page<Environment>> pageEnvironmentCondition(@PathVariable Long current, @PathVariable Long limit,
                                                        @RequestBody(required = false) EnvironmentQuery environmentQuery) {
        // 调用方法，实现分页查询
        Page<Environment> resultPage = environmentService.pageQuery(current, limit, environmentQuery);
        return Result.ok(resultPage);
    }

    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("statistics")
    public Result<List<Environment>> getStatistics(@RequestBody(required = false) EnvironmentStatisticsQuery query) {
        List<Environment> resultList = environmentService.getStatistics(query);
        return Result.ok(resultList);
    }

    @ApiOperation(value = "获取最新一条数据")
    @GetMapping("getLastEnvironment")
    public Result<Environment> getLastEnvironment() {
        Environment environment = environmentService.getLastData();
        return Result.ok(environment);
    }

    @ApiOperation("添加环境监测数据")
    @PostMapping("addEnvironment")
    public Result<String> addEnvironment(@RequestBody Environment environment) {
        environment.setGmtCreate(new Date());
        boolean save = environmentService.save(environment);
        if (save) {
            return Result.ok();
        } else
            return Result.fail();
    }

    @ApiOperation("根据ID查询环境监测数据")
    @GetMapping("getEnvironment/{id}")
    public Result<Environment> getEnvironment(@PathVariable String id) {
        Environment byId = environmentService.getById(id);
        return Result.ok(byId);
    }

    @ApiOperation("修改环境监测数据")
    @PostMapping("updateEnvironment")
    public Result<String> updateEnvironment(@RequestBody Environment environment) {
        boolean b = environmentService.updateById(environment);
        if (b) {
            return Result.ok();
        } else
            return Result.fail("修改失败");
    }

}
