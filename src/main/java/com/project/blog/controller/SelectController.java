package com.project.blog.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.project.blog.entity.*;
import com.project.blog.lang.Result;
import com.project.blog.packag.*;
import com.project.blog.service.*;
import com.project.blog.shiro.JwtUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 神和五律
 * @since 2021-05-25
 */
@RestController
@RequestMapping("/api")
public class SelectController {

    @Autowired
    private ArticleService articleService;
    @Autowired
    private AboutmeService aboutmeService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private VersionService versionService;
    @Autowired
    private TagService tagService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private MessageService messageService;
    @Autowired
    private AboutindexService aboutindexService;
    @Autowired
    private TimelineService timelineService;
    @Autowired
    private FriendlylinkService friendlylinkService;
    @Autowired
    private UserService userService;

    @Autowired
    JwtUtils jwtUtils;  //jwt工具

    @GetMapping("/index")
    public Result blogIndex(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize) {
        //结果map
        Map<String, Object> blogIndexMap = new HashMap();
        //得到关于文章的所有数据，并分页
        blogIndexMap.put("articleList", articleService.getArticleList(currentPage, pageSize));
        //得到文章总条数
        blogIndexMap.put("total", articleService.list().size());
        //得到关于我的数据
        blogIndexMap.put("aboutmeList", aboutmeService.list());
        //得到评论数据
        blogIndexMap.put("commentList", commentService.getBlogIndexCommentList());
        //得到版本信息
        blogIndexMap.put("versionList", versionService.list(new QueryWrapper<Version>().orderByDesc("time")));
        //得到友情链接信息
        blogIndexMap.put("friendlylinkList", friendlylinkService.list());
        //网站信息
        Map<String, Object> aboutIndexList = new HashMap();
        //文章总数
        aboutIndexList.put("articleAmount", articleService.list().size());
        //分类总数
        aboutIndexList.put("typeAmount", typeService.list(new QueryWrapper<Type>().groupBy("content")).size());
        //标签总数
        aboutIndexList.put("tagAmount", tagService.list(new QueryWrapper<Tag>().groupBy("name")).size());
        //留言总数
        aboutIndexList.put("messageAmount", messageService.list().size());
        //评论总数
        aboutIndexList.put("commentAmount", commentService.list().size());
        //得到网站公告
        aboutIndexList.put("indexNotic", aboutindexService.getById(1).getNotice());
        //封装网站信息
        blogIndexMap.put("aboutIndexList", aboutIndexList);
        return Result.succ(blogIndexMap);
    }

    @GetMapping("/category")
    public Result blogCategory(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer pageSize) {
        //结果map
        Map<String, Object> blogCategoryMap = new HashMap();

        //得到关于文章的所有数据，并分页
        blogCategoryMap.put("articleList", articleService.getArticleList(currentPage, pageSize));

        //得到关于类别的所有数据
        blogCategoryMap.put("typeList", typeService.list(new QueryWrapper<Type>().groupBy("content")));

        //得到关于标签的所有数据
        blogCategoryMap.put("tagList", tagService.list(new QueryWrapper<Tag>().groupBy("name")));

        //得到文章总条数
        blogCategoryMap.put("total", articleService.list().size());
        return Result.succ(blogCategoryMap);
    }

    @GetMapping("/search/type")
    public Result blogTypeSearch(@RequestParam String typeContent) {
        //结果map
        Map<String, Object> blogTypeSearchMap = new HashMap();
        //获得关于分类的搜索结果
        List<Type> typeList = typeService.list(new QueryWrapper<Type>().select("articleId").like("content", typeContent));
        List<BlogArticle> searchArticleList = new ArrayList<>();
        for (int i = 0; i < typeList.size(); i++) {
            BlogArticle article = articleService.getArticleById(typeList.get(i).getArticleId());
            searchArticleList.add(article);
        }
        blogTypeSearchMap.put("searchArticleList", searchArticleList);

        //获得文章数
        blogTypeSearchMap.put("total", searchArticleList.size());
        return Result.succ(blogTypeSearchMap);
    }

    @GetMapping("/search/tag")
    public Result blogTagSearch(@RequestParam String tagName) {
        //结果map
        Map<String, Object> blogTagSearchMap = new HashMap();
        //获取关于标签的搜索结果
        List<Tag> tagList = tagService.list(new QueryWrapper<Tag>().select("articleId").like("name", tagName));
        List<BlogArticle> searchArticleList = new ArrayList<>();
        for (int i = 0; i < tagList.size(); i++) {
            BlogArticle article = articleService.getArticleById(tagList.get(i).getArticleId());
            searchArticleList.add(article);
        }
        blogTagSearchMap.put("searchArticleList", searchArticleList);
        //获得文章数
        blogTagSearchMap.put("total", searchArticleList.size());

        return Result.succ(blogTagSearchMap);
    }

    @GetMapping("/search")
    public Result blogSearch(@RequestParam String searchContent) {
        //结果map
        Map<String, Object> blogSearchMap = new HashMap();

        if (searchContent.isEmpty() || searchContent == null || searchContent == "") {
            //得到关于文章的所有数据，并分页
            blogSearchMap.put("searchArticleList", articleService.getArticleList(1, 10));

            //得到文章总条数
            blogSearchMap.put("total", articleService.list().size());

            return Result.succ(blogSearchMap);
        } else {
            //获取关于文章所有的搜索结果的文章id
            List<Tag> tagList = tagService.list(new QueryWrapper<Tag>().select("articleId").like("name", "%" + searchContent + "%"));
            List<Type> typeList = typeService.list(new QueryWrapper<Type>().select("articleId").like("content", "%" + searchContent + "%"));
            List<Article> articleTitleList = articleService.list(new QueryWrapper<Article>().select("id").like("title", "%" + searchContent + "%"));
            List<Article> articleSummarizeList = articleService.list(new QueryWrapper<Article>().select("id").like("summarize", "%" + searchContent + "%"));
            // List<Article> articleAuthorList = articleService.list(new QueryWrapper<Article>().select("id").like("author","%" + searchContent + "%"));

            //根据id搜索文章
            List<Integer> ArticleId = new ArrayList<Integer>();
            for (int i = 0; i < tagList.size(); i++) {
                if (!ArticleId.contains(tagList.get(i).getArticleId()))
                    ArticleId.add(tagList.get(i).getArticleId());
            }
            for (int i = 0; i < typeList.size(); i++) {
                if (!ArticleId.contains(typeList.get(i).getArticleId()))
                    ArticleId.add(typeList.get(i).getArticleId());
            }
            for (int i = 0; i < articleTitleList.size(); i++) {
                if (!ArticleId.contains(articleTitleList.get(i).getId().intValue()))
                    ArticleId.add(articleTitleList.get(i).getId().intValue());
            }
            for (int i = 0; i < articleSummarizeList.size(); i++) {
                if (!ArticleId.contains(articleSummarizeList.get(i).getId().intValue()))
                    ArticleId.add(articleSummarizeList.get(i).getId().intValue());
            }
            //for (int i = 0; i<articleAuthorList.size();i++){
            //ArticleId.add(articleAuthorList.get(i).getId().intValue());
            //}
            List<BlogArticle> searchArticleList = new ArrayList<>();
            for (int i = 0; i < ArticleId.size(); i++) {
                BlogArticle article = articleService.getArticleById(ArticleId.get(i));
                searchArticleList.add(article);
            }
            blogSearchMap.put("searchArticleList", searchArticleList);
            //获得文章数
            blogSearchMap.put("total", searchArticleList.size());

            return Result.succ(blogSearchMap);
        }
    }

    @GetMapping("/article/{id}")
    public Result blogArticleDetail(@PathVariable("id") Integer articleId) {
        //结果map
        Map<String, Object> blogDetailMap = new HashMap();
        //获取文章数据
        blogDetailMap.put("blogArticleDetail", articleService.getArticleById((Integer) articleId));

        //获取文章评论消息
        blogDetailMap.put("blogDetailCommentList", commentService.getBlogIndexCommentListByArticleId(articleId));

        return Result.succ(blogDetailMap);
    }

    @GetMapping("/archives")
    public Result blogArchives() {
        //结果map
        Map<String, Object> blogArchivesMap = new HashMap();

        //获取文章创建日期列表
        List<BlogArchives> archives = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        for (int i = 0; i < articleService.list().size(); i++) {
            archives.add(new BlogArchives().setBlogArchives(articleService.list(new QueryWrapper<Article>().select("id").orderByDesc("time")).get(i).getId().intValue(), articleService.list(new QueryWrapper<Article>().select("time").orderByDesc("time")).get(i).getTime()));
        }
        List<String> dateTimes = new ArrayList<>();
        if (dateTimes.size() == 0) {
            dateTimes.add(archives.get(0).getTime().format(formatter));
        }
        for (int i = 0; i < archives.size(); i++) {
            if (dateTimes.contains(archives.get(i).getTime().format(formatter))) {
                continue;
            } else {
                dateTimes.add(archives.get(i).getTime().format(formatter));
            }
        }
        blogArchivesMap.put("dateTimes", dateTimes);

        //根据分类的时间查询
        List<Object> archiveArticleList = new ArrayList<>();
        for (String s : dateTimes) {
            archiveArticleList.add(articleService.getArticleListGroupByTime(s));
        }
        blogArchivesMap.put("archiveArticleList", archiveArticleList);

        return Result.succ(blogArchivesMap);
    }

    @GetMapping("/timeline")
    public Result blogTimeline() {
        //结果map
        Map<String, Object> blogTimelineMap = new HashMap();
        //查询时间线的数据
        blogTimelineMap.put("timelineList", timelineService.list(new QueryWrapper<Timeline>().orderByDesc("time")));

        return Result.succ(blogTimelineMap);
    }

    @GetMapping("/about")
    public Result blogAbout() {
        //结果map
        Map<String, Object> blogAboutMap = new HashMap();
        //查询关于网站的信息
        blogAboutMap.put("aboutIndex", aboutindexService.getById(1));
        //查询关于我的信息
        blogAboutMap.put("aboutMe", aboutmeService.getById(1));
        //查询留言
        blogAboutMap.put("messageList", messageService.getBlogMessageList());
        return Result.succ(blogAboutMap);
    }

    @GetMapping("/user/{phone}")
    public Result blogUser(@PathVariable String phone){
        //结果map
        Map<String, Object> blogUserMap = new HashMap();
        //查询关于用户的信息
        User user = userService.getOne(new QueryWrapper<User>().eq("phone", phone));
        user.setPhone(user.getPhone().substring(0, 3)+"****"+user.getPhone().substring(7));
        blogUserMap.put("name",user.getName());
        blogUserMap.put("phone",user.getPhone());
        blogUserMap.put("sex",user.getSex());
        blogUserMap.put("email",user.getEmail());
        blogUserMap.put("intro",user.getIntro());
        blogUserMap.put("birthday",DateTimeFormatter.ofPattern("yyyy 年 MM 月 dd 日").format(user.getBirthday()));
        return Result.succ(blogUserMap);
    }

    @GetMapping("/admin")
    @RequiresAuthentication
    public Result blogAdmin(){
        return Result.succ("admin成功");
    }
}

