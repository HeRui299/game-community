package com.herui.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user_like")
public class UserLike {

    @TableId(type = IdType.AUTO)
    private Integer id;
    // 用户 id
    private Integer uId;
    // 点赞类型
    private Integer likeType;
    // 点赞类型的id
    private Integer likeId;
}