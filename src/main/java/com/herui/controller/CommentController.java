package com.herui.controller;
import com.herui.pojo.Comment;
import com.herui.pojo.User;
import com.herui.service.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.servlet.http.HttpSession;
@RequestMapping("/comment")
@Controller
public class CommentController {

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private HttpSession session;

    @RequestMapping("/addComment")
    @ResponseBody
    public Object addComment(Comment comment){
        System.out.println(comment.getContent());
        User user = (User) session.getAttribute("user");
        comment.setUId(user.getId());
        comment.setReplyId(0);
       return commentService.save(comment);
    }

}