package com.wqg.blog.Interceptor;


import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Auther: wqg
 * @Description:
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {
    //在请求未到达前进行预处理
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if (request.getSession().getAttribute("user")==null)
        {
            response.sendRedirect("/admin");
            return false;
        }

        return true;
    }
}
