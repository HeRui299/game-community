package com.herui.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.herui.pojo.Follow;
import com.herui.pojo.Post;
import com.herui.pojo.User;
import com.herui.pojo.UserLike;
import com.herui.service.CommentServiceImpl;
import com.herui.service.FollowServiceImpl;
import com.herui.service.PostServiceImpl;
import com.herui.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private FollowServiceImpl followService;

    @Autowired
    private HttpSession session;

    @GetMapping
    public String index(Model model){
        User user = (User) session.getAttribute("user");
        // 帖子列表
        QueryWrapper wrapper1 = new QueryWrapper<>();
        wrapper1.orderByDesc("create_time");
        List<Post> list = postService.list(wrapper1);
        List<Map<String,Object>> list1 = new ArrayList<>();

        if (list!=null) {
            for (Post post : list) {
                Map<String,Object> map1 = new HashMap<>();
                map1.put("post",post);
                map1.put("user",userService.getById(post.getUId()));
                list1.add(map1);
            }
        }
        model.addAttribute("postMap",list1);

        List<Map<String,Object>> listMap = new ArrayList<>();
        // 推荐用户
        if (user != null){
            QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
            userQueryWrapper.ne("id",user.getId());
            // 用户列表
            List<User> userList = userService.list(userQueryWrapper);
            // 从关注表中查询哪些推荐用户，被此用户关注了
            if (userList != null){
                for (User user1 : userList) {
                    Map<String,Object> map = new HashMap<>();
                    map.put("user",user1);
                    // 构建查询条件从关注表中查询用户关注过的最新推荐人
                    QueryWrapper<Follow> wrapper = new QueryWrapper<>();
                    wrapper.eq("u_id",user.getId());
                    wrapper.eq("f_id",user1.getId());
                    if (followService.getOne(wrapper) != null){
                        map.put("followStatus",true);  // 已关注的人
                    }else{
                        map.put("followStatus",false); // 未关注的人
                    }
                    listMap.add(map);
                }
            }
        }else{
            // 用户列表
            List<User> userList = userService.list();
            for (User user1 : userList) {
                Map<String,Object> map = new HashMap<>();
                map.put("user",user1);
                map.put("followStatus",false); // 未关注的人
                listMap.add(map);
            }
        }
        model.addAttribute("MapUser",listMap);
        return "index";
    }

    @GetMapping("/deck.html")
    public String deck(Model model){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("post_type",1);
        wrapper.orderByDesc("create_time");
        List list = postService.list(wrapper);
        model.addAttribute("posts",list);
        return "deck";
    }


    @GetMapping("/strategy.html")
    public String strategy(Model model){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("post_type",2);
        wrapper.orderByDesc("create_time");
        List list = postService.list(wrapper);
        model.addAttribute("posts",list);
        return "deck";
    }

    @GetMapping("/article.html")
    public String article(Model model){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("post_type",3);
        wrapper.orderByDesc("create_time");
        List list = postService.list(wrapper);
        model.addAttribute("posts",list);
        return "deck";
    }



}