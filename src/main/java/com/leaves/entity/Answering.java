package com.leaves.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 留言回复内容实体类
 *
 * @author leaves
 */
@Data
@TableName("gm_answering")
public class Answering implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 回复内容
     */
    private String content;
    /**
     * 回复时间
     */
    private String time;
    /**
     * 回复人
     */
    private String userName;
    /**
     * 回复人头像
     */
    private String userImg;
    /**
     * 判断是否是本人的回复内容属性
     */
    @TableField(exist = false)
    private String isMe;
    /**
     * 留言id
     */
    private String leaveId;

}