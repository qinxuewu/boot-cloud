package com.github.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;


/**
 * 功能描述: 账户操作
 * @author: qinxuewu
 * @date: 2019/11/25 17:19
 * @since 1.0.0
 */
@Mapper
public interface AccountDao {

    /**
     * 扣减账户余额
     * @param userId 用户id
     * @param money 金额
     */
    @Update("UPDATE account SET residue = residue - #{money},used = used + #{money} where user_id = #{userId}")
    void decrease(@Param("userId") Long userId, @Param("money") BigDecimal money);
}
