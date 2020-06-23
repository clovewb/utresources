package com.leaves.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 收藏实体类
 *
 * @author leaves
 */
@Data
@TableName("gm_collect")
public class Collect implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 视频id
     */
    private String postId;

    @TableField(exist = false)
    private PostInfo postInfo;

}