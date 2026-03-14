package com.example.room.environment.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.room.environment.entity.DeviceOption;
import com.example.room.environment.entity.dto.DeviceOptionControl;
import com.example.room.environment.entity.dto.DeviceOptionQuery;

/**
 * <p>
 * 反控记录 服务类
 * </p>
 *
 * @author helloWorld
 * @since 2023-05-31
 */
public interface DeviceOptionService extends IService<DeviceOption> {

    // 条件查询分页方法
    Page<DeviceOption> pageQuery(Long current, Long limit, DeviceOptionQuery deviceOptionQuery);

    boolean controlDevice(DeviceOptionControl deviceOption, String operatorId);
}
