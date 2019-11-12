package com.github.cache.hystrix;


import com.alibaba.fastjson.JSONObject;
import com.github.cache.controller.CacheController;
import com.github.cache.http.HttpClientUtils;
import com.github.cache.model.ProductInfo;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
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

    public GetProductInfoCommand(Long productId){
        // 建服务独有的key并且保存到HystrixCommandGroupKey的InternMap
        super(HystrixCommandGroupKey.Factory.asKey("GetProductInfoGroup"));
        this.productId = productId;
    }

    @Override
    protected ProductInfo run() throws Exception {
        logger.info("【自定义HystrixCommand获取商品信息的Command......】productId="+productId);
        String url = "http://127.0.0.1:8083/getProductInfo?productId=" + productId;
        String response = HttpClientUtils.sendGetRequest(url);
        return JSONObject.parseObject(response, ProductInfo.class);
    }
}
