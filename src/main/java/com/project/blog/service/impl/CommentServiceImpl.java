package com.project.blog.service.impl;

import com.project.blog.entity.Comment;
import com.project.blog.mapper.CommentMapper;
import com.project.blog.packag.BlogComment;
import com.project.blog.service.CommentService;
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
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Resource
    CommentMapper commentMapper;

    @Override
    public List<BlogComment> getBlogIndexCommentList() {
        return commentMapper.getBlogIndexCommentList();
    }

    @Override
    public List<BlogComment> getBlogIndexCommentListByArticleId(Integer id) {
        return commentMapper.getBlogIndexCommentListByArticleId(id);
    }
}
