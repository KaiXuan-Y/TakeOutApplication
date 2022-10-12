package com.ykx.takeout.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykx.takeout.common.R;
import com.ykx.takeout.dto.DishDto;
import com.ykx.takeout.entity.Dish;
import com.ykx.takeout.entity.DishFlavor;
import com.ykx.takeout.mapper.DishMapper;
import com.ykx.takeout.service.DishFlavorService;
import com.ykx.takeout.service.DishService;
import org.springframework.beans.BeanUtils;
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
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
    /**
     * 新增菜品，同时保存对应的口味数据
     * @param dishDto
     */

    @Autowired
    private DishFlavorService dishFlavorService;
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        //保存菜品的基本信息到dish表
        this.save(dishDto);
        Long disId = dishDto.getId(); //菜品id

        //菜品口味
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors = flavors.stream().map((item) ->{
            item.setDishId(disId);
            return item;
        }).collect(Collectors.toList());

        //保存信息到dishFlavor表
        dishFlavorService.saveBatch(flavors);

    }

    @Override
    public DishDto getByIdWithFlavor(Long id) {

        Dish byId = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(byId , dishDto);
        //查询对应的口味，从dish_flavor查询
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId , byId.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);


        return dishDto;
    }

    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        Long id = dishDto.getId();
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DishFlavor::getDishId , id);
        dishFlavorService.remove(queryWrapper);

        List<DishFlavor> flavors = dishDto.getFlavors();
        List<DishFlavor> list = flavors.stream().map((item) -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());

        dishFlavorService.saveBatch(list);

    }

}
