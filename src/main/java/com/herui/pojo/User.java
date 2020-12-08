package com.herui.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("user")
public class User {
     @TableId(type = IdType.AUTO)
     private Integer id;
     // 用户名
     private String username;
     // 密码
     private String password;
     // 粉丝数量
     private Integer fansCount;
     // 关注数量
     private Integer followCount;
     // 喜欢数量
     private Integer likeCount;
     // 账号
     private String account;
     // 头像
     private String headUrl;
}