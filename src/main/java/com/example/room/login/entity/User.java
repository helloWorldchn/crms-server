package com.example.room.login.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("user")
public class User implements Serializable {
    /**
     * 用户ID
     */
    @TableId
    private Integer id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 邮箱
     */
    private String avatar;

    /**
     * 生日
     */
    private Long birthday;

    /**
     * 创建日期
     */
    private Long gmtCreated;

    /**
     * 创建时间
     */
    private Long gmtModified;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
