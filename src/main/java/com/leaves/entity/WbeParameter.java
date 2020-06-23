package com.leaves.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 网站参数实体类
 *
 * @author leaves
 */
@Data
@TableName("gm_wbeParameter")
public class WbeParameter implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * logo设置
     */
    private String logo;
    /**
     * 网站名称
     */
    private String name;
    /**
     * 友情连接1
     */
    private String link1;
    /**
     * 友情连接2
     */
    private String link2;
    /**
     * 友情连接3
     */
    private String link3;
    /**
     * 友情连接4
     */
    private String link4;
    /**
     * 关于我们
     */
    private String aboutWe;
}