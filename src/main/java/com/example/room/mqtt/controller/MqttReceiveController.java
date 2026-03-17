package com.example.room.mqtt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.room.mqtt.entity.MqttReceive;
import com.example.room.mqtt.entity.dto.MqttReceiveQuery;
import com.example.room.mqtt.service.MqttReceiveService;
import com.example.room.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Api("MQTT接收消息管理")
@RestController
@RequestMapping("/mqtt/receive")
public class MqttReceiveController {
    // 把service注入
    @Resource
    private MqttReceiveService receiveService;

    //1. 查询MQTT发送消息所有数据
    // restful风格
    // 访问地址  http://localhost:8001/mqtt/receive/findAll
    @ApiOperation(value = "所有MQTT接收消息列表")
    @GetMapping("findAll")
    public Result<List<MqttReceive>> findAllReceive() {
        // 调用service的方法实现查询所有的操作
        List<MqttReceive> list = receiveService.list();
        return Result.ok(list);
    }

    // 2.逻辑删除MQTT发送消息讲师方法
    @ApiOperation(value = "逻辑删除")
    @DeleteMapping("{id}")
    public Result<String> removeReceive(@ApiParam(name = "id", value = "消息id", required = true) @PathVariable String id) {
        boolean flag = receiveService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 4.添加查询带分页的方法
    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("pageReceiveCondition")
    public Result<Page<MqttReceive>> pageReceiveCondition(@RequestBody MqttReceiveQuery receiveQuery) {
        // 调用方法，实现分页查询
        Page<MqttReceive> resultPage = receiveService.pageQuery(receiveQuery);
        return Result.ok(resultPage);
    }

    @ApiOperation(value = "获取最新一条记录")
    @GetMapping("getLastReceive")
    public Result<MqttReceive> getLastReceive() {
        // 调用方法，实现分页查询
        MqttReceive receive = receiveService.getLastReceive();
        return Result.ok(receive);
    }

    @ApiOperation("添加MQTT接收消息")
    @PostMapping("addMqttReceive")
    public Result<String> addMqttReceive(@RequestBody MqttReceive mqttReceive) {
        mqttReceive.setReceiveTime(new Date());
        boolean save = receiveService.save(mqttReceive);
        if (save) {
            return Result.ok();
        } else
            return Result.fail();
    }

    @ApiOperation("根据ID查询MQTT接收消息")
    @GetMapping("getMqttReceive/{id}")
    public Result<MqttReceive> getMqttReceive(@PathVariable String id) {
        MqttReceive byId = receiveService.getById(id);
        return Result.ok(byId);
    }

    @ApiOperation("修改MQTT接收消息")
    @PostMapping("updateMqttReceive")
    public Result<String> updateMqttReceive(@RequestBody MqttReceive mqttReceive) {
        boolean b = receiveService.updateById(mqttReceive);
        if (b) {
            return Result.ok();
        } else
            return Result.fail("修改失败");
    }
}
