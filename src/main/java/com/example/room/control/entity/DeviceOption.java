package com.example.room.control.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 讲师
 * </p>
 *
 * @author helloWorld
 * @since 2023-05-31
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="DeviceOption对象", description="设备反控记录数据")
public class DeviceOption implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "设备反控记录ID")
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    @ApiModelProperty(value = "数据来源（1：硬件上报；0：手动记录）")
    private String deviceId;

    @ApiModelProperty(value = "控制类型：1-散热")
    private String deviceType;

    @ApiModelProperty(value = "命令：1-ON 2-OFF")
    private String command;

    @ApiModelProperty(value = "操作人")
    private Long operatorId;

    @ApiModelProperty(value = "逻辑删除 1（true）已删除， 0（false）未删除")
    @TableLogic
    private Boolean isDeleted;

    @ApiModelProperty(value = "创建时间")
    @TableField(fill = FieldFill.INSERT)
    private Date gmtCreate;

}
