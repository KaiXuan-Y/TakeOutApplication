package com.ykx.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ykx.takeout.common.R;
import com.ykx.takeout.dto.DishDto;
import com.ykx.takeout.entity.Category;
import com.ykx.takeout.entity.Dish;
import com.ykx.takeout.entity.DishFlavor;
import com.ykx.takeout.service.CategoryService;
import com.ykx.takeout.service.DishFlavorService;
import com.ykx.takeout.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 菜品管理
 * Created on 2022/10/8.
 *
 * @author KaiXuan Yang
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private DishService dishService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){

        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page , int pageSize, String name){
        //构造分页构造器对象
        Page<Dish> pageInfo = new Page<>(page , pageSize);
        Page<DishDto> dishDtoPageInfo = new Page<>();
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null , Dish::getName , name);
        queryWrapper.orderByAsc(Dish::getUpdateTime);
        dishService.page(pageInfo , queryWrapper);
        //对象拷贝
        BeanUtils.copyProperties(pageInfo , dishDtoPageInfo,"records");

        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = null;
        list = records.stream().map((item) ->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item , dishDto);
            Long categoryId = item.getCategoryId(); //分类id
            Category category = categoryService.getById(categoryId);
            if (category != null){
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
            }


            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPageInfo.setRecords(list);

        return R.success(dishDtoPageInfo);
    }

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        return R.success(dishDto);

    }
    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto){

        dishService.updateWithFlavor(dishDto);
        return R.success("菜品修改成功");
    }

    @PostMapping("/status/{status}")
    public R<String> stop(@PathVariable Integer status ,  @RequestParam Long ids){
        log.info(ids.toString());

        return null;

    }

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId()!= null,Dish::getCategoryId , dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus , 1);
        queryWrapper.orderByAsc(Dish::getSort).orderByAsc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        List<DishDto> dishDtos = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flavorLambdaQueryWrapper.eq(dishId != null, DishFlavor::getDishId, dishId);
            List<DishFlavor> dishFlavors = dishFlavorService.list(flavorLambdaQueryWrapper);
            dishDto.setFlavors(dishFlavors);
            return dishDto;

        }).collect(Collectors.toList());
        return R.success(dishDtos);
    }


}
