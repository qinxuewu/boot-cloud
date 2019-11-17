package com.github.cache.service.impl;
import com.alibaba.fastjson.JSONObject;
<<<<<<< HEAD
import com.github.cache.hystrix.GetProductInfoFromReidsCacheCommand;
=======
>>>>>>> 2c2c2bdfa9b80e3a50c462378e3c72473beea707
import com.github.cache.model.ProductInfo;
import com.github.cache.model.ShopInfo;
import com.github.cache.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * 缓存Service实现类
 * @author qinxuewu
 * @create 19/11/3下午12:44
 * @since 1.0.0
 */


@Service("cacheService")
public class CacheServiceImpl implements CacheService{
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
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


    /**
     * 将商品信息保存到本地的ehcache缓存中
     * @param productInfo
     */
    @Override
    @CachePut(value = CACHE_NAME, key = "'product_info_'+#productInfo.getId()")
    public ProductInfo saveProductInfo2LocalCache(ProductInfo productInfo) {
        return productInfo;
    }

    /**
     * 从本地ehcache缓存中获取商品信息
     * @param productId
     * @return
     */
    @Override
    @Cacheable(value = CACHE_NAME, key = "'product_info_'+#productId")
    public ProductInfo getProductInfoFromLocalCache(Long productId) {
        return null;
    }

    /**
     * 将店铺信息保存到本地的ehcache缓存中
     * @param shopInfo
     */
    @CachePut(value = CACHE_NAME, key = "'shop_info_'+#shopInfo.getId()")
    @Override
    public ShopInfo saveShopInfo2LocalCache(ShopInfo shopInfo) {
        return shopInfo;
    }

    /**
     * 从本地ehcache缓存中获取店铺信息
     * @param shopId
     * @return
     */
    @Override
    @Cacheable(value = CACHE_NAME, key = "'shop_info_'+#shopId")
    public ShopInfo getShopInfoFromLocalCache(Long shopId) {
        return null;
    }

    /**
     * 将商品信息保存到redis中
     * @param productInfo
     */
    @Override
    public void saveProductInfo2ReidsCache(ProductInfo productInfo) {
        String key = "product_info_" + productInfo.getId();

        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(productInfo));
    }

    /**
     * 将店铺信息保存到redis中
     * @param shopInfo
     */
    @Override
    public void saveShopInfo2ReidsCache(ShopInfo shopInfo) {
        String key = "shop_info_" + shopInfo.getId();
        redisTemplate.opsForValue().set(key, JSONObject.toJSONString(shopInfo));
    }

    @Override
    public ProductInfo getProductInfoFromReidsCache(Long productId) {
<<<<<<< HEAD
//        String key = "product_info_" + productId;
//        String json =   redisTemplate.opsForValue().get(key);
//        return JSONObject.parseObject(json, ProductInfo.class);

        // 基于hystrix对Redis获取缓存的操作 进行资源隔离
        GetProductInfoFromReidsCacheCommand cacheCommand=new GetProductInfoFromReidsCacheCommand(productId);
        return  cacheCommand.execute();

=======
        String key = "product_info_" + productId;
        String json =   redisTemplate.opsForValue().get(key);
        return JSONObject.parseObject(json, ProductInfo.class);
>>>>>>> 2c2c2bdfa9b80e3a50c462378e3c72473beea707
    }

    @Override
    public ShopInfo getShopInfoFromReidsCache(Long shopId) {
        String key = "shop_info_" + shopId;
        String json = redisTemplate.opsForValue().get(key);
        return JSONObject.parseObject(json, ShopInfo.class);
    }
}
