package com.ykx.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykx.takeout.entity.Orders;

/**
 * Created on 2022/10/12.
 *
 * @author KaiXuan Yang
 */
public interface OrderService extends IService<Orders> {
    /**
     * 用户下单
     * @param orders
     */
    public void submit(Orders orders);
}
