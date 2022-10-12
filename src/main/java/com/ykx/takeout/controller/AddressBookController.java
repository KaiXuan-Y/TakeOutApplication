package com.ykx.takeout.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.ykx.takeout.common.BaseContext;
import com.ykx.takeout.common.R;
import com.ykx.takeout.entity.AddressBook;
import com.ykx.takeout.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created on 2022/10/11.
 *
 * @author KaiXuan Yang
 */
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {
    @Autowired
    private AddressBookService addressBookService;
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){
        addressBook.setUserId(BaseContext.getCurrentId());
        log.info(addressBook.toString());
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }
    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null , AddressBook::getUserId , userId);
        queryWrapper.eq(AddressBook::getIsDefault , 1);
        AddressBook one = addressBookService.getOne(queryWrapper);
        return R.success(one);
    }
    @PutMapping
    public R<String> saveEdit(@RequestBody AddressBook addressBook){
        addressBookService.updateById(addressBook);
        return R.success("修改成功");
    }
    @PutMapping("/default")
    public R<AddressBook> getDefault(@RequestBody AddressBook addressBook){
        log.info(addressBook.toString());
        LambdaUpdateWrapper<AddressBook> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(AddressBook::getUserId , BaseContext.getCurrentId());
        updateWrapper.set(AddressBook::getIsDefault , 0);
        addressBookService.update(updateWrapper);
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success(addressBook);

    }
    @GetMapping("/list")
    public R<List<AddressBook>> list(){
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null , AddressBook::getUserId , userId);
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return R.success(list);

    }
    @GetMapping("/{ids}")
    public R<AddressBook> editAddress(@PathVariable String ids){
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ids != null , AddressBook::getId , ids);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return R.success(addressBook);
    }
    @DeleteMapping
    public R<String> delete(String ids){
        addressBookService.removeById(ids);
        return R.success("删除成功");

    }

}
