package com.project.blog.service.impl;

import com.project.blog.entity.Article;
import com.project.blog.mapper.ArticleMapper;
import com.project.blog.packag.BlogArticle;
import com.project.blog.service.ArticleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Resource
    ArticleMapper articleMapper;

    @Override
    public List<BlogArticle> getArticleList(int currPage, int pageSize) {
        List<BlogArticle> articleList = articleMapper.getArticleList();
        //从第几条数据开始
        int firstIndex = (currPage - 1) * pageSize;
        //到第几条数据结束
        int lastIndex = currPage * pageSize;
        if (articleList.size()<lastIndex){
            lastIndex = articleList.size();
        }
        articleList = articleList.subList(firstIndex, lastIndex);
        return articleList; //直接在list中截取
    }

    @Override
    public BlogArticle getArticleById(Integer articleId) {
        return articleMapper.getArticleById(articleId);
    }

    @Override
    public List<BlogArticle> getArticleListGroupByTime(String time) {
        String startTime = time+"-01 00:0:0";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();;
        try {
            Date date = simpleDateFormat.parse(startTime);
            calendar.setTime(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar.add(Calendar.MONTH, +1);
        String stopTime = simpleDateFormat.format(calendar.getTime());
        return articleMapper.getArticleListGroupByTime(startTime,stopTime);
    }
}
