package com.example.service.impl;
import com.example.entity.UserEntity;
import com.example.repository.UserRepository;
import com.example.service.UserService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author qinxuewu
 * @version 1.00
 * @time  22/4/2019 下午 6:38
 * @email 870439570@qq.com
 */

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserRepository userRepository;

    @Override
    public void saveUser(UserEntity user) {
        userRepository.save(user);
    }

    @Override
    public List<UserEntity> getUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users;
    }
}
