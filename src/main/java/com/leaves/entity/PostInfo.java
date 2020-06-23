package com.leaves.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 视频实体类
 *
 * @author leaves
 */
@Data
@TableName("gm_postInfo")
public class PostInfo implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 视频名称
     */
    private String name;
    /**
     * 内容
     */
    private String content;
    /**
     * 介绍
     */
    private String introduce;
    /**
     * 浏览数
     */
    private Integer pageView;
    /**
     * 评论数
     */
    private Integer observer;

    /**
     * 是否置顶
     */
    private Integer isTop;
    /**
     * 创建时间
     */
    private String createTime;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 用户名
     */
    private String userName;
    /**
     * 父类id
     */
    private String categoryId;
    /**
     * 子类id
     */
    private String chdCategoryId;
    /**
     * 子类name
     */
    private String chdCategoryName;
    private String state;
    private String img;
    private String url;
    private String faculty;
    private String cag;
    /**
     * 排序参数
     */
    @TableField(exist = false)
    private String orderParam;
    @TableField(exist = false)
    private String searchParam;
    @TableField(exist = false)
    private String isMe;
    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private Integer likeCount;
    @TableField(exist = false)
    private Integer commentNoCount;


}