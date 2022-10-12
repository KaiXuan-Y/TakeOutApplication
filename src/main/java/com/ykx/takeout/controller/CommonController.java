package com.ykx.takeout.controller;

import com.ykx.takeout.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

/**
 * 文件的上传和下载
 * Created on 2022/10/7.
 *
 * @author KaiXuan Yang
 */
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${takeout.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file){
        //当前的file是临时文件，需要转存到指定的位置，不然请求结束后文件也消失了
        log.info(file.toString());
        String originalFilename = file.getOriginalFilename();
        String suffixName = originalFilename.substring(originalFilename.lastIndexOf("."));
        //用原文件名肯呢个出现文件重名造成文件覆盖问题，使用uuid
        String fileName = UUID.randomUUID().toString() + suffixName;
        //创建一个目录对象
        File dir = new File(basePath);
        if (!dir.exists()){
            //不存在的话创建
            dir.mkdirs();
        }
        try {
            file.transferTo(new File(basePath + fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return R.success(fileName);
    }

    @GetMapping("/download")
    public void download(String name , HttpServletResponse response) {
        FileInputStream fis = null;
        ServletOutputStream ops = null;
        //输入流，通过输入流读取文件内容
        try {
            fis = new FileInputStream(new File(basePath + name));
            //输出流，通过输出流协会浏览器，在浏览器展示图片
            ops = response.getOutputStream();
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fis.read(bytes)) != -1){
                ops.write(bytes , 0 , len);
                ops.flush();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                ops.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}
