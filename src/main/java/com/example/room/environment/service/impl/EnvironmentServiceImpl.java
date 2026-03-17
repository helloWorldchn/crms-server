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
    public Page<Environment> pageQuery(EnvironmentQuery query) {
        // 创建page
        Page<Environment> page = new Page<>(query.getCurrentPage(), query.getPageSize());
        // 构建条件
        QueryWrapper<Environment> queryWrapper = new QueryWrapper<>();

        Integer source = query.getSource();
        String begin = query.getBegin();
        String end = query.getEnd();
        if (Objects.nonNull(query.getSource())) {
            queryWrapper.lambda().eq(Environment::getSource, source); // eq等于
        }
        if (!StringUtils.isEmpty(begin)) {
            queryWrapper.lambda().ge(Environment::getGmtCreate, begin); // ge大于等于
        }
        if (!StringUtils.isEmpty(end)) {
            queryWrapper.lambda().le(Environment::getGmtCreate, end); // le小于等于
        }
        // 排序
        queryWrapper.lambda().orderByDesc(Environment::getGmtCreate);
        return baseMapper.selectPage(page, queryWrapper);
    }

    @Override
    public List<Environment> getStatistics(EnvironmentStatisticsQuery query) {
        QueryWrapper<Environment> queryWrapper = new QueryWrapper<>();
        if (!StringUtils.isEmpty(query.getBegin())) {
            queryWrapper.lambda().ge(Environment::getGmtCreate, query.getBegin()); // ge大于等于
        }
        if (!StringUtils.isEmpty(query.getEnd())) {
            queryWrapper.lambda().le(Environment::getGmtCreate, query.getEnd()); // le小于等于
        }
        queryWrapper.lambda().orderByDesc(Environment::getGmtCreate);
        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public Environment getLastData() {
        QueryWrapper<Environment> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(Environment::getGmtCreate);
        queryWrapper.last("limit 1");
        // Service层
        return this.getOne(queryWrapper);
    }
}
