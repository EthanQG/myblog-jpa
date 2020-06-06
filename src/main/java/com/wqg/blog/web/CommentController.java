package com.wqg.blog.web;

import com.wqg.blog.po.Comment;
import com.wqg.blog.po.User;
import com.wqg.blog.service.BlogService;
import com.wqg.blog.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

/**
 * @Auther: wqg
 * @Description:
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private BlogService blogService;

    @Value("${comment.avatar}")
    private String avatar;

    //获取评论列表
    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model)
    {
        //通过blogid查询这条blog下的所有评论
        model.addAttribute("comments",commentService.listCommentByBlogId(blogId));

        return "blog :: commentList";
    }

    //保存信息并返回评论页面
    @PostMapping("/comments")
    public String post(Comment comment, HttpSession session)
    {

        //获取到blogId来得到blog
        Long blogId=comment.getBlog().getId();
        //建立blog和comment之间的关系
        comment.setBlog(blogService.getBlog(blogId));
        //从session中判断管理员是否登录
        User user=(User)session.getAttribute("user");
        if (user!=null)
        {
            comment.setAvatar(user.getAvatar());
            comment.setAdminComment(true);
        }else{
            comment.setAvatar(avatar);
        }
        commentService.saveComment(comment);
        return "redirect:/comments/"+comment.getBlog().getId();
    }
}
