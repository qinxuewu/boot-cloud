package com.hjc.sharding.demo.service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hjc.sharding.demo.entity.Member;
import com.hjc.sharding.demo.mapper.MemberMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created with Intellij IDEA.
 * @author hjc
 * @version 2018/5/31
 */
@Service
public class MemberService extends ServiceImpl<MemberMapper, Member> {

    @Transactional(rollbackFor = Exception.class)
    public boolean save(Member member) {
        return super.insertOrUpdate(member);
    }
}