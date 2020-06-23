package com.leaves.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 板块实体类
 *
 * @author leaves
 */
@Data
@TableName("gm_plate")
public class Plate implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 分类名称
     */
    private String categoryId;
    private String name;
    /**
     * 图片
     */
    private String img;
    /**
     * 简介
     */
    private String content;
    /**
     * 成员
     */
    private String member;

}