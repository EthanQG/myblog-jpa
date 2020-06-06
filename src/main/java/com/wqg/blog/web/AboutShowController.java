package com.wqg.blog.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @Auther: wqg
 * @Description:
 */
@Controller
public class AboutShowController {
    @GetMapping("/about")
    public String about()
    {
        return "about";
    }
}
