package com.herui.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.herui.pojo.Comment;
import com.herui.pojo.Follow;
import com.herui.pojo.User;
import com.herui.service.CommentServiceImpl;
import com.herui.service.FollowServiceImpl;
import com.herui.service.PostServiceImpl;
import com.herui.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * 用户控制类
 */
@RequestMapping("/user")
@Controller
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private PostServiceImpl postService;

    @Autowired
    private FollowServiceImpl followService;

    @Autowired
    private CommentServiceImpl commentService;

    @Autowired
    private HttpSession session;

    /**
     * 注册用户
     * @param username 用户名
     * @param password 密码
     * @return
     */
    @PostMapping("/register")
    @ResponseBody
    public Object register(String username,String password){
        QueryWrapper<User> wrapper = new QueryWrapper();
        wrapper.eq("username",username);
        Map<String,Object> map = new HashMap<>();

        if(userServiceImpl.getOne(wrapper) != null){
            map.put("msg","该用户已被注册");
            return map;
        }
        User a = new User();
        a.setUsername(username);
        a.setPassword(password);
        a.setHeadUrl("d:/h/upload/f6f71573-f99f-4e4b-85c5-9e5107b4fd20.jpg");

        if (userServiceImpl.save(a)) {
            map.put("msg", "注册成功赶快去登录吧！");
        } else {
            map.put("msg", "注册失败请联系管理员");
        }
        return map;
    }

    @PostMapping("/login")
    @ResponseBody
    public Object login(String username, String password, Model model){
        QueryWrapper<User> wrapper = new QueryWrapper();
        wrapper.eq("username",username).eq("password",password);
        User one = userServiceImpl.getOne(wrapper);
        Map<String,Object> map = new HashMap<>();
        if (one != null){
            map.put("code",0);
            session.setAttribute("user",one);
        }else{
            map.put("code",1);
            map.put("msg","用户名或密码错误");
        }
        return map;
    }

    @GetMapping("/person")
    public String person(Model model){
        User user = (User) session.getAttribute("user");
        model.addAttribute("user",userServiceImpl.getById(user.getId()));
        return "personal";
    }

    @PostMapping("/update_avatar")
    @ResponseBody
    public Object updateHeader(@RequestParam(value = "file")MultipartFile file, HttpServletRequest request) throws IOException {
       //1.确定保存的文件夹
        String dirPath = "d:/h/upload";

        File dir = new File(dirPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        //2.确定保存的文件名
        String orginalFilename = file.getOriginalFilename();
        int beginIndex = orginalFilename.lastIndexOf(".");
        String suffix ="";
        if(beginIndex!=-1) {
            suffix = orginalFilename.substring(beginIndex);
        }
        String filename = UUID.randomUUID().toString()+suffix;
        //创建文件对象，表示要保存的头像文件,第一个参数表示存储的文件夹，第二个参数表示存储的文件
        File dest = new File(dir,filename);
        //执行保存
        file.transferTo(dest);
        //更新数据表
        User user = (User) session.getAttribute("user");
        User byId = userServiceImpl.getById(user.getId());
        UpdateWrapper<User> wrapper = new UpdateWrapper<>();
        wrapper.eq("id",byId.getId());
        wrapper.set("head_url",dirPath+"/"+filename);
        userServiceImpl.update(wrapper);
        Map<String,Object> map = new HashMap<>();
        map.put("state","100");
        map.put("url",dirPath+"/"+orginalFilename);
        return map;
    }

    @GetMapping("/header/{id}")
    public void header(@PathVariable Integer id,HttpServletResponse response){
        User user = userServiceImpl.getById(id);

        // 服务器存放路径
        String url = user.getHeadUrl();
        // 文件的后缀
        if (url != null) {
            String suffix = url.substring(url.lastIndexOf("."));

        // 响应图片
        response.setContentType("image/" + suffix);
        }
        try (FileInputStream fis = new FileInputStream(url); OutputStream os = response.getOutputStream();){
            byte[] flush = new byte[1024];
            int b = 0;
            while((b = fis.read(flush)) != -1){
                os.write(flush,0,b);
            }
        } catch (IOException e) {
            System.out.println("头像读取异常");
//            e.printStackTrace();
//            logger.error("读取头像失败" + e.getMessage());
        }
    }

    @RequestMapping("/logout")
    public String logout(){
        session.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/myPost/{id}")
    public String myPost(@PathVariable Integer id,Model model){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("u_id",id);
        List list = postService.list(wrapper);
        model.addAttribute("myPost",list);
        return "MyPost";
    }

    // 查询当前用户的粉丝
    @RequestMapping("/myFans/{id}")
    public String myFans(@PathVariable Integer id,Model model){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("f_id",id);
        List<Follow> list = followService.list(wrapper);

        List<User> userList = null;
        if (list != null) {
            userList = new ArrayList<>();
            for (Follow follow : list) {
                System.out.println("粉丝id"+follow.getUId());
                userList.add(userServiceImpl.getById(follow.getUId()));
            }
        }

        // 粉丝列表
        model.addAttribute("myFans",userList);
        return "MyFans";
    }

    @RequestMapping("/myFollower/{id}")
    public String myFollower(@PathVariable Integer id,Model model){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("u_id",id);
        List<Follow> list = followService.list(wrapper);

        List<User> userList = null;
        if (list != null) {
            userList = new ArrayList<>();
            for (Follow follow : list) {
                userList.add(userServiceImpl.getById(follow.getFId()));
            }
        }

        // 粉丝列表
        model.addAttribute("myFollower",userList);
        return "MyFollower";
    }

    @RequestMapping("/myComment/{id}")
    public String myComment(@PathVariable Integer id,Model model){
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("u_id",id);
        List<Comment> list = commentService.list(wrapper);
        model.addAttribute("myComment",list);
        return "MyComment";
    }

}