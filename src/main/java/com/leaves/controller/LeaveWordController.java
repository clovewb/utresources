package com.leaves.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Answering;
import com.leaves.entity.LeaveWord;
import com.leaves.entity.LikeNumber;
import com.leaves.service.LeaveWordService;
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
 * @date: 2020年5月2日 上午9:01:51
 * @version: v1.0.0
 * @Description: 在线留言实现接口
 */
@Controller
@RequestMapping("/leaveWord")
public class LeaveWordController {
    @Autowired
    private LeaveWordService leaveWordService;

    /**
     * 在线留言管理界面跳转
     *
     * @param mv
     * @return
     */
    @RequestMapping("/index.do")
    public ModelAndView leaveWordIndex(ModelAndView mv) {
        mv.setViewName("/operation/leaveWord/index");
        return mv;
    }

    /**
     * 在线留言列表加载接口
     *
     * @param leaveWord
     * @param page
     * @param limit
     * @return
     */
    @ResponseBody
    @GetMapping("leaveWordTable.do")
    public TableResultResponse leaveWordTable(LeaveWord leaveWord, int page, int limit) {
        List<Map<String, Object>> infoList = new ArrayList<>();
        Page<LeaveWord> pageInfo = leaveWordService.selectPage(leaveWord, page, limit);
        for (LeaveWord record : pageInfo.getRecords()) {
            Map<String, Object> resultMap = new HashMap<>(16);
            resultMap.put("id", record.getId());
            resultMap.put("userName", record.getUserName());
            resultMap.put("time", record.getTime() == null ? "" : record.getTime().substring(0, 19));
            resultMap.put("content", record.getContent());
            infoList.add(resultMap);
        }
        return Result.tableResule(pageInfo.getTotal(), infoList);
    }

    /**
     * 删除留言
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/leaveWord.do")
    public ResultResponse delLeaveWord(String id) {
        boolean result = leaveWordService.delById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }


    /**
     * 新增留言
     *
     * @param leaveWord
     * @return
     */
    @ResponseBody
    @PostMapping("/leaveWord.do")
    public ResultResponse insertLeaveWord(LeaveWord leaveWord) {
        boolean result = leaveWordService.insert(leaveWord);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 新增回复
     *
     * @param answering
     * @return
     */
    @ResponseBody
    @PostMapping("/answering.do")
    public ResultResponse insertAnswering(Answering answering) {
        boolean result = leaveWordService.insert(answering);
        if (!result) {
            return Result.resuleError("新增失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 删除回复
     *
     * @param id
     * @return
     */
    @ResponseBody
    @DeleteMapping("/answering.do")
    public ResultResponse delAnswering(String id) {
        boolean result = leaveWordService.delAnsweringById(id);
        if (!result) {
            return Result.resuleError("删除失败");
        }
        return Result.resuleSuccess();
    }

    /**
     * 点赞功能接口
     *
     * @param likeNumber
     * @return
     */
    @ResponseBody
    @PostMapping("/getALike.do")
    public ResultResponse getALike(LikeNumber likeNumber) {
        String result = leaveWordService.giveALike(likeNumber);
        if ("default".equals(result)) {
            return Result.resuleError("系统故障!");
        } else if ("callOff".equals(result)) {
            return Result.resuleSuccess(null, "取消点赞成功");
        }
        return Result.resuleSuccess(null, "点赞成功");
    }

    /**
     * 查看回复功能
     *
     * @param mv
     * @param leaveId
     * @return
     */
    @RequestMapping("/lookReply.do")
    public ModelAndView lookReply(ModelAndView mv, String leaveId) {
        List<Answering> answeringList = leaveWordService.selectListAnswering(leaveId);
        mv.addObject("answeringList", answeringList);
        mv.setViewName("/operation/leaveWord/lookReply");
        return mv;
    }

    /**
     * 后台留言回复页面接口
     */
    @RequestMapping("/reply.do")
    public ModelAndView reply(ModelAndView mv, String leaveId) {
        mv.addObject("leaveId", leaveId);
        mv.setViewName("/operation/leaveWord/reply");
        return mv;
    }
}