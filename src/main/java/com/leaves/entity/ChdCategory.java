package com.leaves.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 教学资源子分类实体类
 *
 * @author leaves
 */
@Data
@TableName("gm_chdCategory")
public class ChdCategory implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 父分类id
     */
    private String ptId;

}