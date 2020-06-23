package com.leaves.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Slideshow;

import java.util.List;

public interface SlideshowService {
    /**
     * 分页查询
     *
     * @param Slideshow
     * @param page
     * @param limit
     * @return
     */
    Page<Slideshow> selectPage(Slideshow Slideshow, int page, int limit);

    /**
     * 新增
     *
     * @param slideshow
     * @return
     */
    boolean insert(Slideshow slideshow);

    /**
     * 删除
     */
    boolean delById(String id);

    /**
     * 得到轮播图集合
     * @return
     */
    List<Slideshow> getList();


}
