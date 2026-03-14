package com.example.room.mqtt.entity;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="MqttReceive", description="Mqtt接收记录")
public class MqttReceive implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "topic主题")
    private String topic;

    @ApiModelProperty(value = "payload有效载荷")
    private String payload;

    @ApiModelProperty(value = "接收数据来自于设备id")
    private String deviceId;

    @ApiModelProperty(value = "接收时间")
    @TableField(fill = FieldFill.INSERT)
    private Date receiveTime;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Boolean isDeleted;
}