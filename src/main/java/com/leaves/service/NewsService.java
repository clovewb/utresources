package com.leaves.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.News;

public interface NewsService {
    /**
     * 分页查询
     *
     * @param news
     * @param page
     * @param limit
     * @return
     */
    Page<News> selectPage(News news, int page, int limit);

    /**
     * 新增
     *
     * @param news
     * @return
     */
    boolean insert(News news);

    /**
     * 删除
     */
    boolean delById(String id);

    boolean editById(News news);

    News getOneById(String id);


}
