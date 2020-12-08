package com.herui;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.herui.pojo.Comment;
import com.herui.service.CommentServiceImpl;
import com.herui.service.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
class GameCommunityApplicationTests {

    // 评论
    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private UserServiceImpl userService;

    @Test
    void contextLoads() {
        // 评论
        List<Comment> list = commentService.list();

        // 评论 Vo 列表
        List<Map<String,Object>> commentVoList = new ArrayList<>();

        if (list != null) {
            for (Comment comment : list) {
                Map<String,Object> commentVo = new HashMap<>();
                // 评论
                commentVo.put("comment",comment);
                // 作者
                commentVo.put("user",userService.getById(comment.getUId()));

                QueryWrapper<Comment> wrapper = new QueryWrapper<>();
                wrapper.eq("reply_id",comment.getId());
                // 回复评论的的评论
                List<Comment> replyList = commentService.list(wrapper);
                List<Map<String,Object>> replyVoList = new ArrayList<>();
                if (replyList != null) {
                    for (Comment reply : replyList) {
                        Map<String,Object> replyVo = new HashMap<>();
                        replyVo.put("reply",reply);
                        replyVo.put("user",userService.getById(reply.getUId()));
                        replyVoList.add(replyVo);
                    }
                }
                commentVo.put("replys",replyVoList);
                commentVoList.add(commentVo);
            }
        }

        System.out.println(commentVoList.get(1).get("replys"));

    }
}