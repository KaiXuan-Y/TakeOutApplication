package com.ykx.takeout.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常捕获
 * Created on 2022/10/5.
 *
 * @author KaiXuan Yang
 */
@ControllerAdvice(annotations = {RestController.class , Controller.class})
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     * @return
     */

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){

        log.error(ex.getMessage());
        if (ex.getMessage().contains("Duplicate entry")){
            String msg = ex.getMessage().split(" ")[2];
            msg = msg + "已经存在";
            return R.error(msg);
        }
        return R.error("未知错误");
    }
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.error(ex.getMessage());
        return R.error(ex.getMessage());
    }

}