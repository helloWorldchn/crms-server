package com.example.room.mqtt.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.room.mqtt.entity.MqttSend;
import com.example.room.mqtt.entity.dto.MqttSendQuery;

/**
 * <p>
 * 讲师 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-05-31
 */
public interface MqttSendService extends IService<MqttSend> {
    // 条件查询分页方法
    Page<MqttSend> pageQuery(Long current, Long limit, MqttSendQuery receiveQuery);

    MqttSend getLastSend();
}
