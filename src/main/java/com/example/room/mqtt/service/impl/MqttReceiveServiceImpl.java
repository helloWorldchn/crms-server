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
    public Page<MqttReceive> pageQuery(Long current, Long limit, MqttReceiveQuery receiveQuery) {
        // 创建page
        Page<MqttReceive> mqttReceivePage = new Page<>(current, limit);
        // 构建条件
        QueryWrapper<MqttReceive> queryWrapper = new QueryWrapper<>();
        if (receiveQuery == null){
            baseMapper.selectPage(mqttReceivePage, queryWrapper);
            return new Page<>();
        }
        // 多条件组合查询
        // MyBatis的动态sql
        String begin = receiveQuery.getBegin();
        String end = receiveQuery.getEnd();
        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.lambda().ge(MqttReceive::getReceiveTime, begin); // ge大于
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.lambda().le(MqttReceive::getReceiveTime, end); // le小于
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