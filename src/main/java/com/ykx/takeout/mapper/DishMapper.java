package com.ykx.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykx.takeout.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created on 2022/10/6.
 *
 * @author KaiXuan Yang
 */
@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
