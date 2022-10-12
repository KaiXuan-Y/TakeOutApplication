package com.ykx.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ykx.takeout.common.BaseContext;
import com.ykx.takeout.common.R;
import com.ykx.takeout.entity.ShoppingCart;
import com.ykx.takeout.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Created on 2022/10/12.
 *
 * @author KaiXuan Yang
 */
@Slf4j
@RestController
@RequestMapping("/shoppingCart")
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService shoppingCartService;
    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){
        log.info(shoppingCart.toString());
        //1.设置userId
        Long userId = BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //2.查询当前用户的购物车中是否有菜品或者套餐
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId , userId);
        if(shoppingCart.getDishId() != null){
            //查询的是菜品
            queryWrapper.eq(ShoppingCart::getDishId , shoppingCart.getDishId());

        }else {
            queryWrapper.eq(ShoppingCart::getSetmealId , shoppingCart.getSetmealId());
        }
        ShoppingCart serviceOne = shoppingCartService.getOne(queryWrapper);



        //3.如果已经存在数量+1
        if (serviceOne != null){
            Integer number = serviceOne.getNumber();
            serviceOne.setNumber(number + 1);
            shoppingCartService.updateById(serviceOne);
        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            serviceOne = shoppingCart;
        }
        //4.不存在数量为1 ， 保存
        return R.success(serviceOne);
    }

    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null , ShoppingCart::getUserId , userId);
        queryWrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(queryWrapper);
        return R.success(list);
    }

    @DeleteMapping("/clean")
    public R<String> clean(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId , userId);
        shoppingCartService.remove(queryWrapper);
        return R.success("删除成功");
    }
}
