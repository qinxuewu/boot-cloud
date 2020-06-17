package com.github.repository;
import com.github.annotation.MyRepository;
import org.springframework.stereotype.Repository;

@MyRepository(value = "myUserRepository") // Bean 名称
//@Repository(value = "myUserRepository") // Bean 名称
public class MyUserRepository {
}
