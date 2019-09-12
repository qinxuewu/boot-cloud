package com.github.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * 功能描述:  登录成功处理
 *
 *  如果需要自定义登录成功、失败的状态，我们仅需要了解两个handle处理器：
 *      AuthenticationSuccessHandler和AuthenticationFailureHandler。
 *      他们两个分别用于登录成功和登录失败的处理，我们仅需要创建两个类分别实现这两个接口然后
 * @author: qinxuewu
 * @date: 2019/9/11 12:16
 * @since 1.0.0
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler  {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandlerImpl.class);

    private RequestCache requestCache = new HttpSessionRequestCache();
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        logger.info("登录成功");
        // 输出json
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(objectMapper.writeValueAsString(authentication));

        // 在登录成功后做页面的跳转
//        SavedRequest savedRequest = requestCache.getRequest(httpServletRequest, httpServletResponse);
//        redirectStrategy.sendRedirect(httpServletRequest, httpServletResponse, savedRequest.getRedirectUrl());
    }
}
