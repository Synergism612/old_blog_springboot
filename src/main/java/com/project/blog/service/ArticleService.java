package com.project.blog.service;

import com.project.blog.entity.Article;
import com.baomidou.mybatisplus.extension.service.IService;
import com.project.blog.packag.BlogArticle;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 神和五律
 * @since 2021-05-25
 */
public interface ArticleService extends IService<Article> {
    List<BlogArticle> getArticleList(int currPage, int pageSize);

    BlogArticle getArticleById(Integer articleId);

    List<BlogArticle> getArticleListGroupByTime(String time);
}
