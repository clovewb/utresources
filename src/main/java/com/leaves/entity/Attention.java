package com.leaves.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 关注实体类
 *
 * @author leaves
 */
@Data
@TableName("gm_attention")
public class Attention implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 关注人
     */
    private String userId;
    /**
     * 被关注人
     */
    private String attenId;
    @TableField(exist = false)
    private User user;
}