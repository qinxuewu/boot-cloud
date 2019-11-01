package com.github.inventory.service.impl;


import com.alibaba.fastjson.JSONObject;
import com.github.inventory.mapper.UserMapper;
import com.github.inventory.model.User;
import com.github.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 功能描述: 用户Service实现类
 * @author: qinxuewu
 * @date: 2019/11/1 10:42
 * @since 1.0.0
 */

@Service("userService")
public class UserServiceImpl  implements UserService {
    @Resource
    private UserMapper userMapper;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Override
    public User findUserInfo() {
        return userMapper.findUserInfo().get(0);
    }

    @Override
    public User getCachedUserInfo() {
        redisTemplate.opsForValue().set("cached_user_lisi","{\"name\": \"lisi\", \"age\":28}");

        String userJSON = redisTemplate.opsForValue().get("cached_user_lisi");
        JSONObject userJSONObject = JSONObject.parseObject(userJSON);

        User user = new User();
        user.setName(userJSONObject.getString("name"));
        user.setAge(userJSONObject.getInteger("age"));
        return user;
    }
}
