package com.herui.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("follow")
public class Follow {
    @TableId(type = IdType.AUTO)
    private Integer id;
    // 关注人的用户的id
    private Integer uId;
    // 被关注人的id
    private Integer fId;
}