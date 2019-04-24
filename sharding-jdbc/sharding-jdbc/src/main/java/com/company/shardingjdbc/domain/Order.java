package com.company.shardingjdbc.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table(name = "t_order")
@Data
public class Order {
    @Id
    private Long orderId;

    private Long userId;
}
