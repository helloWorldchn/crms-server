package com.example.room.environment.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

// 前端传递过来的数据封装到该类的对象中
@Data
public class UpdatePasswordReq implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "用户名")
    private String accountId;

    @ApiModelProperty(value = "用户名")
    private String password;

}
