package com.leaves.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 浏览记录实体类
 *
 * @author leaves
 */
@Data
@TableName("gm_browseRecord")
public class BrowseRecord implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * id
     */
    private String productId;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 更新时间
     */
    private String time;
    /**
     * 浏览次数
     */
    private Integer frequency;

}