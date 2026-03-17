package com.example.room.mqtt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.room.mqtt.entity.MqttReceive;
import com.example.room.mqtt.entity.dto.MqttReceiveQuery;
import com.example.room.mqtt.mapper.MqttReceiveMapper;
import com.example.room.mqtt.service.MqttReceiveService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

@Slf4j
@Service
public class MqttReceiveServiceImpl extends ServiceImpl<MqttReceiveMapper, MqttReceive> implements MqttReceiveService {

    @Override
    public Page<MqttReceive> pageQuery(MqttReceiveQuery receiveQuery) {
        // 创建page
        Page<MqttReceive> mqttReceivePage = new Page<>(receiveQuery.getCurrentPage(), receiveQuery.getPageSize());
        // 构建条件
        QueryWrapper<MqttReceive> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(receiveQuery.getBegin())) {
            queryWrapper.lambda().ge(MqttReceive::getReceiveTime, receiveQuery.getBegin()); // ge大于
        }
        if (!StringUtils.isEmpty(receiveQuery.getEnd())) {
            queryWrapper.lambda().le(MqttReceive::getReceiveTime, receiveQuery.getEnd()); // le小于
        }
        // 排序
        queryWrapper.lambda().orderByDesc(MqttReceive::getReceiveTime);
        return baseMapper.selectPage(mqttReceivePage, queryWrapper);
    }

    @Override
    public MqttReceive getLastReceive() {
        QueryWrapper<MqttReceive> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(MqttReceive::getReceiveTime);
        queryWrapper.last("limit 1");
        // Service层
        return this.getOne(queryWrapper);
    }
}