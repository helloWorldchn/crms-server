package com.example.room.environment.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.room.environment.entity.Environment;
import com.example.room.environment.entity.dto.EnvironmentQuery;
import com.example.room.environment.entity.dto.EnvironmentStatisticsQuery;
import com.example.room.environment.mapper.EnvironmentMapper;
import com.example.room.environment.service.EnvironmentService;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * <p>
 * 环境数据 服务实现类
 * </p>
 *
 * @author helloWorld
 * @since 2023-05-31
 */
@Service
public class EnvironmentServiceImpl extends ServiceImpl<EnvironmentMapper, Environment> implements EnvironmentService {
    @Override
    public Page<Environment> pageQuery(Long current, Long limit, EnvironmentQuery environmentQuery) {
        // 创建page
        Page<Environment> page = new Page<>(current, limit);
        // 构建条件
        QueryWrapper<Environment> queryWrapper = new QueryWrapper<>();
        if (environmentQuery == null){
            baseMapper.selectPage(page, queryWrapper);
            return new Page<>();
        }
        // 多条件组合查询
        // MyBatis的动态sql
        Integer source = environmentQuery.getSource();
        String begin = environmentQuery.getBegin();
        String end = environmentQuery.getEnd();
        if (Objects.nonNull(source)) {
            queryWrapper.eq("source", source); // eq等于
        }
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
    public List<Environment> getStatistics(EnvironmentStatisticsQuery query) {
        QueryWrapper<Environment> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(query.getBegin())) {
            queryWrapper.ge("gmt_create", query.getBegin()); // ge大于
        }
        if (!StringUtils.isEmpty(query.getEnd())) {
            queryWrapper.le("gmt_create", query.getEnd()); // le小于
        }
        queryWrapper.orderByDesc("gmt_create");
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Environment getLastData() {
        QueryWrapper<Environment> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("gmt_create");
        queryWrapper.last("limit 1");
        // Service层
        return this.getOne(queryWrapper);
    }
}
