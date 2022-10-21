package com.ykx.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ykx.takeout.common.R;
import com.ykx.takeout.dto.SetmealDto;
import com.ykx.takeout.entity.Category;
import com.ykx.takeout.entity.Setmeal;
import com.ykx.takeout.service.CategoryService;
import com.ykx.takeout.service.SetmealDishService;
import com.ykx.takeout.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 套餐管理
 * Created on 2022/10/10.
 *
 * @author KaiXuan Yang
 */
@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private SetmealService  setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;
    /**
     * 新增套餐
     * @param setmealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info(setmealDto.toString());
        setmealService.saveSetMeals(setmealDto);
        String mealKey = "meal_" + setmealDto.getCategoryId() +"_" + setmealDto.getStatus();
        redisTemplate.delete(mealKey);
        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R<Page> page(int page , int pageSize , String name){
        log.info("page:{} , pageSize:{}" , page,pageSize);
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPage = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null , Setmeal::getName , name);
        queryWrapper.orderByAsc(Setmeal::getUpdateTime);
        setmealService.page(pageInfo , queryWrapper);
        BeanUtils.copyProperties(pageInfo , dtoPage , "records");
        List<Setmeal> list = pageInfo.getRecords();
        List<SetmealDto> setmealDtoList = list.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item, setmealDto);
            Category byId = categoryService.getById(item.getCategoryId());
            if (byId != null){
                String label = byId.getName();
                setmealDto.setCategoryName(label);
            }

            return setmealDto;

        }).collect(Collectors.toList());

        dtoPage.setRecords(setmealDtoList);
        return R.success(dtoPage);
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){
        log.info("ids:{}" , ids);
        setmealService.removeWithDish(ids);
        return R.success("套餐删除成功");
    }


    @GetMapping("/list")
    public R<List<Setmeal>> list(Setmeal setmeal){
        log.info(setmeal.toString());
        List<Setmeal> list = null;
        String mealKey = "meal_"+ setmeal.getCategoryId() + "_"+setmeal.getStatus();
        list = (List<Setmeal>) redisTemplate.opsForValue().get(mealKey);
        if (list != null){
            return R.success(list);
        }
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null , Setmeal::getCategoryId , setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null , Setmeal::getStatus , setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        list = setmealService.list(queryWrapper);
        redisTemplate.opsForValue().set(mealKey , list , 60 , TimeUnit.MINUTES);
        return R.success(list);

    }


}
