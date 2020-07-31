package com.onoff.wechatofficialaccount.config;

import com.onoff.wechatofficialaccount.entity.BAM.Admin;
import com.onoff.wechatofficialaccount.utils.CommonUtils;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @Description TODO
 * @Author ZHENG
 * @Data 2020/6/11 11:08
 * @VERSION 1.0
 **/
@Component
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        HttpSession session = request.getSession();
        Admin admin = (Admin) session.getAttribute(CommonUtils.ADMIN_SESSION);
        if (admin == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           @Nullable ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                @Nullable Exception ex) throws Exception {
    }
}
