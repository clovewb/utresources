package com.leaves.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Slideshow;
import com.leaves.entity.WbeParameter;
import com.leaves.service.SlideshowService;
import com.leaves.service.WbeParameterService;
import com.leaves.util.IntegrateUtils;
import com.leaves.util.Result;
import com.leaves.util.ResultResponse;
import com.leaves.util.TableResultResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * @date: 2020 年 05月 07日 上午 09:23:51
 * @version: v1.0.0
 * @Description: 网站设置相关接口
 *
 */
@RequestMapping("/wbeSet")
@Controller
public class WebSiteSetUpController {
    private static final transient Logger log = LoggerFactory.getLogger(WebSiteSetUpController.class);

    @Autowired
    private SlideshowService slideshowService;

    @Autowired
    private WbeParameterService wbeParameterService;

    /**
     * 轮播图设置页面跳转接口
     *
     * @param mv
     * @return
     */
    @GetMapping("/slideshow.do")
    public ModelAndView slideshow(ModelAndView mv) {
        mv.setViewName("/wbeSet/slideshow/index");
        return mv;
    }

    /**
     * 轮播图表格加载接口
     *
     * @param slideshow 轮播图实体
     * @param page      页数
     * @param limit     每页条数
     * @return
     */
    @ResponseBody
    @GetMapping("slideshowTable.do")
    public TableResultResponse slideshowTable(Slideshow slideshow, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Slideshow> pageInfo = slideshowService.selectPage(slideshow, page, limit);
        for (Slideshow record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("imgUrl", record.getImgUrl());
            if (IntegrateUtils.stringIsNotBlack(record.getImgUrl())) {
                resultMap.put("lookPhoto", "  <a href='" + record.getImgUrl() + "' style='color:#40AFFE ' target='_blank'>查看图片</a>");
            } else {
                resultMap.put("lookPhoto", "暂无图片");
            }
            resultMap.put("creationUser", record.getCreationUser());
            resultMap.put("creationTime", record.getCreationTime() == null ? "" : record.getCreationTime().substring(0, 19));
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 新增轮播图页面跳转接口
     *
     * @param mv
     * @return
     */
    @GetMapping("/addSlideshow.do")
    public ModelAndView addSlideshow(ModelAndView mv) {
        mv.setViewName("/wbeSet/slideshow/addSlideshow");
        return mv;
    }

    /**
     * 新增轮播图接口
     *
     * @param slideshow 轮播图实体
     * @return
     */
    @ResponseBody
    @PostMapping("/slideshow.do")
    public ResultResponse insertSlideshow(Slideshow slideshow) {
        boolean result = slideshowService.insert(slideshow);
        if (!result) {
            return Result.resuleError("操作失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除轮播图
     *
     * @param id 轮播图id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/slideshow.do")
    public ResultResponse deleteSlideshow(String id) {
        boolean result = slideshowService.delById(id);
        if (!result) {
            return Result.resuleError("操作失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 网站参数设置页面跳转接口
     *
     * @param mv
     * @return
     */
    @GetMapping("/wbeParameter.do")
    public ModelAndView wbeParameter(ModelAndView mv) {
        WbeParameter wbeParameter = wbeParameterService.getWbeParameter();
        mv.addObject("wbeParameter", wbeParameter);
        mv.setViewName("/wbeSet/wbeParameter/index");
        return mv;
    }

    /**
     * 修改网站参数的接口
     *
     * @param wbeParameter
     * @return
     */
    @ResponseBody
    @PutMapping("/wbeParameter.do")
    public ResultResponse editWbeParameter(WbeParameter wbeParameter) {
        boolean result = wbeParameterService.editById(wbeParameter);
        if (!result) {
            return Result.resuleError("操作失败");
        }
        return Result.resuleSuccess();
    }

}
