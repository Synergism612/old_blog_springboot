package com.project.blog.common.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class RegisterDto implements Serializable {
    //电话号
    @NotBlank(message = "账号不能为空")
    private String phone;
    //昵称
    @NotBlank(message = "昵称不能为空")
    private String name;
    //密码
    @NotBlank(message = "密码不能为空")
    private String password;
}
