package com.project.blog.mapper;

import com.project.blog.entity.Comment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.blog.packag.BlogComment;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 神和五律
 * @since 2021-05-25
 */
public interface CommentMapper extends BaseMapper<Comment> {
    List<BlogComment> getBlogIndexCommentList();
    List<BlogComment> getBlogIndexCommentListByArticleId(Integer articleId);
}
