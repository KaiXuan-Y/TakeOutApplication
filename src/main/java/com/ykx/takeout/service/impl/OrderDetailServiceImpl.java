package com.ykx.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykx.takeout.entity.OrderDetail;
import com.ykx.takeout.mapper.OrderDetailMapper;
import com.ykx.takeout.service.OrderDetailService;
import org.springframework.stereotype.Service;

/**
 * Created on 2022/10/12.
 *
 * @author KaiXuan Yang
 */
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper , OrderDetail> implements OrderDetailService {
}
