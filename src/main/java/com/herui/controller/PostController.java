package com.herui.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.herui.pojo.Comment;
import com.herui.pojo.Post;
import com.herui.pojo.User;
import com.herui.pojo.UserLike;
import com.herui.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private UserLikeServiceImpl userLikeService;

    @Autowired
    private HttpSession session;

    @RequestMapping("/publish")
    public String publish(RedirectAttributes redirectAttributes){
        if (session.getAttribute("user") == null){
            redirectAttributes.addAttribute("msg","发帖子前先登录噢!");
            return "redirect:/toLogin";
        }
        return "publish";
    }

    @RequestMapping("/save")
    @ResponseBody
    public Object savePost(Post post){
        User user = (User) session.getAttribute("user");
        if (user != null) {
            post.setUId(user.getId());
        }
        post.setCreateTime(new Date());
        if (post.getContent().length()>6) {
            String substring = post.getContent().substring(0, 6);
            post.setPostDesc(substring);
        }else{
            post.setPostDesc(post.getContent());
        }

        return postService.save(post);
    }

    @Autowired
    private FollowServiceImpl followService;

    @RequestMapping("/detail/{id}")
    public String detail(@PathVariable Integer id, Model model){
        Post post = postService.getById(id);
        model.addAttribute("post",post);
        // 帖子作者
        model.addAttribute("hhh",userService.getById(post.getUId()));
        //帖子id
        int postId = post.getUId();
        //用户id
        User a = (User) session.getAttribute("user");
        if (a != null) {
            int userId = a.getId();
            model.addAttribute("userId",userId);
            QueryWrapper wrapper2 = new QueryWrapper();
            wrapper2.eq("u_id", userId);
            wrapper2.eq("f_id", postId);
            model.addAttribute("followStatus", followService.getOne(wrapper2) == null ? false : true);
        }
        QueryWrapper wrapper1 = new QueryWrapper();
        wrapper1.eq("comment_id",id);
        List<Comment> list = commentService.list(wrapper1);
        List<Map<String,Object>> list1 = new ArrayList<>();
        if (list!=null) {
            for (Comment comment: list) {
                Map<String,Object> map1 = new HashMap<>();
                map1.put("comment",comment);
                map1.put("user",userService.getById(comment.getUId()));
                list1.add(map1);
            }
        }
        model.addAttribute("commentMap",list1);
        Integer postType = post.getPostType();
        if (postType == 1){
            model.addAttribute("type","甲板");
        }else if(postType == 2){
            model.addAttribute("type","攻略");
        }else{
            model.addAttribute("type","同人文");
        }
        QueryWrapper wrapper = new QueryWrapper();
        User user = (User) session.getAttribute("user");
        if (user != null) {
            wrapper.eq("u_id", user.getId());
            wrapper.eq("like_id", id);
            if (userLikeService.getOne(wrapper) != null){
                model.addAttribute("likeStatus",true);
            }else{
                model.addAttribute("likeStatus",false);
            }
        }
        post.setViewCount(post.getViewCount() + 1);
        postService.updateById(post);
        return "details";
    }

    /**
     * 点赞，同一用户只能点赞一次再点就是取消点赞
     *  表数据改变   帖子点赞数   用户和点赞中间表
     * @param id
     * @return
     */
    @Autowired
    private UserServiceImpl userService;
    @RequestMapping("/like")
    @ResponseBody
    @Transactional
    public Object like(UserLike userLike){
        // 添加数据到用户和喜欢关联表,在添加之前先查询一遍有没有点过赞，如若点过则此次操作视为取消点赞
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("u_id",userLike.getUId());
        wrapper.eq("like_id",userLike.getLikeId());
        Post post = postService.getById(userLike.getLikeId());
        User byId = userService.getById(post.getUId());
        UserLike one = userLikeService.getOne(wrapper);
        if (one != null){ // 证明点过赞
            byId.setLikeCount(byId.getLikeCount()-1);
            post.setLikeCount(post.getLikeCount() - 1);
            return userLikeService.removeById(one.getId()) && postService.updateById(post);
        }
        byId.setLikeCount(byId.getLikeCount()+1);
        userService.updateById(byId);
        post.setLikeCount(post.getLikeCount() + 1);
        // 点赞
       return userLikeService.save(userLike) && postService.updateById(post);
    }

}