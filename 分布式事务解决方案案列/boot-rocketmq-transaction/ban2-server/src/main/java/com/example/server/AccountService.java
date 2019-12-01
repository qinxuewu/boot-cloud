package com.example.server;

public interface AccountService {

    /**
     * 增加ban2余额
     * @param userId 用户id
     * @param money 金额
     */
    void decrease(Long userId, double money, String txNo);



}
