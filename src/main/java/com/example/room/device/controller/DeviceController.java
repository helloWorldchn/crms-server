package com.example.room.device.controller;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.room.device.entity.Device;
import com.example.room.device.entity.query.DeviceQuery;
import com.example.room.device.entity.vo.DeviceSelect;
import com.example.room.device.service.DeviceService;
import com.example.room.environment.entity.dto.EnvironmentQuery;
import com.example.room.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 设备管理前端控制器
 * </p>
 *
 * @author helloWorld
 * @since 2023-05-31
 */
@Api(description = "设备管理")
@RestController
@RequestMapping("/service/device")
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @ApiOperation(value = "所有设备列表")
    @GetMapping("findAll")
    public Result<List<Device>> findAll() {
        List<Device> list = deviceService.list();
        return Result.ok(list);
    }

    @ApiOperation(value = "分页查询设备列表")
    @PostMapping("pageDeviceCondition")
    public Result<Page<Device>> pageQuery(@RequestBody(required = false) DeviceQuery deviceQuery) {
        Page<Device> resultPage = deviceService.pageQuery(deviceQuery);
        return Result.ok(resultPage);
    }

    @ApiOperation(value = "逻辑删除")
    @DeleteMapping("{id}")
    public Result<String> removeDevice(@ApiParam(name = "id", value = "设备 id", required = true) @PathVariable String id) {
        boolean flag = deviceService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    @ApiOperation("根据 ID 查询设备")
    @GetMapping("getDevice/{id}")
    public Result<Device> getDevice(@PathVariable String id) {
        Device device = deviceService.getById(id);
        return Result.ok(device);
    }

    @ApiOperation("根据 ID 查询设备")
    @GetMapping("getDeviceSelect")
    public Result<List<DeviceSelect>> getDeviceSelect() {
        List<Device> list = deviceService.list();
        List<DeviceSelect> result = BeanUtil.copyToList(list, DeviceSelect.class);;
        return Result.ok(result);
    }

    @ApiOperation("根据 deviceKey 查询设备")
    @GetMapping("getByDeviceKey/{deviceKey}")
    public Result<Device> getByDeviceKey(@PathVariable String deviceKey) {
        Device device = deviceService.getByDeviceKey(deviceKey);
        if (device != null) {
            return Result.ok(device);
        } else {
            return Result.fail("设备不存在");
        }
    }

    @ApiOperation("修改设备信息")
    @PostMapping("updateDevice")
    public Result<String> updateDevice(@RequestBody Device device) {
        boolean b = deviceService.updateById(device);
        if (b) {
            return Result.ok();
        } else
            return Result.fail("修改失败");
    }

    @ApiOperation("新增设备")
    @PostMapping("addDevice")
    public Result<String> addDevice(@RequestBody Device device) {
        boolean save = deviceService.save(device);
        if (save) {
            return Result.ok();
        } else
            return Result.fail("新增失败");
    }

    @ApiOperation("更新设备在线状态")
    @PostMapping("updateOnlineStatus")
    public Result<String> updateOnlineStatus(
            @ApiParam(name = "deviceKey", value = "设备标识", required = true) @RequestParam String deviceKey,
            @ApiParam(name = "status", value = "在线状态：0-离线，1-在线", required = true, example = "1") @RequestParam Integer status) {
        deviceService.updateOnlineStatus(deviceKey, status);
        return Result.ok();
    }
}
