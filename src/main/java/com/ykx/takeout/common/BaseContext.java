package com.ykx.takeout.common;

import org.springframework.web.bind.annotation.GetMapping;

/**
 * 基于ThreadLocal封装的工具类，用于保存和获取当前用户登录Id
 * Created on 2022/10/6.
 *
 * @author KaiXuan Yang
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();
    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
    public static Long getCurrentId(){
        return threadLocal.get();
    }

}
