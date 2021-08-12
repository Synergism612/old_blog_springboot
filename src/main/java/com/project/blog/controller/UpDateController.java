package com.project.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.blog.entity.User;
import com.project.blog.lang.Result;
import com.project.blog.oss.updateOss;
import com.project.blog.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
@RequestMapping("/api")
public class UpDateController {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private AboutmeService aboutmeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private UserService userService;
    @Autowired
    private VersionService versionService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private AboutindexService aboutindexService;
    @Autowired
    private FriendlylinkService friendlylinkService;

    @PatchMapping("/article/{id}/{viewsNumber}")
    public Result addViewsNumber(@PathVariable("id") Integer articleId, @PathVariable("viewsNumber") Integer viewsNumber) {
        //为对应文章的浏览数加一
        articleService.updateById(articleService.getById(articleId).setViewsNumber(viewsNumber + 1));
        return Result.succ("操作成功");
    }

    @PatchMapping("/user/{phone}/icon/{fileName}")
    public Result userUpdate(@PathVariable String phone, @PathVariable String fileName, @RequestBody MultipartFile file) {
        //调用oss存储图片，得到url
        updateOss updateOss = new updateOss();
        String url = updateOss.saveOssImg(file, fileName.substring(fileName.lastIndexOf(".")), phone);

        //存储url到数据库
        userService.updateById(userService.getOne(new QueryWrapper<User>().eq("phone", phone)).setIcon(url));
        return Result.succ(url);
    }

    @PatchMapping("/user/{phone}/name/{name}")
    public Result userUpdatePhone(@PathVariable String phone, @PathVariable String name) {
        userService.updateById(userService.getOne(new QueryWrapper<User>().eq("phone", phone)).setName(name));
        return Result.succ("修改成功");
    }

    @PatchMapping("/user/{phone}/sex/{sex}")
    public Result userUpdateSex(@PathVariable String phone, @PathVariable String sex) {
        switch (sex) {
            case "女":
                userService.updateById(userService.getOne(new QueryWrapper<User>().eq("phone", phone)).setSex(0));
                break;
            case "男":
                userService.updateById(userService.getOne(new QueryWrapper<User>().eq("phone", phone)).setSex(1));
                break;
            default:
                userService.updateById(userService.getOne(new QueryWrapper<User>().eq("phone", phone)).setSex(3));
                break;
        }
        return Result.succ("修改成功");
    }

    @PatchMapping("/user/{phone}/email/{email}")
    public Result userUpdateEmail(@PathVariable String phone, @PathVariable String email) {
        userService.updateById(userService.getOne(new QueryWrapper<User>().eq("phone", phone)).setEmail(email));
        return Result.succ("修改成功");
    }
    @PatchMapping("/user/{phone}/birthday/{birthday}")
    public Result userUpdateBirthday(@PathVariable String phone, @PathVariable String birthday) {
        userService.updateById(userService.getOne(new QueryWrapper<User>().eq("phone", phone)).setBirthday(LocalDateTime.parse(birthday+" 00:00:00",DateTimeFormatter.ofPattern("yyyy 年 MM 月 dd 日 HH:mm:ss"))));
        return Result.succ("修改成功");
    }
    @PatchMapping("/user/{phone}/intro/{intro}")
    public Result userUpdateIntro(@PathVariable String phone, @PathVariable String intro) {
        userService.updateById(userService.getOne(new QueryWrapper<User>().eq("phone", phone)).setIntro(intro));
        return Result.succ("修改成功");
    }
}
