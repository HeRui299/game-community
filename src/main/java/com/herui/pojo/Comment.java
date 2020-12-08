package com.herui.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("comment")
@Data
public class Comment {

    @TableId(type=IdType.AUTO)
    private Integer id;
    // 帖子内容
    private String content;

    // 帖子发布时间
    @TableField(update = "now()")
    private Date createTime;
    // 帖子喜欢数量
    private Integer likeCount;
    // 帖子类型
    private Integer commentType;
    // 评论id
    private Integer commentId;
    // 回复评论的id
    private Integer replyId;
    // 用户id
    private Integer uId;


}