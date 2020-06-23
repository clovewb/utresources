package com.leaves.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.leaves.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/**
 * 数据访问层
 */
public interface CategoryMapper extends BaseMapper<Category> {

}
