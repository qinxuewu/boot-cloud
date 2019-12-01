package com.example.server;


public interface AccountService {

    /**
     * 扣减账户余额 -
     * @param userId 用户id
     * @param money 金额
     */
    void decrease(Long userId, double money,String txNo);


    /**
     * 发送MQ转账消息
     * @param userId  账号
     * @param amount  变动金额
     * @param txNo  事务号
     */
    void sendUpdateAccontChange(Long userId,Long toUserId,double amount,String txNo);

}
