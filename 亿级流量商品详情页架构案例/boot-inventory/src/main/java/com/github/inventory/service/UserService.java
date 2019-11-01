package com.github.inventory.service;

import com.github.inventory.model.User;


/**
 * 功能描述: 用户Service接口
 * @author: qinxuewu
 * @date: 2019/11/1 10:40
 * @since 1.0.0 
 */
public interface UserService {

    /**
     * 查询用户信息
     * @return 用户信息
     */
    public User findUserInfo();

    /**
     * 查询redis中缓存的用户信息
     * @return
     */
    public User getCachedUserInfo();

}
