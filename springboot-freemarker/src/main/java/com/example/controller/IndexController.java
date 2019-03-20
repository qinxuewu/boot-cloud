package com.example.controller;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author qinxuewu
 * @version 1.00
 * @time  19/3/2019 下午 1:47
 * @email 870439570@qq.com
 */
@Controller
@RequestMapping(value = "/freemarker")
public class IndexController {
    private static Logger log = LoggerFactory.getLogger(IndexController.class);

    @RequestMapping(value = "/index")
    public ModelAndView toDemo(ModelAndView mv) {
        mv.addObject("name", "freemarker模板文件语法");
        mv.addObject("num",111);
        mv.addObject("falg",true);
        mv.addObject("str",null);
        JSONObject obj=new JSONObject();
        obj.put("name","qxw");
        obj.put("age",25);
        mv.addObject("user",obj);

        mv.addObject("htmlStr","<font color = 'red'>后天传入html字符串</font>");

        List<String> list = new ArrayList<String>();
        list.add("A");
        list.add("B");
        list.add("C");
        mv.addObject("list",list);

        Map<String,Object> maps=new HashMap<>();
        maps.put("map1",1);
        maps.put("map2",2);
        mv.addObject("maps",maps);
        mv.setViewName("freemarker");
        return mv;
    }

}
