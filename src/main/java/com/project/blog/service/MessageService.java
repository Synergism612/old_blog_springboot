package com.project.blog.service;

import com.project.blog.entity.Message;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.blog.packag.BlogComment;
import com.project.blog.packag.BlogMessage;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 神和五律
 * @since 2021-05-25
 */
public interface MessageService extends IService<Message> {
    List<BlogMessage> getBlogMessageList();
}
