package com.leaves.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Category;
import com.leaves.entity.ChdCategory;
import com.leaves.entity.Plate;
import com.leaves.mapper.CategoryMapper;
import com.leaves.mapper.ChdCategoryMapper;
import com.leaves.mapper.PlateMapper;
import com.leaves.service.CategoryService;
import com.leaves.util.IntegrateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈视频分类实现接口〉<br>
 *
 * @author
 * @create 2019/2/15 17:53
 * @since 1.0.0
 */
@Service
public class CategoryServiceImpl implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private ChdCategoryMapper chdCategoryMapper;
    @Resource
    private IntegrateUtils itdragonUtils;
    @Resource
    private PlateMapper plateMapper;

    @Override
    public Page<Category> selectPage(Category category, int page, int limit) {
        EntityWrapper<Category> searchInfo = new EntityWrapper<>();
        Page<Category> pageInfo = new Page<>(page, limit);
        List<Category> resultList = categoryMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Category category) {
        Integer insert = categoryMapper.insert(category);
        if (insert > 0) {
            Plate plate = new Plate();
            plate.setCategoryId(category.getId());
            plate.setName(category.getName());
            plateMapper.insert(plate);
            return true;
        }
        return false;
    }

    @Override
    public boolean delById(String id) {
        //删除分类前先要删除给分类下所有的子分类
        EntityWrapper<ChdCategory> wrapper = new EntityWrapper<>();
        wrapper.eq("ptId", id);
        chdCategoryMapper.delete(wrapper);
        //删除分类前先要删除分类的简介
        EntityWrapper<Plate> wrapper1 = new EntityWrapper<>();
        wrapper.eq("categoryId", id);
        plateMapper.delete(wrapper1);
        Integer delete = categoryMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Category> selectList() {
        List<Category> categoryList = categoryMapper.selectList(null);
        if (categoryList.isEmpty()) {
            return new ArrayList<>();
        }
        return categoryList;
    }

    @Override
    public boolean insert(ChdCategory chdCategory) {
        Integer insert = chdCategoryMapper.insert(chdCategory);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deChdCategoryById(String id) {
        Integer delete = chdCategoryMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<ChdCategory> selectChdCategoryList(String ptId) {
        EntityWrapper<ChdCategory> wrapper = new EntityWrapper<>();
        wrapper.eq("ptId", ptId);
        List<ChdCategory> chdCategoryList = chdCategoryMapper.selectList(wrapper);
        if (chdCategoryList.isEmpty()) {
            return new ArrayList<>();
        }
        return chdCategoryList;
    }

    @Override
    public List<ChdCategory> selectChdCategoryList() {
        return chdCategoryMapper.selectList(null);
    }

    @Override
    public Category getCategory(String id) {
        return categoryMapper.selectById(id);
    }

    @Override
    public ChdCategory getChdCategory(String id) {
        return chdCategoryMapper.selectById(id);
    }

    @Override
    public Plate getByCategoryId(String categoryId) {
        EntityWrapper<Plate> wrapper = new EntityWrapper<>();
        wrapper.eq("categoryId", categoryId);
        List<Plate> plates = plateMapper.selectList(wrapper);
        if (plates.isEmpty()) {
            return new Plate();
        }
        return plates.get(0);
    }

    @Override
    public boolean editPlate(Plate plate) {
        Integer update = plateMapper.updateById(plate);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean isJoin(String userName, String id) {
        Plate plate = plateMapper.selectById(id);
        if (plate.getMember() == null) {
            return false;
        }
        if (plate.getMember().indexOf(userName) != -1) {
            return true;
        }
        return false;
    }

    @Override
    public boolean join(String userName, String id) {
        Plate plate = plateMapper.selectById(id);
        if (IntegrateUtils.stringIsNotBlack(plate.getMember())) {
            plate.setMember(plate.getMember() + "," + userName);
        } else {
            plate.setMember(userName);
        }
        Integer update = plateMapper.updateById(plate);
        if (update > 0) {
            return true;
        }
        return false;
    }
}