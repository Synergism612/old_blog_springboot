package com.project.blog.controller;

import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import com.project.blog.common.dto.RegisterDto;
import com.project.blog.entity.User;
import com.project.blog.lang.Result;
import com.project.blog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;

@RestController
@CrossOrigin
public class RegisterController {
    @Autowired
    private Producer captchaProducer;
    @Autowired
    private UserService userService;

    @RequestMapping("/captcha/{time}")
    public void getKaptchaImage(HttpServletRequest request, HttpServletResponse response, @PathVariable String time) throws Exception {
        //用字节数组存储
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        ServletOutputStream responseOutputStream =
                response.getOutputStream();
        final HttpSession httpSession = request.getSession();
        try {
            //生产验证码字符串并保存到session中
            String createText = captchaProducer.createText();
            //打印随机生成的字母和数字
//            System.out.println(createText);
            httpSession.setAttribute(Constants.KAPTCHA_SESSION_KEY, createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = captchaProducer.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
            captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
            response.setHeader("Cache-Control", "no-store");
            response.setHeader("Pragma", "no-cache");
            response.setDateHeader("Expires", 0);
            response.setContentType("image/jpeg");
            //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
            responseOutputStream.write(captchaChallengeAsJpeg);
            responseOutputStream.flush();
        } catch (IllegalArgumentException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } finally {
            responseOutputStream.close();
        }
    }

    @PostMapping("/checkable/{code}")
    public Result checkcode(HttpServletRequest request, @PathVariable String code) {
        String captchaId = (String)
                request.getSession().getAttribute(Constants.KAPTCHA_SESSION_KEY);
//        System.out.println("Session  vrifyCode "+captchaId.toUpperCase()+" form veritycode "+code.toUpperCase());
        if (!captchaId.toUpperCase().equals(code.toUpperCase())) {
            return Result.fail("验证码错误");
        } else {
            return Result.succ("验证码通过");
        }
    }

    @GetMapping("/register/{phone}")
    public Result registerPhone(@PathVariable String phone){
        if(userService.list(new QueryWrapper<User>().eq("phone", phone)).isEmpty()){
            return Result.succ("手机未被注册");
        }else {
            return Result.fail("该手机号已注册");
        }
    }

    @PostMapping("/register")
    public Result register(@Validated @RequestBody RegisterDto registerDto) {
        userService.save(new User().setName(registerDto.getName()).setPassword(SecureUtil.md5(registerDto.getPassword())).setPhone(registerDto.getPhone()).setSex(3).setIcon("https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png"));
        return Result.succ("注册成功");
    }
}
