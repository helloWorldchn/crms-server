package com.example.room.environment.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.room.environment.entity.DeviceOption;
import com.example.room.environment.entity.dto.DeviceOptionControl;
import com.example.room.environment.entity.dto.DeviceOptionQuery;
import com.example.room.environment.entity.enums.DeviceCommandEnum;
import com.example.room.environment.service.DeviceOptionService;
import com.example.room.mqtt.common.MqttSendMessageService;
import com.example.room.util.JwtUtil;
import com.example.room.util.RequestIdGenerator;
import com.example.room.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 环境数据前端控制器
 * </p>
 *
 * @author helloWorld
 * @since 2023-05-31
 */
@Api(description = "反控操作记录数据管理")
@RestController
@RequestMapping("/service/deviceOption")
//@CrossOrigin // 解决跨域问题
public class DeviceOptionController {
    // 把service注入
    @Resource
    private DeviceOptionService deviceOptionService;
    @Resource
    private MqttSendMessageService mqttSendMessageService;
    @ApiOperation(value = "所有反控操作记录数据列表")
    @GetMapping("findAll")
    public Result<List<DeviceOption>> findAll() {
        List<DeviceOption> list = deviceOptionService.list();
        return Result.ok(list);
    }

    // 2.逻辑删除讲师方法
    @ApiOperation(value = "逻辑删除")
    @DeleteMapping("{id}")
    public Result<String> removeDeviceOption(@ApiParam(name = "id", value = "环境数据id", required = true) @PathVariable String id) {
        boolean flag = deviceOptionService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 4.添加查询带分页的方法
    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("pageDeviceOptionCondition/{current}/{limit}")
    public Result<Page<DeviceOption>> pageDeviceOptionCondition(@PathVariable Long current, @PathVariable Long limit,
                                                        @RequestBody(required = false) DeviceOptionQuery deviceOptionQuery) {
        // 调用方法，实现分页查询
        Page<DeviceOption> resultPage = deviceOptionService.pageQuery(current, limit, deviceOptionQuery);
        return Result.ok(resultPage);
    }

    @ApiOperation("根据ID查询反控操作记录数据")
    @GetMapping("getDeviceOption/{id}")
    public Result<DeviceOption> getDeviceOption(@PathVariable String id) {
        DeviceOption byId = deviceOptionService.getById(id);
        return Result.ok(byId);
    }

    @ApiOperation("修改反控操作记录数据")
    @PostMapping("updateDeviceOption")
    public Result<String> updateDeviceOption(@RequestBody DeviceOption deviceOption) {
        boolean b = deviceOptionService.updateById(deviceOption);
        if (b) {
            return Result.ok();
        } else
            return Result.fail("修改失败");
    }


    @ApiOperation("反控操作")
    @PostMapping("control")
    public Result<String> controlDevice(@RequestBody DeviceOptionControl deviceOption, HttpServletRequest request) {
        String token = JwtUtil.getTokenFromRequest(request);
        Integer userIdFromToken = JwtUtil.getAccountIdFromToken(token);

        boolean b = deviceOptionService.controlDevice(deviceOption, String.valueOf(userIdFromToken));
        if (b) {
            return Result.ok();
        } else
            return Result.fail("操作失败，请稍后重试");
    }

}
