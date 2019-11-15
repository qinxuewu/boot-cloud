package com.github.cache.hystrix;


import com.github.cache.cache.BrandCache;
import com.github.cache.controller.CacheController;
import com.netflix.hystrix.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述:  获取品牌名称的command
 * @author: qinxuewu
 * @date: 2019/11/13 16:53
 * @since 1.0.0 
 */
public class GetBrandNameCommand  extends HystrixCommand<String> {
    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);
    private Long brandId;

    public GetBrandNameCommand(Long brandId) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("BrandInfoService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GetBrandNameCommand"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetBrandInfoPool"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        .withCoreSize(15)
                        .withQueueSizeRejectionThreshold(10))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        // 设置了HystrixCommand.getFallback()最大允许的并发请求数量，默认值是10，也是通过semaphore信号量的机制去限流
                        // 如果超出了这个最大值，那么直接被reject
                        .withFallbackIsolationSemaphoreMaxConcurrentRequests(15))
        );
        this.brandId = brandId;
    }

    @Override
    protected String run() throws Exception {
        // 调用一个品牌服务的接口
        // 如果调用失败了，报错了，那么就会去调用fallback降级机制
        throw new Exception();
    }

    @Override
    protected String getFallback() {
        logger.info("【getFallback降级-》从本地缓存获取过期的品牌数据】brandId=" + brandId);
        return BrandCache.getBrandName(brandId);
    }
}
