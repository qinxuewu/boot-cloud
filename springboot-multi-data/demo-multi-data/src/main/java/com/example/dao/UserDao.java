package com.example.dao;

import com.example.entity.UserEntity;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户

 */
@Mapper
public interface UserDao extends BaseDao<UserEntity> {

    UserEntity queryByMobile(String mobile);
}
