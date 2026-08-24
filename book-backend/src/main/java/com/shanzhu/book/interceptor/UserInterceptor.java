package com.shanzhu.book.interceptor;

import com.shanzhu.book.model.User;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 用户拦截器，拦截未登录不能访问的请求
 *
 * @author: ShanZhu
 * @date: 2023-12-31
 */
public class UserInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = (User) request.getSession().getAttribute("userObj");

        if (user == null) {  //没有登录
            System.out.println("没有登录!不能访问!");
            // 重定向到登录界面
            response.sendRedirect(request.getContextPath() + "/index.html");
            return false;
        }

        //放行
        return true;
    }

}
