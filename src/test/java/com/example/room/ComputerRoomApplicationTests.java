package com.example.room;

import com.example.room.environment.entity.Environment;
import com.example.room.util.WebSocketPushUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ComputerRoomApplicationTests {
    @Test
    void contextLoads() {
    }
    @Autowired
    private WebSocketPushUtil webSocketPushUtil;

    /**
     * 模拟数据更新后推送
     */
    @Test
    public void testPushEnvironmentData() {
        // 模拟环境数据
        Environment data = new Environment();
        data.setTemperature(25);
        data.setHumidity(60);
        data.setGasPpm(50);
        data.setGasStatus(0);
        data.setLightStatus(1);
        data.setLightPercentage(80);
        data.setFlameStatus(0);
        data.setFlamePercentage(0);
        data.setAlarmStatus(0);
        data.setFanStatus(0);
        data.setLedStatus(1);
        data.setDeviceId("DEV001");

        // 调用推送方法
        webSocketPushUtil.pushToTopic("/topic/environment", data);

        // 如果没有抛出异常，则认为测试通过
        // 实际场景中可以配合WebSocket客户端验证，但这里简化
    }
}
