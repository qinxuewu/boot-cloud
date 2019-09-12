package com.github.security;
import com.github.entity.SysUser;
import com.github.mapper.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;


/**
 * 功能描述:   https://blog.csdn.net/qq_29580525/article/details/79317969
 * @author: qinxuewu
 * @date: 2019/9/11 18:02
 * @since 1.0.0 
 */
public class MyAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private SysUserMapper sysUserMapper;

    /** 注入我们自己定义的用户信息获取对象 */
    @Autowired
    private UserDetailsService userDetailService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // 这个获取表单输入中返回的用户名;
        String userName = authentication.getName();
        SysUser user = sysUserMapper.selectByName(userName);
        if (user == null) {
            throw new AuthenticationCredentialsNotFoundException("账号不存在");
        }
        // 这个是表单中输入的密码；
        String password = (String) authentication.getCredentials();

        // 这里构建来判断用户是否存在和密码是否正确

        SysUser userInfo=new SysUser();
        userInfo.setPassword("123456");
        userInfo.setUserName("qxw");

        if (!userInfo.getPassword().equals("123456")) {
            throw new BadCredentialsException("密码不正确");
        }
//        Collection<? extends GrantedAuthority> authorities = userInfo.getAuthorities();
        // 构建返回的用户登录成功的token
        return new UsernamePasswordAuthenticationToken(userInfo, password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));

    }

    @Override
    public boolean supports(Class<?> aClass) {
        // 这里直接改成retrun true;表示是支持这个执行
        return true;

    }
}
