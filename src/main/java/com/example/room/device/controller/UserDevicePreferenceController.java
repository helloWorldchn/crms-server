package com.example.room.device.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.room.device.entity.Device;
import com.example.room.device.entity.UserDevicePreference;
import com.example.room.device.service.DeviceService;
import com.example.room.device.service.UserDevicePreferenceService;
import com.example.room.environment.entity.dto.EnvironmentStatisticsQuery;
import com.example.room.util.JwtUtil;
import com.example.room.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 用户设备偏好 Controller
 */
@RestController
@RequestMapping("/service/device/default")
public class UserDevicePreferenceController {

    @Resource
    private UserDevicePreferenceService userDevicePreferenceService;

    @Resource
    private DeviceService deviceService;

    /**
     * 获取用户默认设备
     * @param request Http请求
     * @return 默认设备信息
     */
    @GetMapping("/getDefaultDevice")
    public Result<UserDevicePreference> getDefaultDevice(HttpServletRequest request) {
        // String token = JwtUtil.getTokenFromRequest(request);
        // String code = JwtUtil.getAccountCodeFromToken(token);
        String code =  SecurityContextHolder.getContext().getAuthentication().getName();
        UserDevicePreference defaultDevice = userDevicePreferenceService.getDefaultDeviceKey(code);
        if (Objects.isNull(defaultDevice)) {
            QueryWrapper<Device> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().orderByDesc(Device::getOnlineStatus).orderByDesc(Device::getLastActiveTime);
            queryWrapper.lambda().last("limit 1");
            Device one = deviceService.getOne(queryWrapper);
            defaultDevice = new UserDevicePreference();
            defaultDevice.setDeviceKey(one.getDeviceKey());
        }
        return Result.ok(defaultDevice);
    }

    /**
     * 设置用户默认设备（实际是记录用户最后选择的设备）
     * @param request Http请求
     * @param param 设备标识
     * @return 操作结果
     */
    @PostMapping("/setDefaultDevice")
    public Result<Boolean> setDefaultDevice(HttpServletRequest request,@RequestBody(required = false) UserDevicePreference param) {
        // String token = JwtUtil.getTokenFromRequest(request);
        // String userCode = JwtUtil.getAccountCodeFromToken(token);

        String userCode =  SecurityContextHolder.getContext().getAuthentication().getName();
        boolean success = userDevicePreferenceService.setDefaultDevice(userCode, param.getDeviceKey());
        
        if (success) {
            return Result.ok(true);
        } else {
            return Result.fail("设置默认设备失败");
        }
    }

}