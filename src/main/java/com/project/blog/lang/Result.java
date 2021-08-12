package com.project.blog.lang;

import lombok.Data;
import java.io.Serializable;

@Data
public class Result implements Serializable {

    private int code;  //是否成功，200为正常，非200为异常
    private String msg;  //结果消息
    private Object data;  //结果数据

    //成功方法
    public static Result succ(Object data){
        //重载成功函数
        //一般成功时，code都是200，也就是成功，所以设置定值
        return succ(200, "操作成功", data);
    }

    public static Result succ(int code,String msg,Object data){
        //成功时调用
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    //失败方法
    public static Result fail(String msg){
        //重载失败函数再封装
        //一些报错情况下，data是没有返回值的，所以我们设置为null
        return fail(400, msg, null);
    }

    public static Result fail(String msg,Object data){
        //重载失败函数
        //一些报错情况下，code没有意义，所以我们设置定值400
        return fail(400, msg, data);
    }

    public static Result fail(int code,String msg,Object data){
        //失败时调用
        Result r = new Result();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}