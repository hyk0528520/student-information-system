package com.heyongkang.controller;

import cn.dev33.satoken.stp.StpUtil;
import com.heyongkang.entity.User;
import com.heyongkang.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    /**
     * 首页
     */
    @GetMapping("/")
    public String index(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("user", currentUser);
        return "index";
    }

    /**
     * 登录页
     */
    @GetMapping("/login")
    public String loginPage() {
        if (StpUtil.isLogin()) {
            return "redirect:/";
        }
        return "login";
    }

    /**
     * 登录处理
     */
    @PostMapping("/login")
    public String login(String username, String password,
                        HttpServletRequest request,
                        RedirectAttributes redirectAttributes) {
        try {
            User user = userService.login(username, password);
            request.getSession().setAttribute("currentUser", user);
            return "redirect:/";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/login";
        }
    }

    /**
     * 注销
     */
    @GetMapping("/logout")
    public String logout() {
        StpUtil.logout();
        return "redirect:/login";
    }

    /**
     * 注册页
     */
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    /**
     * 注册处理
     */
    @PostMapping("/register")
    public String register(User user, RedirectAttributes redirectAttributes) {
        try {
            boolean success = userService.register(user);
            if (success) {
                redirectAttributes.addFlashAttribute("message", "注册成功，请登录");
                return "redirect:/login";
            } else {
                redirectAttributes.addFlashAttribute("error", "注册失败");
                return "redirect:/register";
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/register";
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/api/currentUser")
    @ResponseBody
    public Map<String, Object> getCurrentUserInfo() {
        Map<String, Object> result = new HashMap<>();
        User user = userService.getCurrentUser();
        if (user != null) {
            result.put("success", true);
            result.put("data", user);
        } else {
            result.put("success", false);
            result.put("message", "用户未登录");
        }
        return result;
    }
}