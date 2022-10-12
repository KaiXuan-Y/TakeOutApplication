package com.ykx.takeout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Created on 2022/9/27.
 *
 * @author KaiXuan Yang
 */

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class TakeOutApplication {
    public static void main(String[] args) {
        SpringApplication.run(TakeOutApplication.class,args);
        log.info("项目启动成功。。");
    }
}
