package com.ykx.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykx.takeout.entity.User;
import com.ykx.takeout.mapper.UserMapper;
import com.ykx.takeout.service.UserService;
import org.springframework.stereotype.Service;

/**
 * Created on 2022/10/11.
 *
 * @author KaiXuan Yang
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper , User> implements UserService {
}
