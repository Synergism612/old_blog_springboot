package com.project.blog.mapper;

import com.project.blog.entity.Message;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.blog.packag.BlogComment;
import com.project.blog.packag.BlogMessage;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 神和五律
 * @since 2021-05-25
 */
public interface MessageMapper extends BaseMapper<Message> {
    List<BlogMessage> getBlogMessageList();
}
