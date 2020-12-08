package com.herui.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.herui.dao.UserDao;
import com.herui.pojo.User;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> {

}