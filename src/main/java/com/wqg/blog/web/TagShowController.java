package com.wqg.blog.web;

import com.wqg.blog.po.Tag;
import com.wqg.blog.po.Type;
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

import java.util.List;

/**
 * @Auther: wqg
 * @Description:
 */
@Controller
public class TagShowController {
    @Autowired
    private TagService tagService;
    @Autowired
    private BlogService blogService;
    @GetMapping("/tags/{id}")
    public String types(@PageableDefault(size = 8,sort = {"updateTime"},direction = Sort.Direction.DESC) Pageable pageable,
                        @PathVariable Long id, Model model)
    {
        List<Tag> tags=tagService.listTagTop(1000);//查询所有tpyes
        //id为-1说明是从导航点进来的 默认返回第一个分类
        if (id == -1) {
            id = tags.get(0).getId();
        }
        model.addAttribute("tags",tags);
        //根据tagid去关联查询
        model.addAttribute("page",blogService.listBlog(pageable,id));
        model.addAttribute("activeTagId",id);//把选中的typeid也返回 前端用来判断选中状态
        return "tags";
    }
}
