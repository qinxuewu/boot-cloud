package com.example.service;

import com.example.config.PersonProperties;

/**
 *
 *  每个starter都有自己的功能，例如在spring-boot-starter-jdbc中最重要的类时JdbcTemplate
 *  这里使用PersonService来定义helloworld-spring-boot-starter的功能，这里通过一个sayHello来模拟一个功能。
 * @author qinxuewu
 * @version 1.00
 * @time  24/4/2019 下午 5:58
 * @email 870439570@qq.com
 */
public class PersonService {

    private PersonProperties properties;

    public PersonService() {
    }

    public PersonService(PersonProperties properties) {
        this.properties = properties;
    }

    public void sayHello(){
        System.out.println("大家好，我叫: " + properties.getName() + ", 今年" + properties.getAge() + "岁"
                + ", 性别: " + properties.getSex());
    }
}