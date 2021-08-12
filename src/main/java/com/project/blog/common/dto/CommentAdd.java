package com.project.blog.common.dto;

import lombok.Data;
import java.util.Date;

@Data
public class CommentAdd {

    String userPhone;
    String content;
    Integer articleId;
    Date time;
}
