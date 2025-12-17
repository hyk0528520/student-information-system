package com.heyongkang.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 处理未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public ModelAndView handleNotLoginException(NotLoginException e, HttpServletRequest request) {
        logger.warn("用户未登录访问: {}", request.getRequestURI());

        // AJAX请求返回JSON
        if (isAjaxRequest(request)) {
            ModelAndView mav = new ModelAndView();
            Map<String, Object> result = new HashMap<>();
            result.put("success", false);
            result.put("code", 401);
            result.put("message", "请先登录");
            mav.addAllObjects(result);
            mav.setViewName("jsonView");
            return mav;
        }

        // 普通请求重定向到登录页
        return new ModelAndView("redirect:/login");
    }

    /**
     * 处理权限不足异常
     */
    @ExceptionHandler(NotPermissionException.class)
    @ResponseBody
    public Map<String, Object> handleNotPermissionException(NotPermissionException e) {
        logger.warn("权限不足: {}", e.getMessage());

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("code", 403);
        result.put("message", "权限不足");
        return result;
    }

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public Map<String, Object> handleBusinessException(BusinessException e) {
        logger.error("业务异常: {}", e.getMessage(), e);

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("code", 400);
        result.put("message", e.getMessage());
        return result;
    }

    /**
     * 处理所有其他异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, Object> handleException(Exception e, HttpServletRequest request) {
        logger.error("系统异常: {}", request.getRequestURI(), e);

        Map<String, Object> result = new HashMap<>();
        result.put("success", false);
        result.put("code", 500);
        result.put("message", "系统异常，请稍后重试");
        // 生产环境隐藏详细错误
        if (!"prod".equals(System.getProperty("spring.profiles.active"))) {
            result.put("detail", e.getMessage());
        }
        return result;
    }

    /**
     * 判断是否为AJAX请求
     */
    private boolean isAjaxRequest(HttpServletRequest request) {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"))
                || "application/json".equals(request.getHeader("Content-Type"))
                || request.getHeader("Accept") != null && request.getHeader("Accept").contains("application/json");
    }
}

/**
 * 业务异常类
 */
class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}