package com.ykx.takeout.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ykx.takeout.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;

/**
 * Created on 2022/10/12.
 *
 * @author KaiXuan Yang
 */
@Mapper
public interface OrderDetailMapper extends BaseMapper<OrderDetail> {
}
