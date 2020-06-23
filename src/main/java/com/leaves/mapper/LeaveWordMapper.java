package com.leaves.mapper;


import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.leaves.entity.LeaveWord;
import org.apache.ibatis.annotations.Mapper;

@Mapper
/**
 * 数据访问层
 */
public interface LeaveWordMapper extends BaseMapper<LeaveWord> {

}
