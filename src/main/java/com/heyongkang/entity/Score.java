package com.heyongkang.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.heyongkang.entity.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("score")
public class Score extends BaseEntity {
    private Long studentId;
    private String studentNo;
    private String studentName;
    private Long courseId;
    private String courseNo;
    private String courseName;
    private BigDecimal score;
    private String semester;
    private Date examTime;
    private String remark;

    // 成绩等级
    public String getGradeLevel() {
        if (score == null) return "无";
        double s = score.doubleValue();
        if (s >= 90) return "优秀";
        if (s >= 80) return "良好";
        if (s >= 70) return "中等";
        if (s >= 60) return "及格";
        return "不及格";
    }
}