package com.github.cache.hystrix;

import com.alibaba.fastjson.JSONObject;
import com.github.cache.model.ProductInfo;
import com.github.cache.model.ShopInfo;
import com.github.cache.service.CacheService;
import com.github.cache.spring.SpringContextHolder;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import org.springframework.data.redis.core.RedisTemplate;

/**
 *  基于hystrix对Redis获取缓存的操作 进行资源隔离
 *
 * @author qinxuewu
 * @create 19/11/17下午12:36
 * @since 1.0.0
 */
public class GetProductInfoFromReidsCacheCommand extends HystrixCommand<ProductInfo> {
    private Long productId;

    public  GetProductInfoFromReidsCacheCommand(Long  productId){
        // command，都可以设置一个自己的名称，同时可以设置一个自己的组
        // 建服务独有的key并且保存到HystrixCommandGroupKey的InternMap
        // command threadpool -> command group -> command key
        // command group，代表了某一个底层的依赖服务，合理，一个依赖服务可能会暴露出来多个接口，每个接口就是一个command key
        // command group，在逻辑上去组织起来一堆command key的调用，统计信息，成功次数，timeout超时次数，失败次数，可以看到某一个服务整体的一些访问情况
        // command group，一般来说，推荐是根据一个服务去划分出一个线程池，command key默认都是属于同一个线程池的

        super(HystrixCommandGroupKey.Factory.asKey("RedisGroup"));
        this.productId = productId;
    }


    @Override
    protected ProductInfo run() {
        String key = "product_info_" + productId;
        RedisTemplate<String,String> redisTemplate = (RedisTemplate) SpringContextHolder.getApplicationContext().getBean("redisTemplate");
        String json =   redisTemplate.opsForValue().get(key);
        return JSONObject.parseObject(json, ProductInfo.class);
    }


    /**
     *  fail silent模式，fallback里面直接返回一个空值，比如一个null
     *  在外面调用redis的代码（CacheService类），是感知不到redis的访问异常的，只要你把timeout、熔断、熔断恢复、降级，都做好了
     *
     *  可能会出现的情况是，当redis集群崩溃的时候，CacheService获取到的是大量的null空值
     *
     *  根据这个null空值，我们还可以去做多级缓存的降级访问，nginx本地缓存，redis分布式集群缓存，ehcache本地缓存，CacheController
     * @return
     */
    @Override
    protected ProductInfo getFallback() {
        return null;
    }
}
