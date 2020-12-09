package com.herui.controller;
import com.herui.pojo.Comment;
import com.herui.pojo.Post;
import com.herui.pojo.User;
import com.herui.service.CommentServiceImpl;
import com.herui.service.PostServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
import java.util.Date;

@RequestMapping("/comment")
@Controller
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private HttpSession session;

    @RequestMapping("/addComment")
    @ResponseBody
    public Object addComment(Comment comment,Integer id){
        User user = (User) session.getAttribute("user");
        comment.setUId(user.getId());
        comment.setCommentId(id);
        comment.setReplyId(0);
        comment.setCreateTime(new Date(System.currentTimeMillis()));

        Post post = postService.getById(id);
        post.setCommentCount(post.getCommentCount() +1);
        postService.updateById(post);
        return commentService.save(comment);
    }
}