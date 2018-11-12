package com.example.datasources;

import com.example.datasources.annotation.DataSource;
import com.example.entity.UserEntity;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 测试
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017/9/16 23:10
 */
@Service
public class DataSourceTestService {
    @Autowired
    private UserService userService;

    /**
     * 主库 写操作
     * @return
     */

    public void save(String mobile, String password){
        userService.save(mobile,password);
    }

    @DataSource(name = "read")
    public UserEntity queryObject2(Long userId){
        return userService.queryObject(userId);
    }

    @DataSource(name = "read")
    public UserEntity queryObject3(Long userId){
        return userService.queryObject(userId);
    }

}
