package com.example.config;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 定义配置
 * @author qinxuewu
 * @version 1.00
 * @time  24/4/2019 下午 5:56
 * @email 870439570@qq.com
 */
@ConfigurationProperties(prefix = "spring.person")
public class PersonProperties {


    private String name;
    private int age;
    private String sex = "M";


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }
}
