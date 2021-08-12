package com.project.blog.mapper;

import com.project.blog.entity.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.project.blog.packag.BlogArticle;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 神和五律
 * @since 2021-05-25
 */
public interface ArticleMapper extends BaseMapper<Article> {
    List<BlogArticle> getArticleList();

    BlogArticle getArticleById(Integer articleId);

    List<BlogArticle> getArticleListGroupByTime(String startTime,String stopTime);
}
