package com.sun.shard.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sun.shard.bean.User;
import com.sun.shard.mapper.UserMapper;
import com.sun.shard.service.IUserService;


@Service
public class UserServiceImpl implements IUserService{

	
		@Autowired
		private UserMapper userMapper;


		public void save(User user) {
			user.setCreateTime(new Date());
			userMapper.save(user);
		}


		public User get(Long id) {
			return userMapper.get(id);
		}
		
		
}
