package com.leaves.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.News;
import com.leaves.mapper.NewsMapper;
import com.leaves.service.NewsService;
import com.leaves.util.DateUtil;
import com.leaves.util.IntegrateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 〈新闻相关操作service实现类〉<br>
 *
 * @author
 * @create 2019/8/15 17:53
 * @since 1.0.0
 */
@Service
public class NewsServiceImpl implements NewsService {
    @Resource
    private NewsMapper newsMapper;
    @Resource
    private IntegrateUtils itdragonUtils;

    @Override
    public Page<News> selectPage(News news, int page, int limit) {
        EntityWrapper<News> searchInfo = new EntityWrapper<>();
        Page<News> pageInfo = new Page<>(page, limit);
        if (IntegrateUtils.stringIsNotBlack(news.getCategoryName())) {
            searchInfo.eq("categoryName", news.getCategoryName());
        }
        if (IntegrateUtils.stringIsNotBlack(news.getCategoryId())) {
            searchInfo.eq("categoryId", news.getCategoryId());
        }
        if (IntegrateUtils.stringIsNotBlack(news.getUserName())) {
            searchInfo.eq("userName", news.getUserName());
        }
        if (IntegrateUtils.stringIsNotBlack(news.getName())) {
            searchInfo.like("name", news.getName());
        }
        searchInfo.orderBy("time desc");
        List<News> resultList = newsMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(News news) {
        news.setTime(DateUtil.getNowDateSS());
        String[] category = news.getCategoryId().split(",");
        news.setCategoryId(category[0]);
        news.setCategoryName(category[1]);
        news.setUserName(itdragonUtils.getSessionUser().getUserName());
        Integer insert = newsMapper.insert(news);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delById(String id) {
        Integer delete = newsMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean editById(News news) {
        String[] category = news.getCategoryId().split(",");
        news.setCategoryId(category[0]);
        news.setCategoryName(category[1]);
        Integer integer = newsMapper.updateById(news);
        if (integer > 0) {
            return true;
        }
        return false;
    }

    @Override
    public News getOneById(String id) {
        return newsMapper.selectById(id);

    }


}