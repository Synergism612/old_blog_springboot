package com.project.blog.controller;


import com.baomidou.mybatisplus.core.conditions.AbstractWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.blog.common.dto.CommentAdd;
import com.project.blog.common.dto.MessageAdd;
import com.project.blog.entity.Comment;
import com.project.blog.entity.Message;
import com.project.blog.entity.User;
import com.project.blog.lang.Result;
import com.project.blog.service.*;
import com.project.blog.shiro.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 神和五律
 * @since 2021-05-25
 */

@RestController
@RequestMapping("/api")
public class SaveController {

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

    @Autowired
    JwtUtils jwtUtils;  //jwt工具

    @PostMapping("/comment")
    public Result addComment(@RequestBody CommentAdd commentAdd) {
        //时间格式化
        LocalDateTime time = commentAdd.getTime().toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
        //获得用户id
        Integer authorId = userService.getOne(new QueryWrapper<User>().eq("phone", commentAdd.getUserPhone())).getId().intValue();
        //添加评论
        commentService.save(new Comment().setArticleId(commentAdd.getArticleId()).setContent(commentAdd.getContent()).setTime(time).setAuthorId(authorId));
        return Result.succ("添加成功");
    }

    @PostMapping("/message")
    public Result addMessage(@RequestBody MessageAdd messageAdd) {
        //时间格式化
        LocalDateTime time = messageAdd.getTime().toInstant().atOffset(ZoneOffset.of("+8")).toLocalDateTime();
        //获得用户id
        Integer authorId = userService.getOne(new QueryWrapper<User>().eq("phone", messageAdd.getUserPhone())).getId().intValue();
        //添加留言
        messageService.save(new Message().setAuthorId(authorId).setContent(messageAdd.getContent()).setTime(time));
        return Result.succ("添加成功");
    }
}
