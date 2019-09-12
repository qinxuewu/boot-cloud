package com.github.security;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 功能描述: 自定义密码解析
 * @author: qinxuewu
 * @date: 2019/9/12 10:14
 * @since 1.0.0
 */
public class MyPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence charSequence) {
        //MD5Util.encode((String) charSequence);
        System.out.println(charSequence.toString());
        return null;
    }

    @Override
    public boolean matches(CharSequence charSequence, String s) {
        System.out.println(charSequence);
        System.out.println(s);
        return  s.equals(charSequence.toString());
    }
}
