package com.ykx.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ykx.takeout.common.R;
import com.ykx.takeout.entity.Category;
import com.ykx.takeout.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * Created on 2022/10/6.
 *
 * @author KaiXuan Yang
 */
@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody Category category){
        log.info("category:{}",category);
        categoryService.save(category);
        return R.success("新增分类成功");
    }
    @GetMapping("/page")
    public R<Page> page(int page , int pageSize){
        Page<Category> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.orderByAsc(Category::getSort);

        //进行分页查询
        categoryService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);

    }
    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除id为{}的分类",ids);

        categoryService.remove(ids);
//        categoryService.removeById(ids);
        return R.success("删除成功");
    }
    @PutMapping
    public R<String> update(@RequestBody Category category){
        log.info("修改分类信息{}",category);
        categoryService.updateById(category);
        return R.success("修改分类信息成功");
    }

    /**
     * 根据条件查询分类数据
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list( Category category){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(category.getType() != null , Category::getType , category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByAsc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }


}
