package com.leaves.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.*;
import com.leaves.service.CategoryService;
import com.leaves.service.CommentService;
import com.leaves.service.PostInfoService;
import com.leaves.service.UserService;
import com.leaves.util.IntegrateUtils;
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
 * @date: 2020年5月3日 上午10:14:19
 * @version: v1.0.0
 * @Description: 视频实现接口
 *
 */
@Controller
@RequestMapping("/postInfo")
public class PostInfoController {
    @Autowired
    private PostInfoService postInfoService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserService userService;
    @Autowired
    private IntegrateUtils itdragonUtils;
    @Autowired
    private CommentService commentService;

    /**
     * 管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView enIndex(ModelAndView mv) {
        //获取视频父分类
        List<Category> categoryList = categoryService.selectList();
        mv.addObject("categoryList", categoryList);
        mv.setViewName("postInfo/index");
        return mv;
    }

    /**
     * 评论界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/commentIndex.do")
    public ModelAndView enIndex(ModelAndView mv, String postId) {
        mv.addObject("postId", postId);
        mv.setViewName("postInfo/commentIndex");
        return mv;
    }

    /**
     * 列表加载接口
     *
     * @param comment
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("comment.do")
    public TableResultResponse commentTable(Comment comment, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<Comment> pageInfo = commentService.selectPage(comment, page, limit);
        for (Comment record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("userName", userService.selectByPrimaryKey(record.getUserId())
                    == null ? "用户不存在" : userService.selectByPrimaryKey(record.getUserId()).getUserName());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 19));
            resultMap.put("content", record.getContent());
            resultMap.put("stateType", "1".equals(record.getState()) ? "是" : "否");
            resultMap.put("state", record.getState());
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 列表加载接口
     *
     * @param postInfo
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("postInfoTable.do")
    public TableResultResponse postInfoTable(PostInfo postInfo, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<PostInfo> pageInfo = postInfoService.selectPage(postInfo, page, limit);
        for (PostInfo record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("name", record.getName());
            resultMap.put("createTime", record.getCreateTime() == null ? "" : record.getCreateTime().substring(0, 19));
            resultMap.put("userName", record.getUserName());
            resultMap.put("isTop", record.getIsTop() == 1 ? "是" : "否");
            resultMap.put("state", "1".equals(record.getState()) ? "是" : "否");
            resultMap.put("pageView", record.getPageView());
            resultMap.put("observer", record.getObserver());
            if (IntegrateUtils.stringIsNotBlack(record.getCategoryId()) && IntegrateUtils.stringIsNotBlack(record.getChdCategoryId())) {
                String ChdClassName = categoryService.getChdCategory(record.getChdCategoryId()) == null ? "" : "==>" + categoryService.getChdCategory(record.getChdCategoryId()).getName();
                resultMap.put("classifyName", categoryService.getCategory(record.getCategoryId()) == null ? "" : categoryService.getCategory(record.getCategoryId()).getName() + ChdClassName);
            }
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/postInfo.do")
    public ResultResponse delPostInfo(String id) {
        boolean result = postInfoService.delById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 新增视频界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/addPostInfo.do")
    public ModelAndView addPoatInfo(ModelAndView mv) {
        //获取视频父分类
        List<Category> categoryList = categoryService.selectList();
        mv.addObject("categoryList", categoryList);
        mv.setViewName("postInfo/addPostInfo");
        return mv;
    }

    /**
     * 新增视频
     *
     * @param postInfo
     * @return
     */
    @ResponseBody
    @PostMapping("/postInfo.do")
    public ResultResponse insertProduct(PostInfo postInfo) {
        boolean result = postInfoService.insert(postInfo);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        User user = userService.selectByPrimaryKey(itdragonUtils.getSessionUser().getId());
        user.setPostNum(user.getPostNum() + 1);
        userService.updateByPrimaryKey(user);
        return Result.resuleSuccess();
    }

    /**
     * 编辑视频界面跳转接口
     *
     * @param mv
     * @return
     */
    @RequestMapping("/editPostInfo.do")
    public ModelAndView editPostInfo(ModelAndView mv, String id) {
        //获取视频父分类
        List<Category> categoryList = categoryService.selectList();
        mv.addObject("categoryList", categoryList);
        //获取子分类
        List<ChdCategory> chdClassifyList = categoryService.selectChdCategoryList();
        mv.addObject("chdClassifyList", chdClassifyList);
        PostInfo postInfo = postInfoService.getOne(id);
        mv.addObject("postInfo", postInfo);
        mv.setViewName("postInfo/editPostInfo");
        return mv;
    }

    /**
     * 编辑
     *
     * @param postInfo
     * @return
     */
    @ResponseBody
    @PutMapping("/postInfo.do")
    public ResultResponse updataPostInfo(PostInfo postInfo) {
        boolean result = postInfoService.editById(postInfo);
        if (!result) {
            return Result.resuleError("编辑失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 编辑
     *
     * @param comment
     * @return
     */
    @ResponseBody
    @PutMapping("/comment.do")
    public ResultResponse updataComment(Comment comment) {
        boolean result = commentService.update(comment);
        if (!result) {
            return Result.resuleError("操作失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/comment.do")
    public ResultResponse delComment(String id) {
        boolean result = commentService.delById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }
}