package com.heyongkang.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heyongkang.entity.Student;
import com.heyongkang.mapper.StudentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class StudentService extends ServiceImpl<StudentMapper, Student> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String STUDENT_CACHE_KEY = "students:all";
    private static final String COLLEGE_CACHE_KEY = "colleges:all";

    /**
     * 分页查询学生
     */
    public Page<Student> getStudentPage(Integer page, Integer size, String keyword) {
        Page<Student> pageParam = new Page<>(page, size);
        QueryWrapper<Student> wrapper = new QueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.like("student_no", keyword)
                    .or().like("student_name", keyword)
                    .or().like("phone", keyword)
                    .or().like("college", keyword)
                    .or().like("major", keyword);
        }

        wrapper.eq("deleted", 0)
                .orderByDesc("create_time");

        return this.page(pageParam, wrapper);
    }

    /**
     * 获取所有学生（带缓存）
     */
    public List<Student> getAllStudents() {
        // 先从Redis缓存获取
        List<Student> students = (List<Student>) redisTemplate.opsForValue().get(STUDENT_CACHE_KEY);
        if (students != null && !students.isEmpty()) {
            return students;
        }

        // 缓存中没有，从数据库查询
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0)
                .orderByAsc("student_no");
        students = this.list(wrapper);

        // 存入Redis缓存，设置1小时过期
        if (students != null && !students.isEmpty()) {
            redisTemplate.opsForValue().set(STUDENT_CACHE_KEY, students, 1, TimeUnit.HOURS);
        }

        return students;
    }

    /**
     * 获取所有学院（带缓存）
     */
    public List<String> getAllColleges() {
        // 先从Redis获取
        List<String> colleges = (List<String>) redisTemplate.opsForValue().get(COLLEGE_CACHE_KEY);
        if (colleges != null && !colleges.isEmpty()) {
            return colleges;
        }

        // 从数据库查询不重复的学院
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT college")
                .isNotNull("college")
                .ne("college", "")
                .eq("deleted", 0);

        List<Student> students = this.list(wrapper);
        colleges = students.stream()
                .map(Student::getCollege)
                .toList();

        // 存入缓存
        if (colleges != null && !colleges.isEmpty()) {
            redisTemplate.opsForValue().set(COLLEGE_CACHE_KEY, colleges, 2, TimeUnit.HOURS);
        }

        return colleges;
    }

    /**
     * 按条件统计学生
     */
    public long countByCondition(String college, String major, String className) {
        QueryWrapper<Student> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0);

        if (StringUtils.hasText(college)) {
            wrapper.eq("college", college);
        }
        if (StringUtils.hasText(major)) {
            wrapper.eq("major", major);
        }
        if (StringUtils.hasText(className)) {
            wrapper.eq("class_name", className);
        }

        return this.count(wrapper);
    }

    /**
     * 清除学生缓存
     */
    public void clearStudentCache() {
        redisTemplate.delete(STUDENT_CACHE_KEY);
        redisTemplate.delete(COLLEGE_CACHE_KEY);
    }
}