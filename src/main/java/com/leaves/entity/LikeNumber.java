package com.leaves.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.IdType;
import lombok.Data;

import java.io.Serializable;

/**
 * 留言点赞实体类
 *
 * @author leaves
 */
@Data
@TableName("gm_likeNumber")
public class LikeNumber implements Serializable {
    /**
     * 自增长主键
     */
    @TableId(value = "id", type = IdType.UUID)
    private String id;
    /**
     * 点赞用户id
     */
    private String userId;
    /**
     * 点赞留言id
     */
    private String leaveId;

}