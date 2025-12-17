package com.heyongkang.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.heyongkang.entity.Student;
import com.heyongkang.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/student")
@SaCheckLogin
public class StudentController {

    @Autowired
    private StudentService studentService;

    /**
     * 学生列表页
     */
    @GetMapping("/list")
    public String list(@RequestParam(defaultValue = "1") Integer page,
                       @RequestParam(defaultValue = "10") Integer size,
                       @RequestParam(required = false) String keyword,
                       Model model) {
        Page<Student> studentPage = studentService.getStudentPage(page, size, keyword);

        // 如果没有数据，添加一些示例数据
        if (studentPage.getRecords().isEmpty()) {
            addSampleStudents();
            // 重新获取数据
            studentPage = studentService.getStudentPage(page, size, keyword);
        }

        model.addAttribute("page", studentPage);
        model.addAttribute("keyword", keyword);
        return "student/list";
    }

    /**
     * 添加示例学生数据
     */
    private void addSampleStudents() {
        try {
            // 检查数据库中是否有数据
            long count = studentService.count();
            if (count == 0) {
                // 添加一些示例学生
                Student student1 = new Student();
                student1.setStudentNo("2022001");
                student1.setStudentName("张三");
                student1.setGender(0);
                student1.setPhone("13800000001");
                student1.setCollege("计算机学院");
                student1.setMajor("软件工程");
                student1.setClassName("软件2201班");
                student1.setEnrollmentYear("2022");
                student1.setStatus(1);

                Student student2 = new Student();
                student2.setStudentNo("2022002");
                student2.setStudentName("李四");
                student2.setGender(1);
                student2.setPhone("13800000002");
                student2.setCollege("计算机学院");
                student2.setMajor("软件工程");
                student2.setClassName("软件2201班");
                student2.setEnrollmentYear("2022");
                student2.setStatus(1);

                Student student3 = new Student();
                student3.setStudentNo("2022003");
                student3.setStudentName("王五");
                student3.setGender(0);
                student3.setPhone("13800000003");
                student3.setCollege("信息学院");
                student3.setMajor("计算机科学");
                student3.setClassName("计科2201班");
                student3.setEnrollmentYear("2022");
                student3.setStatus(1);

                studentService.save(student1);
                studentService.save(student2);
                studentService.save(student3);

                // 清除缓存
                studentService.clearStudentCache();
            }
        } catch (Exception e) {
            // 如果添加失败，忽略错误
        }
    }

    /**
     * 添加学生页
     */
    @GetMapping("/add")
    public String addPage() {
        return "student/add";
    }

    /**
     * 添加学生
     */
    @PostMapping("/add")
    public String add(Student student, RedirectAttributes redirectAttributes) {
        try {
            boolean success = studentService.save(student);
            if (success) {
                // 清除缓存
                studentService.clearStudentCache();
                redirectAttributes.addFlashAttribute("success", "添加成功");
            } else {
                redirectAttributes.addFlashAttribute("error", "添加失败");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/student/list";
    }

    /**
     * 编辑学生页
     */
    @GetMapping("/edit/{id}")
    public String editPage(@PathVariable Long id, Model model) {
        Student student = studentService.getById(id);
        if (student == null) {
            return "redirect:/student/list";
        }
        model.addAttribute("student", student);
        return "student/edit";
    }

    /**
     * 编辑学生
     */
    @PostMapping("/edit")
    public String edit(Student student, RedirectAttributes redirectAttributes) {
        try {
            boolean success = studentService.updateById(student);
            if (success) {
                // 清除缓存
                studentService.clearStudentCache();
                redirectAttributes.addFlashAttribute("success", "修改成功");
            } else {
                redirectAttributes.addFlashAttribute("error", "修改失败");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/student/list";
    }

    /**
     * 删除学生
     */
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            boolean success = studentService.removeById(id);
            if (success) {
                // 清除缓存
                studentService.clearStudentCache();
                redirectAttributes.addFlashAttribute("success", "删除成功");
            } else {
                redirectAttributes.addFlashAttribute("error", "删除失败");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/student/list";
    }

    /**
     * API: 获取学生详情
     */
    @GetMapping("/api/detail/{id}")
    @ResponseBody
    public Map<String, Object> detail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        Student student = studentService.getById(id);
        if (student != null) {
            result.put("success", true);
            result.put("data", student);
        } else {
            result.put("success", false);
            result.put("message", "学生不存在");
        }
        return result;
    }

    /**
     * API: 获取所有学院
     */
    @GetMapping("/api/colleges")
    @ResponseBody
    public Map<String, Object> getColleges() {
        Map<String, Object> result = new HashMap<>();
        try {
            result.put("success", true);
            result.put("data", studentService.getAllColleges());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        return result;
    }
}