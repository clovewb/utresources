package com.leaves.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.NewsCategory;

import java.util.List;

public interface NewsCategoryService {
    /**
     * 分页查询
     *
     * @param newsCategory
     * @param page
     * @param limit
     * @return
     */
    Page<NewsCategory> selectPage(NewsCategory newsCategory, int page, int limit);

    /**
     * 新增
     *
     * @param newsCategory
     * @return
     */
    boolean insert(NewsCategory newsCategory);

    /**
     * 删除
     */
    boolean delById(String id);

    /**
     * 得到新闻分类集合
     *
     * @return
     */
    List<NewsCategory> selectList();


}
