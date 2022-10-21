package com.ykx.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ykx.takeout.common.R;
import com.ykx.takeout.entity.User;
import com.ykx.takeout.service.UserService;
import com.ykx.takeout.utils.SMSUtils;
import com.ykx.takeout.utils.ValidateCodeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created on 2022/10/11.
 *
 * @author KaiXuan Yang
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送手机短信验证码
     * @param user
     * @return
     */
    @RequestMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user , HttpServletRequest request){
        //获取手机号

        String phone = user.getPhone();
        //生成随机的4位验证码
        if (!StringUtils.isEmpty(phone)){
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("code = {}" ,code);
            //阿里云短信服务完成短信的发送
//            SMSUtils.sendMessage("takeout","",phone , code);
//            request.getSession().setAttribute(phone , code);
            //将生成的验证码让如redis服务器中，有效期为5分钟
            redisTemplate.opsForValue().set(phone,code,5, TimeUnit.MINUTES);
            return R.success("短信发送 成功");
        }

        //保存
        return R.error("短信发送失败");
    }
    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session){
        log.info(map.toString());
        //获取手机号
        String phone = map.get("phone").toString();
        //获取验证码
        String code = map.get("code").toString();
        //从session中获取保存的验证码
//        Object codeInSession = session.getAttribute(phone);
        //从redis中获取验证码
//        Object codeInSession = redisTemplate.opsForValue().get(phone);
//        if (codeInSession != null && codeInSession.equals(code)){
//            //保存
//            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
//            queryWrapper.eq(User::getPhone , phone);
//            User user = userService.getOne(queryWrapper);
//            if (user == null){
//                user = new User();
//                user.setPhone(phone);
//                user.setStatus(1);
//                userService.save(user);
//            }
//            //如果登录成功删除redis中的验证码
//            redisTemplate.delete(phone);
//            return R.success(user);
//        }
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone , phone);
        User user = userService.getOne(queryWrapper);
        if (user == null){
            user = new User();
            user.setPhone(phone);
            user.setStatus(1);
            userService.save(user);
        }
        session.setAttribute("user" , user.getId());


        return R.success(user);
    }
}
