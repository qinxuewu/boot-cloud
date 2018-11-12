package com.example;

import com.example.datasources.DataSourceTestService;
import com.example.entity.UserEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class DynamicDataSourceTest {
    @Autowired
    private DataSourceTestService dataSourceTestService;

    @Test
    public void test(){
        //数据源1
//        dataSourceTestService.save("13213212321321","123456");


        //数据源2
        UserEntity user1 = dataSourceTestService.queryObject2(2L);
        UserEntity user2 = dataSourceTestService.queryObject2(2L);
        System.out.println("数据源1读："+user1.getUsername());

        System.out.println("数据源2："+user2.getUsername());


    }

}
