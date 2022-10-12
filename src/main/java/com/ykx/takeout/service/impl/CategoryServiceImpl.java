package com.ykx.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykx.takeout.common.CustomException;
import com.ykx.takeout.entity.Category;
import com.ykx.takeout.entity.Dish;
import com.ykx.takeout.entity.Setmeal;
import com.ykx.takeout.mapper.CategoryMapper;
import com.ykx.takeout.service.CategoryService;
import com.ykx.takeout.service.DishService;
import com.ykx.takeout.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created on 2022/10/6.
 *
 * @author KaiXuan Yang
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private DishService dishService;

    @Autowired
    private SetmealService setmealService;
    /**
     * 根据id删除分类，删除之前需要进行判断
     * @param id
     */
    @Override
    public void remove(Long id) {

        //查询当前分类是否关联了菜品，如果关联，抛出异常
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId , id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0){
            //已经关联了菜品，抛出一个业务异常
            throw new CustomException("当前分类下关联了菜品，不能够删除");
        }
        //查询当前分类是否关联了套餐，如果关联，抛出异常
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId , id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            //已经关联了套餐，抛出一个业务异常
            throw new CustomException("当前分类下关联了套餐，不能够删除");
        }
        //正常删除分类
        super.removeById(id);
    }
}
