package com.wqg.blog.web.admin;


import com.wqg.blog.po.Tag;
import com.wqg.blog.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * @Auther: wqg
 * @Description:
 */
@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;
    //分页查询一次返回十条数据根据id倒序查询
    @GetMapping("/tags")
    public String list(@PageableDefault(size = 10,sort = {"id"},direction = Sort.Direction.DESC)
                               Pageable pageable, Model model)
    {
        model.addAttribute("page",tagService.listTag(pageable));
        return "admin/tags";
    }

    //新增标签页面  将用户填入的数据封装到model对象中
    @GetMapping("/tags/input")
    public String input(Model model)
    {
        model.addAttribute("tag",new Tag());
        return "admin/tags-input";
    }

    //修改标签 并将修改数据进行查询后放入model对象 前端页面取得model对象进行判断
    @GetMapping("/tags/{id}/input")
    public String editInput(@PathVariable Long id, Model model)
    {
        model.addAttribute("tag",tagService.getTag(id));
        return "admin/tags-input";
    }

    //新增标签
    @PostMapping("/tags")
    public String post(@Valid Tag tag, BindingResult result, RedirectAttributes redirectAttributes)
    {
        Tag tag1=tagService.getTagByName(tag.getName());
        if (tag1!=null)
        {
            result.rejectValue("name","nameError","不能添加标签重复名称");
        }
        if (result.hasErrors())
        {
            return "admin/tags-input";
        }
        Tag t=tagService.saveTag(tag);
        if (t==null)
        {
            //新增失败
            redirectAttributes.addFlashAttribute("message","新增失败");
        }else {
            //新增成功
            redirectAttributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/tags";
    }
    //修改标签
    @PostMapping("/tags/{id}")
    public String editpost(@Valid Tag tag, BindingResult result,@PathVariable Long id,RedirectAttributes redirectAttributes)
    {
        Tag tag1=tagService.getTagByName(tag.getName());
        if (tag1!=null)
        {
            result.rejectValue("name","nameError","不能添加分类重复名称");
        }
        if (result.hasErrors())
        {
            return "admin/tags-input";
        }
        Tag t=tagService.updateTag(id,tag);
        if (t==null)
        {
            //新增失败
            redirectAttributes.addFlashAttribute("message","更新失败");
        }else {
            //新增成功
            redirectAttributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/tags";
    }

    //删除标签
    @GetMapping("/tags/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes)
    {
        tagService.deleteTag(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/tags";
    }
}
