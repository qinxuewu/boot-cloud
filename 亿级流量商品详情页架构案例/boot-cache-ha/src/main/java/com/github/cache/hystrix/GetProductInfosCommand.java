package com.github.cache.hystrix;


import com.alibaba.fastjson.JSONObject;
import com.github.cache.controller.CacheController;
import com.github.cache.http.HttpClientUtils;
import com.github.cache.model.ProductInfo;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixObservableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * 功能描述: 批量查询多个商品数据的command
 * @author: qinxuewu
 * @date: 2019/11/12 14:45
 * @since 1.0.0 
 */
public class GetProductInfosCommand  extends HystrixObservableCommand<ProductInfo> {
    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);
    private String[] productIds;

    public GetProductInfosCommand(String[] productIds) {
        super(HystrixCommandGroupKey.Factory.asKey("GetProductInfoGroup"));
        this.productIds = productIds;
    }

    @Override
    protected Observable<ProductInfo> construct() {
        return Observable.create(new Observable.OnSubscribe<ProductInfo>() {
            @Override
            public void call(Subscriber<? super ProductInfo> observer) {
                try {

                    logger.info("【自定义HystrixObservableCommand批量获取商品信息的Command......】");
                    for(String productId : productIds) {
                        String url = "http://127.0.0.1:8083/getProductInfo?productId=" + productId;
                        String response = HttpClientUtils.sendGetRequest(url);
                        ProductInfo productInfo = JSONObject.parseObject(response, ProductInfo.class);
                        observer.onNext(productInfo);
                    }
                    // 聚合完了所有的查询请求
                    observer.onCompleted();
                } catch (Exception e) {
                    observer.onError(e);
                }
            }

        }).subscribeOn(Schedulers.io());
    }

}
