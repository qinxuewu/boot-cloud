package com.github.security;
import com.github.entity.SysUser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.thymeleaf.util.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 功能描述:  自定义认证的过程需要实现Spring Security提供的UserDetailService接口，该接口只有一个抽象方法loadUserByUsername
 * 用于加载特定用户信息的，它只有一个接口通过指定的用户名去查询用户 相当于Shiro中的Subject
 * @author: qinxuewu
 * @date: 2019/9/11 11:40
 * @since 1.0.0
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MyUserDetailService implements UserDetailsService {
    private static final Logger logger = LoggerFactory.getLogger(MyUserDetailService.class);
    @Autowired
    private PasswordEncoder passwordEncoder;


    /**
     * Spring Security接收login请求调用UserDetailsService这个接口中的loadUserByUsername方法
     * loadUserByUsername根据传进来的用户名进行校验工作，最后将查询到的用户信息封装到UserDetails这个接口的实现类中
     *
     * @param username 用户名
     * @return
     * @throws UsernameNotFoundException 登录异常类
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String password = passwordEncoder.encode("123456");
        // 模拟一个用户，替代数据库获取逻辑
        SysUser user = new SysUser();
        user.setUserName(username);
        user.setPassword(password);
        logger.info("登录，用户名：{}, 密码：{}", username, password);
        List<GrantedAuthority> authorities = new ArrayList<>();
        // 模拟授予不同的权限
        if (StringUtils.equalsIgnoreCase("qxw", username)) {
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");
        } else {
            authorities = AuthorityUtils.commaSeparatedStringToAuthorityList("test");
        }
//        return new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
        return new User(username, user.getPassword(), user.isEnabled(),
                user.isAccountNonExpired(), user.isCredentialsNonExpired(),
                user.isAccountNonLocked(), authorities);
    }
}
