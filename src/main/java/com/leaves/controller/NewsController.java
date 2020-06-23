package com.leaves.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.News;
import com.leaves.entity.NewsCategory;
import com.leaves.service.NewsCategoryService;
import com.leaves.service.NewsService;
import com.leaves.util.Result;
import com.leaves.util.ResultResponse;
import com.leaves.util.TableResultResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author: leaves
 * @date: 2020年5月4日 上午09:30:15
 * @version: v1.0.0
 * @Description: 新闻相关实现接口
 *
 */
@Controller
@RequestMapping("/news")
public class NewsController {
    @Autowired
    private NewsCategoryService newsCategoryService;
    @Autowired
    private NewsService newsService;

    /**
     * 新闻分类管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/categoryIndex.do")
    public ModelAndView categoryIndex(ModelAndView mv) {
        mv.setViewName("/operation/news/categoryIndex");
        return mv;
    }

    /**
     * 新闻分类列表加载接口
     *
     * @param newsCategory
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("newsCategoryTable.do")
    public TableResultResponse newsCategoryTable(NewsCategory newsCategory, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<NewsCategory> pageInfo = newsCategoryService.selectPage(newsCategory, page, limit);
        for (NewsCategory record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("name", record.getName());
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 删除新闻分类
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/newsCategory.do")
    public ResultResponse delNewsCategory(String id) {
        boolean result = newsCategoryService.delById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 新增新闻分类界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addNewsCategory.do")
    public ModelAndView addClassify(ModelAndView mv) {
        mv.setViewName("/operation/news/addNewsCategory");
        return mv;
    }

    /**
     * 新增新闻分类
     *
     * @param newsCategory
     * @return
     */
    @ResponseBody
    @PostMapping("/newsCategory.do")
    public ResultResponse insertNewsCategory(NewsCategory newsCategory) {
        boolean result = newsCategoryService.insert(newsCategory);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 新闻发布界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addNews.do")
    public ModelAndView addNews(ModelAndView mv) {
        List<NewsCategory> categories = newsCategoryService.selectList();
        mv.addObject("categories", categories);
        mv.setViewName("/operation/news/addNews");
        return mv;
    }

    /**
     * 新闻发布
     *
     * @param news
     * @return
     */
    @ResponseBody
    @PostMapping("/news.do")
    public ResultResponse insertNews(News news) {
        boolean result = newsService.insert(news);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 新闻管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView index(ModelAndView mv) {
        List<NewsCategory> categories = newsCategoryService.selectList();
        mv.addObject("categories", categories);
        mv.setViewName("/operation/news/index");
        return mv;
    }

    /**
     * 新闻列表加载接口
     *
     * @param news
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("newsTable.do")
    public TableResultResponse newsTable(News news, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<News> pageInfo = newsService.selectPage(news, page, limit);
        for (News record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("name", record.getName());
            resultMap.put("categoryName", record.getCategoryName());
            resultMap.put("content", record.getContent());
            resultMap.put("userName", record.getUserName());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 19));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 删除新闻
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/news.do")
    public ResultResponse delNews(String id) {
        boolean result = newsService.delById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 新闻编辑界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/editNews.do")
    public ModelAndView editNews(ModelAndView mv, String id) {
        News news = newsService.getOneById(id);
        mv.addObject("news", news);
        List<NewsCategory> categories = newsCategoryService.selectList();
        mv.addObject("categories", categories);
        mv.setViewName("/operation/news/editNews");
        return mv;
    }

    /**
     * 修改新闻
     *
     * @param news
     * @return
     */
    @ResponseBody
    @PutMapping("/news.do")
    public ResultResponse updataNews(News news) {
        boolean result = newsService.editById(news);
        if (!result) {
            return Result.resuleError("修改失败");
        }
        return Result.resuleSuccess();
    }

}