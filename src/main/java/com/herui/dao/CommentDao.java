package com.herui.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.herui.pojo.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentDao extends BaseMapper<Comment> {

}