package com.company.shardingjdbc.repository;


import com.company.shardingjdbc.domain.Order;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
