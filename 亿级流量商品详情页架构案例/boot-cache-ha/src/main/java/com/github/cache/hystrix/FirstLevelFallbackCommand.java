package com.github.cache.hystrix;


import com.alibaba.fastjson.JSONObject;
import com.github.cache.cache.BrandCache;
import com.github.cache.cache.LocationCache;
import com.github.cache.http.HttpClientUtils;
import com.github.cache.model.ProductInfo;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能描述: 多级降级
 *
 *
 第一级降级策略，可以是
 storm，我们之前做storm这块，第一级降级，一般是搞一个storm的备用机房，部署了一套一模一样的拓扑，如果主机房中的storm拓扑挂掉了，备用机房的storm拓扑定顶上
 如果备用机房的storm拓扑也挂了
 第二级降级，可能就降级成用mysql/hbase/redis/es，手工封装的一套，按分钟粒度去统计数据的系统
 第三季降级，离线批处理去做，hdfs+spark，每个小时执行一次数据统计，去降级
 特别复杂，重要的系统，肯定是要搞好几套备用方案的，一个方案死了，立即上第二个方案，而且要尽量做到是自动化的

 * @author: qinxuewu
 * @date: 2019/11/15 17:16
 * @since 1.0.0 
 */
public class FirstLevelFallbackCommand  extends HystrixCommand<ProductInfo> {
    private Long productId;

    public FirstLevelFallbackCommand(Long productId) {
        // 第一级的降级策略，因为这个command是运行在fallback中的
        // 所以至关重要的一点是，在做多级降级的时候，要将降级command的线程池单独做一个出来
        // 如果主流程的command都失败了，可能线程池都已经被占满了
        // 降级command必须用自己的独立的线程池
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("ProductInfoService"))
                .andCommandKey(HystrixCommandKey.Factory.asKey("FirstLevelFallbackCommand"))
                .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey("FirstLevelFallbackPool"))
        );
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
        // 这里，因为是第一级降级的策略，所以说呢，其实是要从备用机房的机器去调用接口
        // 但是，我们这里没有所谓的备用机房，所以说还是调用同一个服务来模拟
        if(productId.equals(-2L)) {
            throw new Exception();
        }
        String url = "http://127.0.0.1:8082/getProductInfo?productId=" + productId;
        String response = HttpClientUtils.sendGetRequest(url);
        return JSONObject.parseObject(response, ProductInfo.class);
    }

    @Override
    protected ProductInfo getFallback() {
        // 第二级降级策略，第一级降级策略，都失败了
        ProductInfo productInfo = new ProductInfo();
        // 从请求参数中获取到的唯一条数据
        productInfo.setId(productId);
        // 从本地缓存中获取一些数据
        productInfo.setBrandId(BrandCache.getBrandId(productId));
        productInfo.setBrandName(BrandCache.getBrandName(productInfo.getBrandId()));
        productInfo.setCityId(LocationCache.getCityId(productId));
        productInfo.setCityName(LocationCache.getCityName(productInfo.getCityId()));
        // 手动填充一些默认的数据
        productInfo.setColor("默认颜色");
        productInfo.setModifiedTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        productInfo.setName("默认商品");
        productInfo.setPictureList("default.jpg");
        productInfo.setPrice(0.0);
        productInfo.setService("默认售后服务");
        productInfo.setShopId(-1L);
        productInfo.setSize("默认大小");
        productInfo.setSpecification("默认规格");
        return productInfo;
    }
}
