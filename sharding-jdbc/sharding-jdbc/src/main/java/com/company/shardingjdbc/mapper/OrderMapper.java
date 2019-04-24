package com.company.shardingjdbc.mapper;


import com.company.shardingjdbc.domain.Order;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface OrderMapper {

    void insert(Order order);

    List<Order> queryById(@Param("orderIdList") List<Long> orderIdList);
}
