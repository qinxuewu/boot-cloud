package com.example.service;

import com.example.entity.UserEntity;

import java.util.List;

/**
 * @author qinxuewu
 * @version 1.00
 * @time  22/4/2019 下午 6:38
 * @email 870439570@qq.com
 */

public interface UserService {

    /**
     * 保存 user 对象
     *
     * @param user
     */
    void saveUser(UserEntity user);

    /**
     * 获取所有 user 对象
     *
     * @return
     */
    List<UserEntity> getUsers();
}
