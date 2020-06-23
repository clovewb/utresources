package com.leaves.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 留言内容实体类
 *
 * @author leaves
 */
@Data
@TableName("gm_leaveWord")
public class LeaveWord implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 留言内容
     */
    private String content;
    /**
     * 留言时间
     */
    private String time;
    /**
     * 留言人
     */
    private String userName;
    /**
     * 留言人头像
     */
    private String userImg;
    /**
     * 判断是否是本人的留言内容属性
     */
    @TableField(exist = false)
    private String isMe;
    /**
     * 留言下的回复集合
     */
    @TableField(exist = false)
    private List<Answering> answeringList;
    /**
     * 留言下的回复数目
     */
    @TableField(exist = false)
    private Integer count;
    /**
     * 留言下的点赞数目
     */
    @TableField(exist = false)
    private Integer likeCount;

}