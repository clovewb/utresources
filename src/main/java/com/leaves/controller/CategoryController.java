package com.leaves.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Category;
import com.leaves.entity.ChdCategory;
import com.leaves.entity.Plate;
import com.leaves.service.CategoryService;
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
 * @date: 2020年5月3日 上午09:31:13
 * @version: v1.0.0
 * @Description: 视频分类实现接口
 *
 */
@Controller
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 视频分类管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView enIndex(ModelAndView mv) {
        mv.setViewName("category/index");
        return mv;
    }

    /**
     * 视频分类列表加载接口
     *
     * @param category
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("categoryTable.do")
    public TableResultResponse classifyTable(Category category, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Category> pageInfo = categoryService.selectPage(category, page, limit);
        for (Category record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            String chdName = "";
            //得到所有子分类
            List<ChdCategory> chdCategorys = categoryService.selectChdCategoryList(record.getId());
            for (ChdCategory chdCategory : chdCategorys) {
                //拿到子分类名称循环添加到chdName,用空格隔开
                chdName += "&nbsp;&nbsp;" + chdCategory.getName();
            }
            resultMap.put("chdName", chdName);
            resultMap.put("name", record.getName());
            resultMap.put("plate", categoryService.getByCategoryId(record.getId()));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 删除视频分类
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/category.do")
    public ResultResponse delCategory(String id) {
        boolean result = categoryService.delById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 修改板块信息
     *
     * @param plate
     * @return
     */
    @ResponseBody
    @PutMapping("/plate.do")
    public ResultResponse editPlate(Plate plate) {
        boolean result = categoryService.editPlate(plate);
        if (!result) {
            return Result.resuleError("修改失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 新增视频分类界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addCategory.do")
    public ModelAndView addCategory(ModelAndView mv) {
        mv.setViewName("category/addCategory");
        return mv;
    }

    /**
     * 新增视频分类
     *
     * @param category
     * @return
     */
    @ResponseBody
    @PostMapping("/category.do")
    public ResultResponse insertCategory(Category category) {
        boolean result = categoryService.insert(category);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 新增视频子分类界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addChdCategory.do")
    public ModelAndView addChdCategory(ModelAndView mv, String ptId) {
        mv.addObject("ptId", ptId);
        mv.setViewName("category/addChdCategory");
        return mv;
    }

    /**
     * 新增视频子分类
     *
     * @param chdCategory
     * @return
     */
    @ResponseBody
    @PostMapping("/chdCategory.do")
    public ResultResponse insertChdCategory(ChdCategory chdCategory) {
        boolean result = categoryService.insert(chdCategory);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 查看视频子分类界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/lookChdCategory.do")
    public ModelAndView lookChdCategory(ModelAndView mv, String ptId) {
        List<ChdCategory> chdCategoryList = categoryService.selectChdCategoryList(ptId);
        mv.addObject("chdCategoryList", chdCategoryList);
        mv.setViewName("category/lookChdCategory");
        return mv;
    }

    /**
     * 删除视频子分类分类
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/chdCategory.do")
    public ResultResponse delChdCategory(String id) {
        boolean result = categoryService.deChdCategoryById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 视频子分类
     *
     * @param ptId
     * @return
     */
    @ResponseBody
    @PostMapping("/getChdCategory.do")
    public List<ChdCategory> getChdCategory(String ptId) {
        List<ChdCategory> chdCategoryList = categoryService.selectChdCategoryList(ptId);
        return chdCategoryList;
    }
}