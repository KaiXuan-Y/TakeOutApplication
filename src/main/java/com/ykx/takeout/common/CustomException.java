package com.ykx.takeout.common;

/**
 * 自定义业务异常
 * Created on 2022/10/6.
 *
 * @author KaiXuan Yang
 */
public class CustomException extends RuntimeException{
    public CustomException(String message){
        super(message);
    }

}
