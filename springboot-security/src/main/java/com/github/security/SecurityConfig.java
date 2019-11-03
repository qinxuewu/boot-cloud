package com.github.security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


/**
 * 功能描述: Security配置
 * @author: qinxuewu
 * @date: 2019/9/11 11:26
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)  //  启用方法级别的权限认证
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandlerImpl;
    @Autowired
    private AuthenticationFailureHandlerImpl authenticationFailureHandlerImpl;
    @Autowired
    private SessionExpiredStrategy sessionExpiredStrategy;
    @Autowired
    private LogOutSuccessHandler logOutSuccessHandler;
    @Autowired
    private AuthenticationAccessDeniedHandler authenticationAccessDeniedHandler;

    @Autowired
    private  MyUserDetailService myUserDetailService;
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        /**
         * fromLogin(): 表单认证
         * httpBasic(): 弹出框认证
         * authorizeRequests() 身份认证请求
         * anyRequest(): 所有请求
         * authenticated(): 身份认证
         * loginPage(): 登录页面地址（因为SpringBoot无法直接访问页面，所以这通常是一个路由地址）
         * loginProcessingUrl(): 登录表单提交地址
         * .csrf().disable(): 关闭Spring Security的跨站请求伪造的功能
         */

        http.exceptionHandling()
                    // 权限控制处理
                    .accessDeniedHandler(authenticationAccessDeniedHandler)
                .and()
                     .formLogin().loginPage("/login")
                    // 登录表单提交地址
                    .loginProcessingUrl("/auth/login")
                    // 处理登录成功
                    .successHandler(authenticationSuccessHandlerImpl)
                    // 处理登录失败
                    .failureHandler(authenticationFailureHandlerImpl)
                .and()
                    // 授权配置
                    .authorizeRequests()
                    // 无需认证的请求路径
                    .antMatchers(
                            "/login",
                            "/session/invalid",
                            "/css/**",
                            "/js/**",
                            "/images/**").permitAll()
                // 其他地址的访问均需验证权限
                    .anyRequest().authenticated()
                .and()
                    // 添加 Session管理器
                    .sessionManagement().invalidSessionUrl("/login").maximumSessions(1).expiredSessionStrategy(sessionExpiredStrategy)
                .and()
                .and()
                  // 退出
                    .logout().logoutUrl("/signout")
                    // .logoutSuccessUrl("/signout/success")
                    .logoutSuccessHandler(logOutSuccessHandler)
                    // 删除session
                    .deleteCookies("JSESSIONID")
                .and()
                .csrf().disable();
    }



    /**
     * 配置登录验证
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(myUserDetailService);
    }

    /**
     * 密码加密
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
