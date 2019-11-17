package com.github.cache.filter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;

import java.io.IOException;

/**
 * 功能描述:   hystrix请求上下文过滤器
 * @author: qinxuewu
 * @date: 2019/11/13 14:59
 * @since 1.0.0 
 */
public class HystrixRequestContextFilter implements  Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    /**
     *  HystrixCommand和HystrixObservableCommand都可以指定一个缓存key，然后hystrix会自动进行缓存，接着在同一个request context内，再次访问的时候，就会直接取用缓存
     * @param request
     * @param response
     * @param chain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            context.shutdown();
        }
    }


    @Override
    public void destroy() {

    }
}
