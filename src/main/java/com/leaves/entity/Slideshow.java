package com.leaves.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 轮播图实体类
 *
 * @author leaves
 */
@Data
@TableName("gm_slideshow")
public class Slideshow implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 图片地址
     */
    private String imgUrl;
    /**
     * 创建时间
     */
    private String creationTime;
    /**
     * 创建用户
     */
    private String creationUser;
}