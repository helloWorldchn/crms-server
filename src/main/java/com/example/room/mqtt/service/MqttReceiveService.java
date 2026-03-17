package com.example.room.mqtt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.room.mqtt.entity.MqttReceive;
import com.example.room.mqtt.entity.dto.MqttReceiveQuery;

/**
 * <p>
 * Mqtt接收 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-05-31
 */
public interface MqttReceiveService extends IService<MqttReceive> {
    // 条件查询分页方法
    Page<MqttReceive> pageQuery(MqttReceiveQuery receiveQuery);

    MqttReceive getLastReceive();
}
