package com.github.cache.service.impl;
import com.github.cache.model.ProductInfo;
import com.github.cache.service.CacheService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 缓存Service实现类
 * @author qinxuewu
 * @create 19/11/3下午12:44
 * @since 1.0.0
 */


@Service("cacheService")
public class CacheServiceImpl implements CacheService{

    /**
     * 缓存策略名称
     */
    public static final String CACHE_NAME = "local";

    /**
     * 将商品信息保存到本地缓存
     *  @cachePut根据方法的请求参数对其结果进行缓存，和 @Cacheable 不同的是，它每次都会触发真实方法的调用。适用于更新和插入；
     * @param productInfo
     * @return
     */
    @Override
    @CachePut(value = CACHE_NAME,key = "'key_'+#productInfo.getId()")
    public ProductInfo saveLocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    /**
     * 从本地缓存中获取商品信息
     * @param id
     * @return
     */
    @Override
    @Cacheable(value = CACHE_NAME,key = "'key_'+#id")
    public ProductInfo getLocalCache(Long id) {
        return null;
    }
}
