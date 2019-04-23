package com.example;
import com.example.entity.UserEntity;
import com.example.service.UserService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ShardingSpringBootMybatisExampleApplicationTests {

    @Autowired
    private UserService userService;


    @Test
    public void save() {
//        //主库添加数据
//        UserEntity user = new UserEntity(1, "张三", 22);
//        userService.saveUser(user);

        //从库读
        String result=getUsers();
        System.err.println(result);

    }


    public String getUsers() {
        List<UserEntity> users = userService.getUsers();
        return new Gson().toJson(users);
    }
}
