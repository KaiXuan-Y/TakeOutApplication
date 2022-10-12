package com.ykx.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykx.takeout.entity.ShoppingCart;
import com.ykx.takeout.mapper.ShoppingCartMapper;
import com.ykx.takeout.service.ShoppingCartService;
import org.springframework.stereotype.Service;

/**
 * Created on 2022/10/12.
 *
 * @author KaiXuan Yang
 */
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper , ShoppingCart> implements ShoppingCartService {
}
