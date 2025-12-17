package com.heyongkang.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heyongkang.entity.Course;
import com.heyongkang.mapper.CourseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class CourseService extends ServiceImpl<CourseMapper, Course> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final String COURSE_CACHE_KEY = "courses:all";
    private static final String SEMESTER_CACHE_KEY = "semesters:all";

    /**
     * 分页查询课程
     */
    public Page<Course> getCoursePage(Integer page, Integer size, String keyword) {
        Page<Course> pageParam = new Page<>(page, size);
        QueryWrapper<Course> wrapper = new QueryWrapper<>();

        if (StringUtils.hasText(keyword)) {
            wrapper.like("course_no", keyword)
                    .or().like("course_name", keyword)
                    .or().like("teacher", keyword)
                    .or().like("semester", keyword);
        }

        wrapper.eq("deleted", 0)
                .orderByDesc("create_time");

        return this.page(pageParam, wrapper);
    }

    /**
     * 获取所有课程（带缓存）
     */
    public List<Course> getAllCourses() {
        List<Course> courses = (List<Course>) redisTemplate.opsForValue().get(COURSE_CACHE_KEY);
        if (courses != null && !courses.isEmpty()) {
            return courses;
        }

        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.eq("deleted", 0)
                .orderByAsc("course_no");
        courses = this.list(wrapper);

        if (courses != null && !courses.isEmpty()) {
            redisTemplate.opsForValue().set(COURSE_CACHE_KEY, courses, 1, TimeUnit.HOURS);
        }

        return courses;
    }

    /**
     * 获取所有学期
     */
    public List<String> getAllSemesters() {
        List<String> semesters = (List<String>) redisTemplate.opsForValue().get(SEMESTER_CACHE_KEY);
        if (semesters != null && !semesters.isEmpty()) {
            return semesters;
        }

        QueryWrapper<Course> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT semester")
                .isNotNull("semester")
                .ne("semester", "")
                .eq("deleted", 0)
                .orderByDesc("semester");

        List<Course> courses = this.list(wrapper);
        semesters = courses.stream()
                .map(Course::getSemester)
                .toList();

        if (semesters != null && !semesters.isEmpty()) {
            redisTemplate.opsForValue().set(SEMESTER_CACHE_KEY, semesters, 2, TimeUnit.HOURS);
        }

        return semesters;
    }

    /**
     * 清除课程缓存
     */
    public void clearCourseCache() {
        redisTemplate.delete(COURSE_CACHE_KEY);
        redisTemplate.delete(SEMESTER_CACHE_KEY);
    }
}