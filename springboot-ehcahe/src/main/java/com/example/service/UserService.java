package com.example.service;
import com.example.entity.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * https://blog.csdn.net/qq_28988969/article/details/78210873
 *
 * @author qinxuewu
 * @version 1.00
 * @time  26/3/2019 下午 7:03
 * @email 870439570@qq.com
 */
@Service
@CacheConfig(cacheNames = "user")
public class UserService {

    @Autowired
    private UserRepository  userRepository;


    public void save(User user) {
        userRepository.save(user);
    }

    /**
     * @cachePut根据方法的请求参数对其结果进行缓存，和 @Cacheable 不同的是，它每次都会触发真实方法的调用。适用于更新和插入；
     * @return
     */
    @CacheEvict(key = "'user_'+ #userName")
    public int update(String userName,int id) {
        return userRepository.updateUserByuserNameAndId(userName,id);
    }

    /**
     * @CacheEvict相当于delete（）操作。用来清除缓存用的。
     * @return
     */
    @CacheEvict(key = "'user_'+ #userName")
    public void delete(String userName) {
        userRepository.deleteByUserName(userName);
    }

    /**
     * @Cacheable：应用到读取数据的方法上，即可缓存的方法，如查找方法：先从缓存中读取，
     * 如果没有再调 用方法获取数据，然后把数据添加到缓存中，适用于查找；
     * @return
     */
    @Cacheable(key = "'user_'+ #userName")
    public User select(String userName) {
       return userRepository.findByUserName(userName);
    }

}
