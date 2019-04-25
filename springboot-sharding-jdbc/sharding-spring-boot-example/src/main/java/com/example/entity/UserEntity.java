package com.example.entity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 * @author qinxuewu
 * @version 1.00
 * @time  23/4/2019 下午 5:34
 * @email 870439570@qq.com
 */

@Entity(name = "user")
public class UserEntity {

    @Id
    private int id;
    @Column(length = 32)
    private String name;
    @Column(length = 16)
    private int age;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                '}';
    }

    public UserEntity(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
}
