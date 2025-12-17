package com.heyongkang.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heyongkang.entity.Course;
import com.heyongkang.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/course")
@SaCheckLogin
public class CourseController {

    @Autowired
    private CourseService courseService;

    /**
     * 课程列表页
     */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        Page<Course> coursePage = courseService.getCoursePage(page, size, keyword);
        model.addAttribute("page", coursePage);
        model.addAttribute("keyword", keyword);
        return "course/list";
    }

    /**
     * 课程详情API
     */
    @GetMapping("/api/detail/{id}")
    @ResponseBody
    public Map<String, Object> detail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        Course course = courseService.getById(id);
        if (course != null) {
            result.put("success", true);
            result.put("data", course);
        } else {
            result.put("success", false);
            result.put("message", "课程不存在");
        }
        return result;
    }

    /**
     * 获取所有学期API
     */
    @GetMapping("/api/semesters")
    @ResponseBody
    public Map<String, Object> getSemesters() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("data", courseService.getAllSemesters());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}