package com.ykx.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykx.takeout.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created on 2022/9/27.
 *
 * @author KaiXuan Yang
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
