package com.wqg.blog.web.admin;

import com.wqg.blog.po.Blog;
import com.wqg.blog.po.User;
import com.wqg.blog.service.BlogService;
import com.wqg.blog.service.TagService;
import com.wqg.blog.service.TypeService;
import com.wqg.blog.vo.BlogQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;


/**
 * @Auther: wqg
 * @Description:
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    private static final String INPUT = "admin/blogs-input";
    private static final String LIST = "admin/blogs";
    private static final String REDIRECT_LIST = "redirect:/admin/blogs";

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    //设置分页查询结果 排序方式
    @GetMapping("/blogs")
    public String blogs(@PageableDefault(size = 10,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog , Model model )
    {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("page",blogService.listBlog(pageable,blog));
        return LIST;
    }
    //只返回表单内容 返回blogList块 其余部分不会改变
    @PostMapping("/blogs/search")
    public String search(@PageableDefault(size = 10,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        BlogQuery blog ,Model model )
    {
        model.addAttribute("page", blogService.listBlog(pageable, blog));
        //返回页面的blogList片段 这样其他部分就不会更改
        return "admin/blogs :: blogList";
    }
    //访问新建博客页面，把分类和标签返回给前端
    @GetMapping("/blogs/input")
    public String input(Model model)
    {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
        model.addAttribute("blog",new Blog());
        return INPUT;
    }
    //这个方法用来获取分类和标签
    private void setTypeAndTag(Model model)
    {
        model.addAttribute("types",typeService.listType());
        model.addAttribute("tags",tagService.listTag());
    }
    //修改
    @GetMapping("/blogs/{id}/input")
    public String editInput(@PathVariable Long id, Model model)
    {
        setTypeAndTag(model);
        Blog blog=blogService.getBlog(id);
        blog.init();//初始化获得tagIds
        model.addAttribute("blog",blog);
        return INPUT;
    }
    //新增和修改
    @PostMapping("/blogs")
    public String post(Blog blog, RedirectAttributes redirectAttributes, HttpSession session)
    {
        blog.setUser((User)session.getAttribute("user"));
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTags(tagService.listTag(blog.getTagIds()));
        Blog b;
        if (blog.getId()==null)
        {
            b=blogService.savaBlog(blog);
        }
        else
        {
            b=blogService.updateBlog(blog.getId(),blog);
        }
        if (b==null)
        {
            redirectAttributes.addFlashAttribute("message","提交失败");
        }else
        {
            redirectAttributes.addFlashAttribute("message","提交成功");
        }

        return REDIRECT_LIST;
    }
    //删除
    @GetMapping("/blogs/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes)
    {
        blogService.deleteBlog(id);
        redirectAttributes.addFlashAttribute("message","删除成功!");
        return REDIRECT_LIST;
    }
}
