package com.heyongkang.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.heyongkang.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("sys_user")
public class User extends BaseEntity {
    private String username;
    private String password;
    private String realName;
    private Integer gender;
    private String phone;
    private String email;
    private String role;  // admin, student
    private Integer status;

    @TableField(exist = false)
    private String token;
}