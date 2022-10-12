package com.ykx.takeout.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ykx.takeout.entity.Category;
import org.springframework.stereotype.Service;

/**
 * Created on 2022/10/6.
 *
 * @author KaiXuan Yang
 */
public interface CategoryService extends IService<Category> {
    public void remove(Long id);
}
