package com.example.room.mqtt.common;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.room.environment.entity.Environment;
import com.example.room.environment.service.DeviceOptionService;
import com.example.room.environment.service.EnvironmentService;
import com.example.room.mqtt.entity.MqttReceiveCmdResp;
import com.example.room.mqtt.entity.MqttReceiveReport;
import com.example.room.mqtt.service.MqttReceiveCmdRespService;
import com.example.room.mqtt.service.MqttReceiveReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@Service
public class MqttProcessMessageService {

    @Resource
    private MqttReceiveReportService mqttReceiveReportService;

    @Resource
    private MqttReceiveCmdRespService mqttReceiveCmdRespService;

    @Resource
    private EnvironmentService environmentService;

    @Resource
    private DeviceOptionService deviceOptionService;


    @Transactional(rollbackFor = Exception.class)
    public void processMessage(String topic, String payload) {
        if (topic.contains("report")) {
            // 1. 原始JSON直接落库
            MqttReceiveReport dataEntity = new MqttReceiveReport();
            dataEntity.setTopic(topic);
            dataEntity.setPayload(payload);
            dataEntity.setReceiveTime(new Date());
            dataEntity.setDeviceId(parseDeviceIdFromTopic(topic));
            mqttReceiveReportService.save(dataEntity);
            // 2. 解析并处理环境数据
            processEnvironmentData(payload);
        } else if (topic.contains("resp")) {
            // 1. 原始JSON直接落库
            MqttReceiveCmdResp dataEntity = new MqttReceiveCmdResp();
            dataEntity.setTopic(topic);
            dataEntity.setPayload(payload);
            dataEntity.setReceiveTime(new Date());
            dataEntity.setDeviceId(parseDeviceIdFromTopic(topic));
            mqttReceiveCmdRespService.save(dataEntity);
            // 2. 解析并处理报警数据
            deviceOptionService.onMqttMessage(topic, payload);
        }
    }

    private void processEnvironmentData(String payload) {
        try {
            // 解析JSON字符串
            JSONObject jsonObject = JSON.parseObject(payload);
            /*
            {
              "dev": "stm32_01",
              "temp": 25,
              "humi": 47,
              "gasPPM": 1,
              "gasDig": 0,
              "ldrDig": 1,
              "ldrPer": 21,
              "flameDig": 0,
              "flamePer": 0,
              "alarm": 0,
              "fan": 0,
              "led": 0
            }
             */
            // 如果包含其中任意一个字段，则插入数据
            Environment environment = new Environment();
            // 设置默认值
            environment.setSource(1); // 来源：1上报
            environment.setGmtCreate(new Date());
            // 解析并设置各个字段值
            if (jsonObject.containsKey("dev")) {
                Object deviceId = jsonObject.get("dev");
                if (deviceId != null) {
                    environment.setDeviceId(deviceId.toString());
                }
            }

            if (jsonObject.containsKey("temp")) {
                Object temperature = jsonObject.get("temp");
                if (temperature != null) {
                    environment.setTemperature(Float.parseFloat(temperature.toString()));
                }
            }

            if (jsonObject.containsKey("humi")) {
                Object humidity = jsonObject.get("humi");
                if (humidity != null) {
                    environment.setHumidity(Float.parseFloat(humidity.toString()));
                }
            }

            if (jsonObject.containsKey("gasPPM")) {
                Object smoke = jsonObject.get("gasPPM");
                if (smoke != null) {
                    environment.setGasPpm(Float.parseFloat(smoke.toString()));
                }
            }
            if (jsonObject.containsKey("gasDig")) {
                Object gasDig = jsonObject.get("gasDig");
                if (gasDig != null) {
                    environment.setGasStatus(Integer.parseInt(gasDig.toString()));
                }
            }
            if (jsonObject.containsKey("ldrDig")) {
                Object ldrDig = jsonObject.get("ldrDig");
                if (ldrDig != null) {
                    environment.setLightStatus(Integer.parseInt(ldrDig.toString()));
                }
            }
            if (jsonObject.containsKey("flameDig")) {
                Object flameDig = jsonObject.get("flameDig");
                if (flameDig != null) {
                    environment.setFlameStatus(Integer.parseInt(flameDig.toString()));
                }
            }
            if (jsonObject.containsKey("ldrPer")) {
                Object ldrPer = jsonObject.get("ldrPer");
                if (ldrPer != null) {
                    environment.setLightPercentage(Float.parseFloat(ldrPer.toString()));
                }
            }
            if (jsonObject.containsKey("flamePer")) {
                Object flamePer = jsonObject.get("flamePer");
                if (flamePer != null) {
                    environment.setFlamePercentage(Float.parseFloat(flamePer.toString()));
                }
            }
            if (jsonObject.containsKey("alarm")) {
                Object beep = jsonObject.get("alarm");
                if (beep != null) {
                    environment.setAlarmStatus(Integer.parseInt(beep.toString()));
                }
            }
            if (jsonObject.containsKey("fan")) {
                Object fan = jsonObject.get("fan");
                if (fan != null) {
                    environment.setFanStatus(Integer.parseInt(fan.toString()));
                }
            }
            if (jsonObject.containsKey("led")) {
                Object led = jsonObject.get("led");
                if (led != null) {
                    environment.setLedStatus(Integer.parseInt(led.toString()));
                }
            }
            // 设置默认值
            environment.setSource(1); // 来源：1上报
            environment.setGmtCreate(new Date());

            // 处理measureTime字段
            Object measureTimeObj = jsonObject.get("measureTime");

            if (measureTimeObj != null) {
                try {
                    // 支持多种日期格式
                    String timeStr = measureTimeObj.toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date measureTime = sdf.parse(timeStr);
                    environment.setGmtMeasurement(measureTime);
                } catch (Exception e) {
                    log.warn("measureTime格式解析失败，使用当前时间: {}", e.getMessage());
                    environment.setGmtMeasurement(new Date());
                }
            } else {
                // 如果没有measureTime字段，使用当前时间
                environment.setGmtMeasurement(new Date());
            }
            // 插入到数据库
            boolean saved = environmentService.save(environment);
        } catch (Exception e) {
            log.error("处理\"environment数据时发生异常: {}", e.getMessage(), e);
        }
    }

    /**
     * 从topic中解析设备ID
     * 例如: device/abc123/data -> abc123
     */
    private String parseDeviceIdFromTopic(String topic) {
        if (topic == null) return null;
        String[] parts = topic.split("/");
        if (parts.length >= 3) {
            return parts[2];
        }
        if (parts.length == 2) {
            return parts[1];
        }
        return null;
    }
}