package com.github.cache.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

/**
 * 开启缓存
 *
 * @author qinxuewu
 * @version 1.00
 * @cache这个相当于save（）操作
 * @cachePut相当于update（）操作，只要他标示的方法被调用，那么都会缓存起来，而@cache则是先看下有没已经缓存了，然后再选择是否执行方法。
 * @CacheEvict相当于delete（）操作。用来清除缓存用的。
 * @time 26/3/2019 下午 7:01
 * @email 870439570@qq.com
 */

@Configuration
@EnableCaching
public class EhCacheConfig {
}