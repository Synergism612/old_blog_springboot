package com.project.blog.service.impl;

import com.project.blog.entity.Message;
import com.project.blog.mapper.MessageMapper;
import com.project.blog.packag.BlogComment;
import com.project.blog.packag.BlogMessage;
import com.project.blog.service.MessageService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 神和五律
 * @since 2021-05-25
 */
@Service
public class MessageServiceImpl extends ServiceImpl<MessageMapper, Message> implements MessageService {

    @Resource
    MessageMapper messageMapper;


    @Override
    public List<BlogMessage> getBlogMessageList() {
        return messageMapper.getBlogMessageList();
    }
}
