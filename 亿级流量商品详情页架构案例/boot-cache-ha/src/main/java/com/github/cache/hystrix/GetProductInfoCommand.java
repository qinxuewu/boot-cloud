package com.github.cache.hystrix;


import com.alibaba.fastjson.JSONObject;
import com.github.cache.controller.CacheController;
import com.github.cache.http.HttpClientUtils;
import com.github.cache.model.ProductInfo;
import com.netflix.hystrix.*;
import com.netflix.hystrix.strategy.concurrency.HystrixConcurrencyStrategyDefault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述:  获取商品信息的comman
 *   HystrixCommand：是用来获取一条数据的
 * @author: qinxuewu
 * @date: 2019/11/12 14:32
 * @since 1.0.0
 */
public class GetProductInfoCommand extends HystrixCommand<ProductInfo> {
    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);
    private  Long productId;
    private static final HystrixCommandKey KEY = HystrixCommandKey.Factory.asKey("GetProductInfoCommand");
    public GetProductInfoCommand(Long productId){
        // command，都可以设置一个自己的名称，同时可以设置一个自己的组
        // 建服务独有的key并且保存到HystrixCommandGroupKey的InternMap
        // command threadpool -> command group -> command key
        // command group，代表了某一个底层的依赖服务，合理，一个依赖服务可能会暴露出来多个接口，每个接口就是一个command key
        // command group，在逻辑上去组织起来一堆command key的调用，统计信息，成功次数，timeout超时次数，失败次数，可以看到某一个服务整体的一些访问情况
        // command group，一般来说，推荐是根据一个服务去划分出一个线程池，command key默认都是属于同一个线程池的
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GetProductInfoCommand"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetProductInfoPool"))
                .andThreadPoolPropertiesDefaults(HystrixThreadPoolProperties.Setter()
                        // coreSize: 设置线程池的大小，默认是10
                        .withCoreSize(15)
                        // queueSizeRejectionThreshold:控制queue满后reject的threshold，因为maxQueueSize不允许热修改，因此提供这个参数可以热修改，控制队列的最大大小
                        // HystrixCommand在提交到线程池之前，其实会先进入一个队列中，这个队列满了之后，才会reject  默认值是5
                        .withMaxQueueSize(12)
                        .withQueueSizeRejectionThreshold(15))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        // 配置10s内请求数超过30个时熔断器开始生效
                        .withCircuitBreakerRequestVolumeThreshold(30)
                        //  配置错误比例>80%时开始熔断
                        .withCircuitBreakerErrorThresholdPercentage(40)
                        // 熔断器打开到关闭的时间窗长度
                        .withCircuitBreakerSleepWindowInMilliseconds(3000)
                        // 手动设置timeout时长 默认是1000，也就是1000毫秒
                        .withExecutionTimeoutInMilliseconds(500)
                        .withFallbackIsolationSemaphoreMaxConcurrentRequests(30))
        );
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
        System.out.println("调用接口，查询商品数据，productId=" + productId);

        if(productId.equals(-1L)) {
            throw new Exception();
        }
        if(productId.equals(-2L)) {
            Thread.sleep(3000);
        }

        String url = "http://127.0.0.1:8083/getProductInfo?productId=" + productId;
        String response = HttpClientUtils.sendGetRequest(url);
        return JSONObject.parseObject(response, ProductInfo.class);
    }

    /**
     * 实现此方法来启用请求缓存
     *  这样的话，每一次请求的结果，都会放在 Hystrix 请求上下文中。下一次同一个 productId 的数据请求，直接取缓存，无须再调用 run() 方法。
     * @return
     */
//    @Override
//    protected String getCacheKey() {
//        return "product_info_" + productId;
//    }

    /**
     * 将某个商品id的缓存清空
     * @param productId 商品id
     */
    public static void flushCache(Long productId) {
        HystrixRequestCache.getInstance(KEY,HystrixConcurrencyStrategyDefault.getInstance()).clear("product_info_" + productId);
    }


    @Override
    protected ProductInfo getFallback() {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setName("降级商品");
        return productInfo;
    }
}
