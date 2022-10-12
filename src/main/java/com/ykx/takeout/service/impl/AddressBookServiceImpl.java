package com.ykx.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykx.takeout.entity.AddressBook;
import com.ykx.takeout.mapper.AddressBookMapper;
import com.ykx.takeout.service.AddressBookService;
import org.springframework.stereotype.Service;

/**
 * Created on 2022/10/11.
 *
 * @author KaiXuan Yang
 */
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper , AddressBook> implements AddressBookService {
}
