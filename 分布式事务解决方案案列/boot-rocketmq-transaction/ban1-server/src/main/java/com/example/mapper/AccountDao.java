package com.example.mapper;
import org.apache.ibatis.annotations.*;

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
     * 扣减账户余额  本地事务提交
     * @param userId 用户id
     * @param money 金额
     */
    @Update("UPDATE account SET residue = residue - #{money} where user_id = #{userId}")
    int decrease(@Param("userId") Long userId, @Param("money") double money);


    /**
     *  幂等效验
     * @param txNo
     * @return
     */
    @Select("select count(1) from  de_duplication where tx_no=#{txNo}")
    int  isExisTx(@Param("txNo") String txNo);


    /**
     * 添加转账事务
     * @param txNo
     * @return
     */
    @Insert("insert into de_duplication values(#{txNo},now(0));")
    int addTx(@Param("txNo") String txNo);
}
