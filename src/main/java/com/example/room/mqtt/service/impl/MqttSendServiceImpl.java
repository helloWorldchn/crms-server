package com.example.room.mqtt.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.room.mqtt.entity.MqttSend;
import com.example.room.mqtt.entity.dto.MqttSendQuery;
import com.example.room.mqtt.mapper.MqttSendMapper;
import com.example.room.mqtt.service.MqttSendService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MqttSendServiceImpl extends ServiceImpl<MqttSendMapper, MqttSend> implements MqttSendService {
    @Override
    public Page<MqttSend> pageQuery(Long current, Long limit, MqttSendQuery sendQuery) {
        // 创建page
        Page<MqttSend> mqttSendPage = new Page<>(current, limit);
        // 构建条件
        QueryWrapper<MqttSend> queryWrapper = new QueryWrapper<>();
        if (sendQuery == null){
            baseMapper.selectPage(mqttSendPage, queryWrapper);
            return new Page<>();
        }
        // 多条件组合查询
        // MyBatis的动态sql
        String begin = sendQuery.getBegin();
        String end = sendQuery.getEnd();
        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.lambda().ge(MqttSend::getSendTime, begin); // ge大于
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.lambda().le(MqttSend::getSendTime, end); // le小于
        }
        // 排序
        queryWrapper.lambda().orderByDesc(MqttSend::getSendTime);
        return baseMapper.selectPage(mqttSendPage, queryWrapper);
    }

    @Override
    public MqttSend getLastSend() {
        QueryWrapper<MqttSend> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(MqttSend::getSendTime);
        queryWrapper.last("limit 1");
        // Service层
        return this.getOne(queryWrapper);
    }
}