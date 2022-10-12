package com.ykx.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykx.takeout.dto.SetmealDto;
import com.ykx.takeout.entity.Setmeal;

import java.util.List;

/**
 * Created on 2022/10/6.
 *
 * @author KaiXuan Yang
 */
public interface SetmealService extends IService<Setmeal> {
    public void saveSetMeals(SetmealDto setmealDto);

    /**
     * 删除套餐，同时删除套餐和菜品的关联关系
     * @param ids
     */
    public void removeWithDish(List<Long> ids);
}
