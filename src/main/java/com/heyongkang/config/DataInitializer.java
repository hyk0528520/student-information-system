package com.heyongkang.config;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.heyongkang.entity.User;
import com.heyongkang.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        logger.info("开始初始化系统数据...");

        // 初始化管理员账号
        initAdminUser();

        logger.info("系统数据初始化完成");
    }

    /**
     * 初始化管理员账号
     */
    private void initAdminUser() {
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", "admin").eq("deleted", 0);
        if (userService.count(wrapper) == 0) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(DigestUtil.md5Hex("123456"));
            admin.setRealName("系统管理员");
            admin.setGender(0);
            admin.setPhone("13892294462");
            admin.setEmail("admin@student.com");
            admin.setRole("admin");
            admin.setStatus(1);

            boolean success = userService.save(admin);
            if (success) {
                logger.info("管理员账号初始化成功: admin/123456");
            } else {
                logger.error("管理员账号初始化失败");
            }
        } else {
            logger.info("管理员账号已存在，跳过初始化");
        }
    }
}