package com.herui.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.herui.pojo.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostDao extends BaseMapper<Post> {

}