package com.example.room.environment.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.room.environment.entity.DeviceOption;
import com.example.room.environment.entity.dto.DeviceOptionControl;
import com.example.room.environment.entity.dto.DeviceOptionQuery;
import com.example.room.environment.entity.enums.DeviceCommandEnum;
import com.example.room.environment.mapper.DeviceOptionMapper;
import com.example.room.environment.service.DeviceOptionService;
import com.example.room.mqtt.common.MqttSendMessageService;
import com.example.room.util.JwtUtil;
import com.example.room.util.RequestIdGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * <p>
 * 反控记录 服务实现类
 * </p>
 *
 * @author helloWorld
 * @since 2023-05-31
 */
@Service
public class DeviceOptionServiceImpl extends ServiceImpl<DeviceOptionMapper, DeviceOption> implements DeviceOptionService {

    @Resource
    private MqttSendMessageService mqttSendMessageService;

    @Override
    public Page<DeviceOption> pageQuery(Long current, Long limit, DeviceOptionQuery deviceOptionQuery) {
        // 创建page
        Page<DeviceOption> page = new Page<>(current, limit);
        // 构建条件
        QueryWrapper<DeviceOption> queryWrapper = new QueryWrapper<>();
        if (deviceOptionQuery == null){
            baseMapper.selectPage(page, queryWrapper);
            return new Page<>();
        }
        // 多条件组合查询
        // MyBatis的动态sql
        String begin = deviceOptionQuery.getBegin();
        String end = deviceOptionQuery.getEnd();
        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.ge("gmt_create", begin); // ge大于
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.le("gmt_create", end); // le小于
        }
        // 排序
        queryWrapper.orderByDesc("gmt_create");
        return baseMapper.selectPage(page, queryWrapper);
    }
    private final ConcurrentHashMap<String, CompletableFuture<Boolean>> pendingRequests = new ConcurrentHashMap<>();
    private final RequestIdGenerator idGenerator = new RequestIdGenerator();
    @Override
    public boolean controlDevice(DeviceOptionControl deviceOptionControl, String operatorId) {
        // 1. send发送MQTT消息到 设备

        String requestId = idGenerator.nextId();
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        // 如果遇到极低概率的冲突（同一ID已存在），可自旋重试
        while (pendingRequests.putIfAbsent(requestId, future) != null) {
            requestId = idGenerator.nextId(); // 重新生成
        }
        String topic = "room" + "/" + deviceOptionControl.getDeviceId() + "/command";

        // 使用枚举生成消息体
        DeviceCommandEnum commandEnum = DeviceCommandEnum.fromCode(deviceOptionControl.getCommand());
        String message = "{\"" + deviceOptionControl.getDeviceType() + "\":" +
                (commandEnum != null ? commandEnum.getIntValue() : 0) +
                ",\"cmdId\":\"" + requestId + "\"" +
                "}";
        boolean success = mqttSendMessageService.sendMessage(topic, message);
        try {
            return future.get(1000L, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            pendingRequests.remove(requestId); // 超时清理
            return false;
        } catch (Exception e) {
            pendingRequests.remove(requestId);
            return false;
        } finally {
            // 2. 更新数据库操作记录
            DeviceOption deviceOption = new DeviceOption();
            deviceOption.setDeviceId(deviceOptionControl.getDeviceId());
            deviceOption.setDeviceType(deviceOptionControl.getDeviceType());
            deviceOption.setCommand(deviceOptionControl.getCommand());
            deviceOption.setOperator(operatorId);
            deviceOption.setGmtCreate(new Date());
            deviceOption.setIsDeleted(false);
            this.save(deviceOption);
        }
    }
    // MQTT 回调方法
    @Override
    public void onMqttMessage(String topic, String payload) {

        JSONObject json = JSON.parseObject(payload);
        if (json.containsKey("cmdId")) {
            Object cmdId = json.get("cmdId").toString();
            CompletableFuture<Boolean> future = pendingRequests.remove(cmdId);
            if (future != null) {
                // 可以进一步检查回复内容是否匹配指令，但一般有 ID 就足够
                future.complete(true);
            }
        }
    }
}
