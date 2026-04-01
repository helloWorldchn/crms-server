package com.example.room.environment.controller;

import cn.hutool.core.bean.BeanUtil;
import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.room.environment.entity.Environment;
import com.example.room.environment.entity.dto.EnvironmentExportVO;
import com.example.room.environment.entity.dto.EnvironmentQuery;
import com.example.room.environment.entity.dto.EnvironmentStatisticsQuery;
import com.example.room.environment.entity.dto.EnvironmentVO;
import com.example.room.environment.service.EnvironmentService;
import com.example.room.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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
    public Result<String> removeEnvironment(@ApiParam(name = "id", value = "环境数据id", required = true) @PathVariable Long id) {
        boolean flag = environmentService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 4.添加查询带分页的方法
    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("pageEnvironmentCondition")
    public Result<Page<EnvironmentVO>> pageEnvironmentCondition(@RequestBody(required = false) EnvironmentQuery environmentQuery) {
        // 调用方法，实现分页查询
        Page<Environment> environmentPage = environmentService.pageQuery(environmentQuery);
        List<Environment> environmentList = environmentPage.getRecords();
        List<EnvironmentVO> voList = BeanUtil.copyToList(environmentList, EnvironmentVO.class);
        // 创建新的 Page 对象，保留分页信息
        Page<EnvironmentVO> voPage = new Page<>(environmentPage.getCurrent(), environmentPage.getSize(), environmentPage.getTotal());
        voPage.setRecords(voList);
        // 使用 map 转换，保留分页信息
        // Page<EnvironmentVO> voPage = environmentPage.convert(entity -> {
        //     return BeanUtil.copyToList(entity, EnvironmentVO.class);
        // });
        return Result.ok(voPage);
    }

    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("statistics")
    public Result<List<Environment>> getStatistics(@RequestBody(required = false) EnvironmentStatisticsQuery query) {
        List<Environment> resultList = environmentService.getStatistics(query);
        return Result.ok(resultList);
    }

    @ApiOperation(value = "获取最新一条数据")
    @GetMapping("getLastEnvironment")
    public Result<Environment> getLastEnvironment(@RequestParam("deviceKey") String deviceKey) {
        Environment environment = environmentService.getLastData(deviceKey);
        return Result.ok(environment);
    }

    /**
     * 导出环境数据到Excel
     */
    @ApiOperation("导出环境监测数据")
    @PostMapping("export")
    public void export(@RequestBody(required = false) EnvironmentQuery environmentQuery,
                       HttpServletResponse response) {
        try {
            // 1. 查询所有符合条件的数据（不分页）
            environmentQuery.setCurrentPage(1);
            environmentQuery.setPageSize(Integer.MAX_VALUE);

            // 查询数据
            Page<Environment> pageResult = environmentService.pageQuery(environmentQuery);
            List<Environment> list = pageResult.getRecords();
            List<EnvironmentExportVO> exportVOList = BeanUtil.copyToList(list, EnvironmentExportVO.class);
            AtomicInteger index = new AtomicInteger(1);
            exportVOList.forEach(vo -> {
                vo.setIndex(index.getAndIncrement());
            });
            // 2. 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("环境监测数据", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 3. 使用 EasyExcel 写出
            EasyExcel.write(response.getOutputStream(), EnvironmentExportVO.class)
                    .sheet("环境数据")
                    .doWrite(exportVOList);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    public Result<Environment> getEnvironment(@PathVariable Long id) {
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
