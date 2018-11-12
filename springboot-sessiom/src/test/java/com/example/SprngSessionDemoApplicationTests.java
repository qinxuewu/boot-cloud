package com.example;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.servlet.http.HttpSession;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SprngSessionDemoApplicationTests {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    public void contextLoads() {

        stringRedisTemplate.opsForValue().set("dasdsadsadsadsa", "111");
	        Assert.assertEquals("111", stringRedisTemplate.opsForValue().get("aaa"));

    }

}
