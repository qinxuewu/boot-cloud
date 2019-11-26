package com.github.mapper;

import com.github.entity.Order;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;


/**
 * 功能描述: 订单
 * @author: qinxuewu
 * @date: 2019/11/25 17:19
 * @since 1.0.0
 */
@Mapper
public interface OrderDao {

    /**
     * 创建订单
     * @param order
     * @return
     */
    @Insert("INSERT INTO `order` (`user_id`,`product_id`,`count`,`money`,`status`) VALUES " +
            " (#{order.userId}, #{order.productId}, #{order.count}, #{order.money},0) ")
    void create(@Param("order")Order order);

    /**
     * 修改订单金额
     * @param userId
     * @param money
     */
    @Update(" UPDATE `order` SET money = money - #{money},status = 1 where user_id = #{userId} and status = #{status} ")
    void update(@Param("userId") Long userId, @Param("money") BigDecimal money, @Param("status") Integer status);
}
