package com.project.blog.controller;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.blog.common.dto.LoginDto;
import com.project.blog.entity.User;
import com.project.blog.lang.Result;
import com.project.blog.service.UserService;
import com.project.blog.shiro.JwtUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class LoginController {
    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;  //jwt工具

    //登录控制
    @PostMapping("/login")
    public Result login(@Validated @RequestBody LoginDto loginDto, HttpServletResponse response) {
        //用phone查询数据库，创建user接收返回值
        User user = userService.getOne(new QueryWrapper<User>().eq("phone", loginDto.getPhone()));
        //当返回的user为空时，抛出异常
        Assert.notNull(user, "账号或密码不正确");
        //判断密码是否正确
        if (!user.getPassword().equals(SecureUtil.md5(loginDto.getPassword()))) {
            //System.out.println(SecureUtil.md5(loginDto.getPassword()));
            //使用了md5加密
            return Result.fail("账号或密码不正确");
        }
        //如果密码正确，使用jwtUtils工具包来制作一个令牌
        String jwt = jwtUtils.generateToken((Long) user.getId());
        //将令牌返回前端header中的Authorization
        response.setHeader("Authorization", jwt);
        //设置前端header中的Access-control-Expose-Headers
        response.setHeader("Access-control-Expose-Headers", "Authorization");
        response.setHeader("Access-Control-Allow-Credentials","true");
        //往前端返回用户的基本数据
        return Result.succ(MapUtil.builder()
                .put("name", user.getName())
                .put("phone", user.getPhone())
                .put("icons", user.getIcon())
                .put("status", user.getStatus())
                .map()
        );
    }

    //登出控制
    @RequiresAuthentication //登出需要进行权限验证
    @GetMapping("/logout")
    public Result logout(){
        SecurityUtils.getSubject().logout();
        return Result.succ(null);
    }
}
