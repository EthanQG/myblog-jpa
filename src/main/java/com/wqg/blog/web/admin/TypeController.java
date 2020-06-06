package com.wqg.blog.web.admin;

import com.wqg.blog.po.Type;
import com.wqg.blog.service.TypeService;
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
public class TypeController {
    @Autowired
    private TypeService typeService;
    //分页查询一次返回十条数据根据id倒序查询
    @GetMapping("/types")
    public String list(@PageableDefault(size = 10,sort = {"id"},direction = Sort.Direction.DESC)
                                   Pageable pageable,Model model)
    {
        model.addAttribute("page",typeService.listType(pageable));
        return "admin/types";
    }

    //返回新增分类页面  将用户填入的数据封装到model对象中
    @GetMapping("/types/input")
    public String input(Model model)
    {
        model.addAttribute("type",new Type());
        return "admin/types-input";
    }

    //修改分类
    @GetMapping("/types/{id}/input")
    public String editInput(@PathVariable Long id, Model model)
    {
        model.addAttribute("type",typeService.getType(id));
        return "admin/types-input";
    }

    //新增分类
    @PostMapping("/types")
    public String post(@Valid Type type, BindingResult result,RedirectAttributes redirectAttributes)
    {
        Type type1=typeService.getTypeByName(type.getName());
        if (type1!=null)
        {
            result.rejectValue("name","nameError","不能添加分类重复名称");
        }
        if (result.hasErrors())
        {
            return "admin/types-input";
        }
        Type t=typeService.saveType(type);
        if (t==null)
        {
            //新增失败
            redirectAttributes.addFlashAttribute("message","新增失败");
        }else {
            //新增成功
            redirectAttributes.addFlashAttribute("message","新增成功");
        }
        return "redirect:/admin/types";
    }
    //修改分类
    @PostMapping("/types/{id}")
    public String editpost(@Valid Type type, BindingResult result,@PathVariable Long id,RedirectAttributes redirectAttributes)
    {
        Type type1=typeService.getTypeByName(type.getName());
        if (type1!=null)
        {
            result.rejectValue("name","nameError","不能添加分类重复名称");
        }
        if (result.hasErrors())
        {
            return "admin/types-input";
        }
        Type t=typeService.updateType(id,type);
        if (t==null)
        {
            //新增失败
            redirectAttributes.addFlashAttribute("message","更新失败");
        }else {
            //新增成功
            redirectAttributes.addFlashAttribute("message","更新成功");
        }
        return "redirect:/admin/types";
    }

    //删除分类
    @GetMapping("/types/{id}/delete")
    public String delete(@PathVariable Long id,RedirectAttributes redirectAttributes)
    {
        typeService.deleteType(id);
        redirectAttributes.addFlashAttribute("message","删除成功");
        return "redirect:/admin/types";
    }

}
