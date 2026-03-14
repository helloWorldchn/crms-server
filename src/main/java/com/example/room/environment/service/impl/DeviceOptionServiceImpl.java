package com.example.room.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.room.environment.entity.DeviceOption;
import com.example.room.environment.entity.dto.DeviceOptionControl;
import com.example.room.environment.entity.dto.DeviceOptionQuery;
import com.example.room.environment.mapper.DeviceOptionMapper;
import com.example.room.environment.service.DeviceOptionService;
import com.example.room.util.JwtUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

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
    private final JwtUtil jWTUtil;

    public DeviceOptionServiceImpl(JwtUtil jWTUtil) {
        this.jWTUtil = jWTUtil;
    }

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

    @Override
    public boolean controlDevice(DeviceOptionControl deviceOptionControl, String operatorId) {
        // 1. send发送MQTT消息到 设备
        // 2. 更新数据库操作记录
        DeviceOption deviceOption = new DeviceOption();
        deviceOption.setDeviceId(deviceOptionControl.getDeviceId());
        deviceOption.setDeviceType(deviceOptionControl.getDeviceType());
        deviceOption.setCommand(deviceOptionControl.getCommand());
        deviceOption.setOperator(operatorId);
        deviceOption.setGmtCreate(new Date());
        deviceOption.setIsDeleted(false);
        return this.save(deviceOption);
    }
}
