package com.leaves.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Answering;
import com.leaves.entity.LeaveWord;
import com.leaves.entity.LikeNumber;
import com.leaves.entity.User;
import com.leaves.mapper.AnsweringMapper;
import com.leaves.mapper.LeaveWordMapper;
import com.leaves.mapper.LikeNumberMapper;
import com.leaves.service.LeaveWordService;
import com.leaves.util.DateUtil;
import com.leaves.util.IntegrateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * 〈留言回复实现接口〉<br>
 *
 * @author
 * @create 2019/2/15 17:53
 * @since 1.0.0
 */
@Service
public class LeaveWordServiceImpl implements LeaveWordService {
    @Resource
    private LeaveWordMapper leaveWordMapper;
    @Resource
    private IntegrateUtils itdragonUtils;
    @Resource
    private AnsweringMapper answeringMapper;
    @Resource
    private LikeNumberMapper likeNumberMapper;

    @Override
    public Page<LeaveWord> selectPage(LeaveWord leaveWord, int page, int limit) {
        EntityWrapper<LeaveWord> searchInfo = new EntityWrapper<>();
        Page<LeaveWord> pageInfo = new Page<>(page, limit);
        //留言用户查询
        if (IntegrateUtils.stringIsNotBlack(leaveWord.getUserName())) {
            searchInfo.eq("userName", leaveWord.getUserName());
        }
        //留言时间查询
        if (IntegrateUtils.stringIsNotBlack(leaveWord.getTime())) {
            searchInfo.between("time", leaveWord.getTime().split(" ~ ")[0], leaveWord.getTime().split(" ~ ")[1]);
        }
        searchInfo.orderBy("time desc");
        List<LeaveWord> resultList = leaveWordMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(LeaveWord leaveWord) {
        //获取当前留言用户的信息
        User user = itdragonUtils.getSessionUser();
        leaveWord.setUserName(user.getUserName());
        leaveWord.setUserImg(user.getImgUrl() == null ? "/resource/img/default.jpeg" : user.getImgUrl());
        leaveWord.setTime(DateUtil.getNowDateSS());
        Integer insert = leaveWordMapper.insert(leaveWord);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean delById(String id) {
        //删除留言前先要删除留言下所有的回复
        EntityWrapper<Answering> wrapper = new EntityWrapper<>();
        wrapper.eq("leaveId", id);
        answeringMapper.delete(wrapper);
        Integer delete = leaveWordMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<LeaveWord> selectList() {
        List<LeaveWord> leaveWordList = leaveWordMapper.selectList(null);
        if (leaveWordList.isEmpty()) {
            return new ArrayList<>();
        }
        for (LeaveWord leaveWord : leaveWordList) {
            //判断该留言是不是本人留言的
            if (itdragonUtils.getSessionUser() != null) {
                if (itdragonUtils.getSessionUser().getUserName().equals(leaveWord.getUserName())) {
                    leaveWord.setIsMe("yes");
                } else {
                    leaveWord.setIsMe("no");
                }
            }
        }
        return leaveWordList;
    }

    @Override
    public boolean insert(Answering answering) {
        //获取当前留言回复用户的信息
        User user = itdragonUtils.getSessionUser();
        answering.setUserName(user.getUserName());
        answering.setUserImg(user.getImgUrl() == null ? "/resource/img/default.jpeg" : user.getImgUrl());
        answering.setTime(DateUtil.getNowDateSS());
        Integer insert = answeringMapper.insert(answering);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Answering> selectListAnswering(String leaveId) {
        EntityWrapper<Answering> wrapper = new EntityWrapper<>();
        wrapper.eq("leaveId", leaveId);
        wrapper.orderBy("time desc");
        List<Answering> answeringList = answeringMapper.selectList(wrapper);
        if (answeringList.isEmpty()) {
            return new ArrayList<>();
        }
        for (Answering answering : answeringList) {
            //判断该留言是不是本人留言的
            if (itdragonUtils.getSessionUser().getUserName().equals(answering.getUserName())) {
                answering.setIsMe("yes");
            } else {
                answering.setIsMe("no");
            }
        }
        return answeringList;
    }

    @Override
    public boolean delAnsweringById(String id) {
        Integer delete = answeringMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public String giveALike(LikeNumber likeNumber) {
        //查询当前用户是否对当前留言点过赞
        likeNumber.setUserId(itdragonUtils.getSessionUser().getId());
        LikeNumber selectOne = likeNumberMapper.selectOne(likeNumber);
        String result = "default";
        if (selectOne != null) {
            //如果点过赞,删除用户的点赞记录
            EntityWrapper<LikeNumber> wrapper = new EntityWrapper<>();
            wrapper.eq("userId", likeNumber.getUserId());
            wrapper.eq("leaveId", likeNumber.getLeaveId());
            Integer delete = likeNumberMapper.delete(wrapper);
            if (delete > 0) {
                result = "callOff";
                return result;
            }
        } else {
            //如果没点过,添加一点点赞记录
            Integer insert = likeNumberMapper.insert(likeNumber);
            if (insert > 0) {
                result = "success";
                return result;
            }
        }
        return result;
    }

    @Override
    public Integer getLikeNumber(String leaveId) {
        EntityWrapper<LikeNumber> wrapper = new EntityWrapper<>();
        wrapper.eq("leaveId", leaveId);
        List<LikeNumber> likeNumbers = likeNumberMapper.selectList(wrapper);
        if (likeNumbers.isEmpty()) {
            return 0;
        } else {
            return likeNumbers.size();
        }
    }

    @Override
    public Integer getLeaveWordNumber() {
        List<LeaveWord> leaveWordList = leaveWordMapper.selectList(null);
        return leaveWordList.size();
    }

}