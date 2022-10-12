package com.ykx.takeout.filter;

import com.alibaba.fastjson.JSON;
import com.ykx.takeout.common.BaseContext;
import com.ykx.takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created on 2022/10/4.
 *检查用户是否完成登录
 * @author KaiXuan Yang
 */
@Slf4j
@WebFilter(filterName = "loginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    //路劲匹配器，支持通配符
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response = (HttpServletResponse)servletResponse;
        String requestURI = request.getRequestURI();  // /backend/index.html
        log.info("拦截到的请求路径：{}",requestURI);
        //定义不需要处理的请求路径
        String[] urls = new String[]{
                "/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login"
        };
        boolean check = check(urls, requestURI);
        if (check){
            log.info("本次请求{}不需要处理",requestURI);
            filterChain.doFilter(request,response);
            return;
        }
        if (request.getSession().getAttribute("employee") != null){
            log.info("用户已经登录，用户ID:{}",request.getSession().getAttribute("employee"));
            long id = Thread.currentThread().getId();
            log.info("当前线程id为：{}",id);
            Long empId = (Long) request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        if (request.getSession().getAttribute("user") != null){
            Long userId = (Long) request.getSession().getAttribute("user");
            BaseContext.setCurrentId(userId);
            filterChain.doFilter(request , response);
            return;
        }
        //如果没有登录，通过流的方式向客户端页面相应数据
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;



    }

    /**
     * 判断本次请求是否需要放行
     * @param requestUrl
     * @return
     */
    public boolean check(String[] urls,String requestUrl){
        for (String url: urls){
            boolean match = PATH_MATCHER.match(url, requestUrl);
            if (match){
                return true;
            }
        }
        return false;
    }
}
