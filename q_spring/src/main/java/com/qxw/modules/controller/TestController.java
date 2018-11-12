package com.qxw.modules.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.qxw.modules.service.TestService;
import com.qxw.webmvc.annotaion.QAutowired;
import com.qxw.webmvc.annotaion.QController;
import com.qxw.webmvc.annotaion.QRequestMapping;
import com.qxw.webmvc.annotaion.QRequestParam;
import com.qxw.webmvc.bean.View;

@QController
@QRequestMapping("/test")
public class TestController {

    @QAutowired
    public TestService testService;

    @QRequestMapping("/index.do")
    public View index(HttpServletRequest req) {
        System.out.println("index------------------------");
        return new View("/index.jsp");
    }

    @QRequestMapping("/add")
    public void add(HttpServletResponse res) {
        System.out.println("add------------------------");
//		try {
////			res.getWriter().write("addd");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
    }

}
