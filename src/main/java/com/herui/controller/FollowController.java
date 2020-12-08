package com.herui.controller;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.herui.pojo.Follow;
import com.herui.pojo.User;
import com.herui.service.FollowServiceImpl;
import com.herui.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
@Controller
public class FollowController {

    @Autowired
    private FollowServiceImpl followService;

    @Autowired
    private UserServiceImpl userService;

    @RequestMapping("/follow")
    @ResponseBody
    @Transactional
    public Object follow(Follow follow) {
            // 添加数据到关注表,在添加之前先查询一遍有没有关注过某人，如若关注过则此次操作视为取消关注
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("u_id",follow.getUId());
            wrapper.eq("f_id",follow.getFId());
            Follow one = followService.getOne(wrapper);
            if (one != null){ // 证明关注了
                // 关注的人
                User follower = userService.getById(follow.getUId());
                follower.setFollowCount(follower.getFollowCount()-1);
                // 被关注的人
                User followed = userService.getById(follow.getFId());
                followed.setFansCount(followed.getFansCount()-1);

                // 保存两次操作
                userService.updateById(follower);
                userService.updateById(followed);

                return followService.removeById(one.getId());
            }
        // 关注的人
        User follower = userService.getById(follow.getUId());
        System.err.println("关注者"+follower.getUsername());
        follower.setFollowCount(follower.getFollowCount()+1);
        // 被关注的人
        User followed = userService.getById(follow.getFId());
        System.err.println("被关注者"+followed.getUsername());
        followed.setFansCount(followed.getFansCount()+1);
        // 保存两次操作
        userService.updateById(follower);
        userService.updateById(followed);

        return followService.save(follow);
    }
}