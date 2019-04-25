package com.hjc.sharding.demo.controller;

import com.hjc.sharding.demo.entity.Member;
import com.hjc.sharding.demo.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created with Intellij IDEA.
 * @author hjc
 * @version 2018/5/31
 */
@RestController
@RequestMapping("member")
public class MemberController {

    @Autowired
    private MemberService memberService;

    @PostMapping("save")
    public Boolean save(@RequestBody Member member) {
        return memberService.save(member);
    }

    @GetMapping("selectById")
    public Member selectById(String id) {
        return memberService.selectById(id);
    }
}
