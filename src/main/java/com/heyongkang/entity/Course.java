package com.heyongkang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.heyongkang.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("course")
public class Course extends BaseEntity {
    private String courseNo;
    private String courseName;
    private String teacher;
    private BigDecimal credit;
    private Integer hours;
    private String semester;
    private String description;
}