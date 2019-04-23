package com.example.controller;
import com.example.entity.UserEntity;
import com.example.service.UserService;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.List;


/**
 *
 * @author qinxuewu
 * @version 1.00
 * @time  22/4/2019 下午 6:37
 * @email 870439570@qq.com
 */
@RestController
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/save")
    public String saveUser() {
        UserEntity user = new UserEntity(1, "张三", 22);
        userService.saveUser(user);
        return "success";
    }

    @PostMapping("/getUser")
    public String getUsers() {
        List<UserEntity> users = userService.getUsers();
        return new Gson().toJson(users);
    }

}
