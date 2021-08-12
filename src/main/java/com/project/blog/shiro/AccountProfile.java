package com.project.blog.shiro;

import lombok.Data;

import java.io.Serializable;

@Data
public class AccountProfile implements Serializable {
    //用户id
    private Long id;
    //用户名
    private String name;
    //头像
    private String Icons;
    //邮箱
    private String email;

}