package com.wqg.blog.web;


import com.wqg.blog.service.BlogService;
import com.wqg.blog.service.TagService;
import com.wqg.blog.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Auther: wqg
 * @Description:
 */
@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;
    @GetMapping("/")
    public String index(@PageableDefault(size = 10,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         Model model)
    {
        //获取分页数据
        model.addAttribute("page",blogService.listBlog(pageable));
        //获取六条type 按照type大小查找
        model.addAttribute("types",typeService.listTypeTop(6));
        model.addAttribute("tags",tagService.listTagTop(10));
        model.addAttribute("recommendBlogs",blogService.listRecommendBlogTop(8));
        return "index";
    }
    //blog详情页
    @GetMapping("/blog/{id}")
    public String blog(@PathVariable Long id,Model model)
    {
        //把文章内容转换为html格式
        model.addAttribute("blog",blogService.getAndConvert(id));
        return "blog";
    }

    //全局搜索
    @PostMapping("/search")
    public String search(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                         @RequestParam String query, Model model)
    {
        //通过查询条件模糊查询 把查询结果放入page
        model.addAttribute("page",blogService.listBlog(pageable,"%"+query+"%"));
        //查询后把搜索条件放入model并继续显示在页面
        model.addAttribute("query",query);
        return "search";
    }

    @GetMapping("/footer/newblog")
    public String newblogs(Model model)
    {
        model.addAttribute("newblogs",blogService.listRecommendBlogTop(3));
        return "_fragments :: newblogList";
    }

}
