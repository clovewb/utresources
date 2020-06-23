package com.leaves.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.leaves.entity.Comment;

import java.util.List;

/**
 * 评论
 */
public interface CommentService {
    /**
     * 分页查询
     *
     * @param comment
     * @param page
     * @param limit
     * @return
     */
    Page<Comment> selectPage(Comment comment, int page, int limit);
    /**
     * 新增
     *
     * @param comment
     * @return
     */
    boolean insert(Comment comment);

    boolean update(Comment comment);

    List<Comment> getList(String postId);
    List<Comment> getList();
    List<Comment> getListByUserId(String userId);
    /**
     * 删除
     */
    boolean delById(String id);

    Comment getOne(String id);


    boolean updateType(String postId);

    Integer getCommentNum(String postId);

}
