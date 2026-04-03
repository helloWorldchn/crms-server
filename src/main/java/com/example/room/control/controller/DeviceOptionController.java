package com.example.room.control.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.room.control.entity.DeviceOption;
import com.example.room.control.entity.param.DeviceOptionControl;
import com.example.room.control.entity.param.DeviceOptionQuery;
import com.example.room.control.entity.vo.DeviceOptionVo;
import com.example.room.control.service.DeviceOptionService;
import com.example.room.mqtt.common.MqttSendMessageService;
import com.example.room.util.JwtUtil;
import com.example.room.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
public class DeviceOptionController {

    @Resource
    private DeviceOptionService deviceOptionService;

    @ApiOperation(value = "所有反控操作记录数据列表")
    @GetMapping("findAll")
    public Result<List<DeviceOption>> findAll() {
        List<DeviceOption> list = deviceOptionService.list();
        return Result.ok(list);
    }

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

    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("pageDeviceOptionCondition")
    public Result<Page<DeviceOptionVo>> pageDeviceOptionCondition(@RequestBody DeviceOptionQuery deviceOptionQuery) {
        // 调用方法，实现分页查询
        Page<DeviceOptionVo> resultPage = deviceOptionService.pageQuery(deviceOptionQuery);
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
        // String token = JwtUtil.getTokenFromRequest(request);
        // Long userIdFromToken = JwtUtil.getAccountIdFromToken(token);
        // String username = JwtUtil.getAccountCodeFromToken(token);

        String username =  SecurityContextHolder.getContext().getAuthentication().getName();
        boolean b = deviceOptionService.controlDevice(deviceOption, username);
        if (b) {
            return Result.ok("指令已下达");
        } else
            return Result.fail("操作失败，请稍后重试");
    }

}
