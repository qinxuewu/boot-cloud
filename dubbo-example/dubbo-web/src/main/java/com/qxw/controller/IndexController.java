package com.qxw.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.qxw.service.UserService;

/**
 * ����������
 *
 * @author qxw
 * 2018��1��10��
 */

@Controller
public class IndexController {

    @Autowired
    private UserService userService;

    /**
     * ����������
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/index", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public String index(HttpServletRequest request, HttpServletResponse response) {
        String name = request.getParameter("name");
        String result = userService.getName(name);
        return result;
    }

}
