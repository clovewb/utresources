package com.leaves.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.News;
import com.leaves.entity.NewsCategory;
import com.leaves.mapper.ChdClassifyMapper;
import com.leaves.mapper.NewsCategoryMapper;
import com.leaves.mapper.NewsMapper;
import com.leaves.service.NewsCategoryService;
import com.leaves.util.IntegrateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈新闻分类实现接口〉<br>
 *
 * @author
 * @create 2019/2/15 17:53
 * @since 1.0.0
 */
@Service
public class NewsCategoryServiceImpl implements NewsCategoryService {
    @Resource
    private NewsCategoryMapper newsCategoryMapper;
    @Resource
    private ChdClassifyMapper chdClassifyMapper;
    @Resource
    private IntegrateUtils itdragonUtils;
    @Resource
    private NewsMapper newsMapper;

    @Override
    public Page<NewsCategory> selectPage(NewsCategory newsCategory, int page, int limit) {
        EntityWrapper<NewsCategory> searchInfo = new EntityWrapper<>();
        Page<NewsCategory> pageInfo = new Page<>(page, limit);
        List<NewsCategory> resultList = newsCategoryMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(NewsCategory newsCategory) {
        Integer insert = newsCategoryMapper.insert(newsCategory);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delById(String id) {
        //删除分类前先要删除该分类下所有的新闻
        EntityWrapper<News> wrapper = new EntityWrapper<>();
        wrapper.eq("categoryId", id);
        newsMapper.delete(wrapper);
        Integer delete = newsCategoryMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<NewsCategory> selectList() {
        List<NewsCategory> newsCategoryList = newsCategoryMapper.selectList(null);
        if (newsCategoryList.isEmpty()) {
            return new ArrayList<>();
        }
        return newsCategoryList;
    }


}