package com.heyongkang.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.heyongkang.entity.base.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("student_info")
public class Student extends BaseEntity {
    private String studentNo;
    private String studentName;
    private Integer gender;

    // 修复：添加日期格式化注解
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date birthday;

    private String idCard;
    private String phone;
    private String email;
    private String college;
    private String major;
    private String className;
    private String enrollmentYear;
    private Integer status;
    private String address;
    private String emergencyContact;
    private String emergencyPhone;

    @TableField(exist = false)
    private Integer age;  // 计算字段
}