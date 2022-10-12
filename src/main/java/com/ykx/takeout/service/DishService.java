package com.ykx.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykx.takeout.dto.DishDto;
import com.ykx.takeout.entity.Dish;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;

/**
 * Created on 2022/10/6.
 *
 * @author KaiXuan Yang
 */
public interface DishService extends IService<Dish> {
    //新增菜品同时插入口味数据
    public void saveWithFlavor(DishDto dishDto);
    //根据id查询菜品的信息和口味信息
    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
