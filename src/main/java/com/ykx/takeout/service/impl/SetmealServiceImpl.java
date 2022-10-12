package com.ykx.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykx.takeout.common.CustomException;
import com.ykx.takeout.dto.SetmealDto;
import com.ykx.takeout.entity.Setmeal;
import com.ykx.takeout.entity.SetmealDish;
import com.ykx.takeout.mapper.SetmealMapper;
import com.ykx.takeout.service.SetmealDishService;
import com.ykx.takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created on 2022/10/6.
 *
 * @author KaiXuan Yang
 */
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper , Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;


    @Transactional
    @Override
    public void saveSetMeals(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        Long setmealId = setmealDto.getId();
        List<SetmealDish> dishes = setmealDishes.stream().map((item) -> {
            item.setSetmealId(setmealId);
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
    }

    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId ,ids ).eq(Setmeal::getStatus , 1);
        int count = this.count(queryWrapper);
        if (count > 0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }

        this.removeByIds(ids);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.in(SetmealDish::getSetmealId , ids);
        setmealDishService.remove(setmealDishLambdaQueryWrapper);


    }
}
