package com.example.room.mqtt.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.room.mqtt.entity.MqttSend;
import com.example.room.mqtt.entity.dto.MqttSendQuery;
import com.example.room.mqtt.service.MqttSendService;
import com.example.room.util.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@Api("MQTT发送消息管理")
@RestController
@RequestMapping("/mqtt/send")
public class MqttSendController {

    // 把service注入
    @Resource
    private MqttSendService sendService;

    //1. 查询MQTT发送消息所有数据
    // restful风格
    // 访问地址  http://localhost:8001/mqtt/send/findAll
    @ApiOperation(value = "所有MQTT发送记录列表")
    @GetMapping("findAll")
    public Result<List<MqttSend>> findAllSend() {
        // 调用service的方法实现查询所有的操作
        List<MqttSend> list = sendService.list();
        return Result.ok(list);
    }

    // 2.逻辑删除MQTT发送消息讲师方法
    @ApiOperation(value = "逻辑删除")
    @DeleteMapping("{id}")
    public Result<String> removeSend(@ApiParam(name = "id", value = "消息id", required = true) @PathVariable String id) {
        boolean flag = sendService.removeById(id);
        if (flag) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    // 4.添加查询带分页的方法
    @ApiOperation(value = "条件查询分页方法")
    @PostMapping("pageSendCondition/{current}/{limit}")
    public Result<Page<MqttSend>> pageSendCondition(@PathVariable Long current, @PathVariable Long limit,
                                                          @RequestBody(required = false) MqttSendQuery sendQuery) {
        // 调用方法，实现分页查询
        Page<MqttSend> resultPage = sendService.pageQuery(current, limit, sendQuery);
        return Result.ok(resultPage);
    }

    @ApiOperation(value = "获取最新一条记录")
    @GetMapping("getLastSend")
    public Result<MqttSend> getLastSend() {
        // 调用方法，实现分页查询
        MqttSend send = sendService.getLastSend();
        return Result.ok(send);
    }

    @ApiOperation("添加MQTT发送记录")
    @PostMapping("addMqttSend")
    public Result<String> addMqttSend(@RequestBody MqttSend mqttSend) {
        mqttSend.setSendTime(new Date());
        boolean save = sendService.save(mqttSend);
        if (save) {
            return Result.ok();
        } else
            return Result.fail();
    }

    @ApiOperation("根据ID查询MQTT发送记录")
    @GetMapping("getMqttSend/{id}")
    public Result<MqttSend> getMqttSend(@PathVariable String id) {
        MqttSend byId = sendService.getById(id);
        return Result.ok(byId);
    }

    @ApiOperation("修改MQTT发送记录")
    @PostMapping("updateMqttSend")
    public Result<String> updateMqttSend(@RequestBody MqttSend mqttSend) {
        boolean b = sendService.updateById(mqttSend);
        if (b) {
            return Result.ok();
        } else
            return Result.fail("修改失败");
    }
}
