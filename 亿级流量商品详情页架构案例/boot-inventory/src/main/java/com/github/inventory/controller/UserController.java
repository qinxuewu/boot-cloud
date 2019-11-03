package com.github.inventory.controller;


import com.github.inventory.model.User;
import com.github.inventory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


/**
  * 功能描述: 用户Controller控制器
  * @author: qinxuewu
  * @date: 2019/11/1 11:09
  * @since 1.0.0 
  */
@Controller
public class UserController {
     @Autowired
     private UserService userService;

     @RequestMapping("/getUserInfo")
     @ResponseBody
     public User getUserInfo() {
         User user = userService.findUserInfo();
         return user;
     }

     @RequestMapping("/getCachedUserInfo")
     @ResponseBody
     public User getCachedUserInfo() {
         User user = userService.getCachedUserInfo();
         return user;
     }
}
