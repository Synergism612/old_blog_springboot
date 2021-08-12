package com.project.blog.packag;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.blog.entity.Comment;
import com.project.blog.entity.Tag;
import com.project.blog.entity.Type;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class BlogArticle {

    private Long id;

    private String cover;

    private String title;

    private String summarize;

    private String content;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime lasttime;

    private String author;

    private Integer ViewsNumber;



    private List<Comment> commentList;

    private List<Tag> tagList;

    private List<Type> typeList;

    private List<String> likeNameList;

}
