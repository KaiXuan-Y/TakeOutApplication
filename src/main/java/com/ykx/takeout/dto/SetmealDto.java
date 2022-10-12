package com.ykx.takeout.dto;


import com.ykx.takeout.entity.Setmeal;
import com.ykx.takeout.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
