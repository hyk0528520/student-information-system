package com.heyongkang.service;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.heyongkang.entity.User;
import com.heyongkang.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import cn.dev33.satoken.secure.SaSecureUtil;
import cn.dev33.satoken.stp.StpUtil;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 用户登录
     */
    public User login(String username, String password) {
        // 查询用户
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", username)
                .eq("deleted", 0)
                .eq("status", 1);
        User user = this.getOne(wrapper);

        if (user == null) {
            throw new RuntimeException("用户不存在或已被禁用");
        }

        // MD5加密后比较密码
        String encryptedPassword = DigestUtil.md5Hex(password);
        if (!user.getPassword().equals(encryptedPassword)) {
            throw new RuntimeException("密码错误");
        }

        // 登录成功，存储用户信息到Sa-Token
        StpUtil.login(user.getId());
        user.setToken(StpUtil.getTokenValue());

        // 将用户信息存入Redis，设置30分钟过期
        String redisKey = "user:" + user.getId();
        redisTemplate.opsForValue().set(redisKey, user, 30, TimeUnit.MINUTES);

        return user;
    }

    /**
     * 用户注册
     */
    public boolean register(User user) {
        // 检查用户名是否已存在
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.eq("username", user.getUsername()).eq("deleted", 0);
        if (this.count(wrapper) > 0) {
            throw new RuntimeException("用户名已存在");
        }

        // 加密密码
        user.setPassword(DigestUtil.md5Hex(user.getPassword()));
        user.setRole("student");  // 默认学生角色
        user.setStatus(1);

        return this.save(user);
    }

    /**
     * 获取当前登录用户
     */
    public User getCurrentUser() {
        if (!StpUtil.isLogin()) {
            return null;
        }
        Object userId = StpUtil.getLoginId();
        String redisKey = "user:" + userId;

        // 先从Redis获取
        User user = (User) redisTemplate.opsForValue().get(redisKey);
        if (user == null) {
            // Redis中没有，从数据库查询
            user = this.getById((Serializable) userId);
            if (user != null) {
                redisTemplate.opsForValue().set(redisKey, user, 30, TimeUnit.MINUTES);
            }
        }
        return user;
    }

    /**
     * 修改密码
     */
    public boolean changePassword(Long userId, String oldPassword, String newPassword) {
        User user = this.getById(userId);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        String encryptedOld = DigestUtil.md5Hex(oldPassword);
        if (!user.getPassword().equals(encryptedOld)) {
            throw new RuntimeException("原密码错误");
        }

        user.setPassword(DigestUtil.md5Hex(newPassword));
        return this.updateById(user);
    }
}