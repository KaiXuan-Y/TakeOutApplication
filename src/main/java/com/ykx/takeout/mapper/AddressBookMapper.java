package com.ykx.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykx.takeout.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created on 2022/10/11.
 *
 * @author KaiXuan Yang
 */
@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
