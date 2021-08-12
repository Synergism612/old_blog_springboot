package com.project.blog.common.dto;

import lombok.Data;

import java.util.Date;

@Data
public class MessageAdd {
    String userPhone;
    String content;
    Date time;
}
