package com.example.room.environment.entity.dto;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

// 前端传递过来的数据封装到该类的对象中
@Data
public class DeviceOptionVo implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备反控记录ID")
    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private String id;

    @ApiModelProperty(value = "数据来源（1：硬件上报；0：手动记录）")
    private String deviceId;

    @ApiModelProperty(value = "控制类型：1-散热")
    private String deviceType;

    @ApiModelProperty(value = "设备名称")
    private String deviceTypeName;

    @ApiModelProperty(value = "命令：1-ON 2-OFF")
    private String command;

    @ApiModelProperty(value = "操作人")
    private String operator;

    @ApiModelProperty(value = "操作人姓名")
    private String operatorName;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

}
