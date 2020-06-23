package com.leaves.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 评论实体类
 *
 * @author leaves
 */
@Data
@TableName("gm_comment")
public class Comment implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 内容
     */
    private String content;
    /**
     * 评论时间
     */
    private String time;
    /**
     * 评论人
     */
    private String userId;
    /**
     * 评论人user
     */
    @TableField(exist = false)
    private User user;
    private String state;
    private String postId;
    /**
     * 是否楼主
     */
    @TableField(exist = false)
    private String isMain;
    /**
     * 是否管理员
     */
    @TableField(exist = false)
    private String isAdmin;
    /**
     * 是否本人留言
     */
    @TableField(exist = false)
    private String isMe;
    /**
     * 是否本人
     */
    @TableField(exist = false)
    private String canCai;
    /**
     * 视频
     */
    @TableField(exist = false)
    private PostInfo postInfo;
    private String type;
}