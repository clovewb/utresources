package com.leaves.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Comment;
import com.leaves.mapper.CommentMapper;
import com.leaves.mapper.PostInfoMapper;
import com.leaves.service.CommentService;
import com.leaves.util.DateUtil;
import com.leaves.util.IntegrateUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 评论
 *
 * @author
 * @create 2019/2/15 17:53
 * @since 1.0.0
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private IntegrateUtils itdragonUtils;
    @Resource
    private PostInfoMapper postInfoMapper;

    @Override
    public Page<Comment> selectPage(Comment comment, int page, int limit) {
        EntityWrapper<Comment> searchInfo = new EntityWrapper<>();
        if (IntegrateUtils.stringIsNotBlack(comment.getPostId())) {
            searchInfo.like("postId", comment.getPostId());
        }
        Page<Comment> pageInfo = new Page<>(page, limit);
        List<Comment> resultList = commentMapper.selectPage(pageInfo, searchInfo);
        if (!resultList.isEmpty()) {
            pageInfo.setRecords(resultList);
        }
        return pageInfo;
    }

    @Override
    public boolean insert(Comment comment) {
        comment.setUserId(itdragonUtils.getSessionUser().getId());
        comment.setTime(DateUtil.getNowDateSS());
        comment.setState("0");
        comment.setType("未读");
        Integer insert = commentMapper.insert(comment);
        if (insert > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Comment comment) {
        Integer update = commentMapper.updateById(comment);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Comment> getList(String postId) {
        EntityWrapper<Comment> wrapper = new EntityWrapper<>();
        wrapper.eq("postId", postId);
        wrapper.eq("state", "1");
        return commentMapper.selectList(wrapper);
    }

    @Override
    public List<Comment> getList() {
        EntityWrapper<Comment> wrapper = new EntityWrapper<>();
        return commentMapper.selectList(wrapper);
    }

    @Override
    public List<Comment> getListByUserId(String userId) {
        EntityWrapper<Comment> wrapper = new EntityWrapper<>();
        wrapper.eq("userId", userId);
        wrapper.eq("state", "1");
        List<Comment> commentList = commentMapper.selectList(wrapper);
        if (commentList.isEmpty()) {
            return null;
        }
        for (Comment comment : commentList) {
            comment.setPostInfo(postInfoMapper.selectById(comment.getPostId()));
        }
        return commentList;


    }

    @Override
    public boolean delById(String id) {
        Integer delete = commentMapper.deleteById(id);
        if (delete > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Comment getOne(String id) {
        return commentMapper.selectById(id);
    }

    @Override
    public boolean updateType(String postId) {
        EntityWrapper<Comment> wrapper = new EntityWrapper<>();
        wrapper.eq("postId", postId);
        Comment comment = new Comment();
        comment.setType("已读");
        Integer update = commentMapper.update(comment, wrapper);
        if (update > 0) {
            return true;
        }
        return false;
    }

    @Override
    public Integer getCommentNum(String postId) {
        EntityWrapper<Comment> wrapper = new EntityWrapper<>();
        wrapper.eq("postId", postId);
        wrapper.eq("type", "未读");
        List<Comment> comments = commentMapper.selectList(wrapper);
        return comments.size();
    }
}