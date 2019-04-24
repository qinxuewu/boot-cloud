package com.company.shardingjdbc.controller;

import com.company.shardingjdbc.domain.Order;
import com.company.shardingjdbc.mapper.OrderMapper;
import com.company.shardingjdbc.repository.OrderRepository;
import com.dangdang.ddframe.rdb.sharding.keygen.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private KeyGenerator keyGenerator;

    @RequestMapping("/create")
    public Object create() {
//        for (int i = 0; i < 10; i++) {
//            Order order = new Order();
//            order.setUserId((long) i);
//            order.setOrderId((long) i);
//            orderRepository.save(order);
//        }
//        for (int i = 10; i < 20; i++) {
//            Order order = new Order();
//            order.setUserId((long) i + 1);
//            order.setOrderId((long) i);
//            orderRepository.save(order);
//        }

//        for (int i = 0; i < 30; i++) {
//            Order order = new Order();
//            order.setOrderId(keyGenerator.generateKey().longValue());
//            order.setUserId(keyGenerator.generateKey().longValue());
//            orderRepository.save(order);
//        }

        return "success";
    }

    @RequestMapping("/insert")
    public Object insert() {
        for (int i = 20; i < 30; i++) {
            Order order = new Order();
            order.setUserId((long) i);
            order.setOrderId((long) i);
            orderMapper.insert(order);
        }
        for (int i = 30; i < 40; i++) {
            Order order = new Order();
            order.setUserId((long) i + 1);
            order.setOrderId((long) i);
            orderMapper.insert(order);
        }

        return "success";
    }

    @RequestMapping("queryById")
    public List<Order> queryById(String orderIds) {
        List<String> strings = Arrays.asList(orderIds.split(","));
        List<Long> orderIdList = strings.stream().map(item -> Long.parseLong(item)).collect(Collectors.toList());
        return orderMapper.queryById(orderIdList);
    }


    @RequestMapping("query")
    private Object queryAll() {
        return orderRepository.findAll();
    }
}
