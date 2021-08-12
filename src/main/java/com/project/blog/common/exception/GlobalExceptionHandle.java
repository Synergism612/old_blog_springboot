package com.project.blog.common.exception;

import com.project.blog.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j  //日志注入
@RestControllerAdvice  //全局的异步的控制类
public class GlobalExceptionHandle  {

    //为前端返回 请求发生错误
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //捕获RuntimeException 运行时的异常
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException e){
        log.error("运行时异常：---------------{}",e);
        return Result.fail(e.getMessage());
    }

    //发生shiro异常主要是登录不成功，为前端返回没有权限的错误
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    //捕获ShiroException shiro发生的异常
    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException e){
        log.error("shiro发生异常：---------------{}",e);
        //没有权限的错误代码一般为401
        return Result.fail(401,e.getMessage(),null);
    }

    //实体数据校验发生异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //MethodArgumentNotValidException 实体校验时发生的异常
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException e){
        log.error("实体校验发生异常：---------------{}",e);
        //对冗杂的报告进行处理
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();
        return Result.fail(objectError.getDefaultMessage());
    }

    //登录出现异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    //IllegalArgumentException 登录时发生的异常
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException e){
        log.error("数据异常：---------------{}",e);
        return Result.fail(e.getMessage());
    }
}