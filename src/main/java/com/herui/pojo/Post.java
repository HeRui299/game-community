package com.herui.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("post")
public class Post {
    @TableId(type = IdType.AUTO)
    private Integer id;
    // 帖子标题
    private String title;
    // 帖子内容
    private String content;
    // 用户 id
    private Integer uId;
    // 浏览量
    private Integer viewCount;
    // 评论数量
    private Integer commentCount;
    // 喜欢数量
    private Integer likeCount;
    // 创建时间
    @TableField(update = "now()")
    private Date createTime;
    // 修改时间
    @TableField(update = "now()")
    private Date updateTime;
    // 帖子类型 甲板  攻略 同人文
    private Integer postType;
    // 帖子标签
    private Integer postTag;
}