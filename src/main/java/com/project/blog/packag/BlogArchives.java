package com.project.blog.packag;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BlogArchives {
    LocalDateTime time;
    Integer articleId;

    public BlogArchives setBlogArchives(Integer articleId,LocalDateTime time){
        this.time = time;
        this.articleId = articleId;
        return this;
    }
}
