package com.project.blog.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class LoginDto implements Serializable {

    //电话号
    @NotBlank(message = "账号不能为空")
    private String phone;
    //密码
    @NotBlank(message = "密码不能为空")
    private String password;


}
