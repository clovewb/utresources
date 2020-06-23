package com.leaves.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Category;
import com.leaves.entity.ChdCategory;
import com.leaves.entity.Plate;

import java.util.List;

/**
 * 类别
 */
public interface CategoryService {
    /**
     * 分页查询
     *
     * @param category
     * @param page
     * @param limit
     * @return
     */
    Page<Category> selectPage(Category category, int page, int limit);

    /**
     * 新增
     *
     * @param category
     * @return
     */
    boolean insert(Category category);

    /**
     * 删除
     */
    boolean delById(String id);

    /**
     * 得到视频分类集合
     *
     * @return
     */
    List<Category> selectList();

    /**
     * 新增子分类
     *
     * @param chdCategory
     * @return
     */
    boolean insert(ChdCategory chdCategory);

    /**
     * 删除子分类
     */
    boolean deChdCategoryById(String id);

    /**
     * 得到视频子分类集合
     *
     * @return
     */
    List<ChdCategory> selectChdCategoryList(String ptId);

    List<ChdCategory> selectChdCategoryList();

    Category getCategory(String id);

    ChdCategory getChdCategory(String id);


    Plate getByCategoryId(String categoryId);

    /**
     * 修改板块信息
     */
    boolean editPlate(Plate plate);

    /**
     * 判断当前用户是否加入
     * @param userName
     * @return
     */
    boolean isJoin(String userName,String id);

    boolean join(String userName,String id);


}
