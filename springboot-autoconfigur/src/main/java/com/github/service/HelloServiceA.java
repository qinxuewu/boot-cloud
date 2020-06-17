package com.github.service;

public class HelloServiceA implements HelloService {
    @Override
    public void doSomething() {
        System.out.println("Hello A");
    }
}
