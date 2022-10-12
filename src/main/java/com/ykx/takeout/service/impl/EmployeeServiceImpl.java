package com.ykx.takeout.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ykx.takeout.entity.Employee;
import com.ykx.takeout.mapper.EmployeeMapper;
import com.ykx.takeout.service.EmployeeService;
import org.springframework.stereotype.Service;


/**
 * Created on 2022/9/27.
 *
 * @author KaiXuan Yang
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper , Employee> implements EmployeeService {


}
