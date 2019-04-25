package com.example.repository;
import com.example.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author qinxuewu
 * @version 1.00
 * @time  22/4/2019 下午 6:36
 * @email 870439570@qq.com
 */
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
}
