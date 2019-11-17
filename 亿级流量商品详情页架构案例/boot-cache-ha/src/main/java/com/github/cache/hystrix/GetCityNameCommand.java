package com.github.cache.hystrix;


import com.github.cache.cache.LocationCache;
import com.netflix.hystrix.*;

/**
 * 功能描述: 获取城市名称的command 采用信号量隔离
 * @author: qinxuewu
 * @date: 2019/11/13 14:17
 * @since 1.0.0 
 */
public class GetCityNameCommand  extends HystrixCommand<String> {

    private Long cityId;


    public GetCityNameCommand(Long cityId){
        // command，都可以设置一个自己的名称，同时可以设置一个自己的组
        // command threadpool -> command group -> command key
        // command group，代表了某一个底层的依赖服务，合理，一个依赖服务可能会暴露出来多个接口，每个接口就是一个command key
        // command group，在逻辑上去组织起来一堆command key的调用，统计信息，成功次数，timeout超时次数，失败次数，可以看到某一个服务整体的一些访问情况
        // command group，一般来说，推荐是根据一个服务去划分出一个线程池，command key默认都是属于同一个线程池的
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("GetCityNameGroup"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("GetCityNameCommand"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("GetCityNamePool"))
                .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                        .withExecutionIsolationStrategy(HystrixCommandProperties.ExecutionIsolationStrategy.SEMAPHORE)
                        //  设置使用SEMAPHORE隔离策略的时候，允许访问的最大并发量，超过这个最大并发量，请求直接被reject
                        // 默认值是10，设置的小一些，否则因为信号量是基于调用线程去执行command的，而且不能从timeout中抽离，因此一旦设置的太大，而且有延时发生，可能瞬间导致tomcat本身的线程资源本占满
                        .withExecutionIsolationSemaphoreMaxConcurrentRequests(15)));
        this.cityId = cityId;
    }
    @Override
    protected String run() throws Exception {
        return LocationCache.getCityName(cityId);
    }
}
