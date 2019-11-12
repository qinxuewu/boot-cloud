package com.github.cache.controller;
import com.github.cache.http.HttpClientUtils;
import com.github.cache.hystrix.GetProductInfoCommand;
import com.github.cache.hystrix.GetProductInfosCommand;
import com.github.cache.model.ProductInfo;
import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixObservableCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import rx.Observable;
import rx.Observer;

import java.util.concurrent.Future;

/**
 * 功能描述:  缓存服务的接口
 * @author: qinxuewu
 * @date: 2019/11/12 11:33
 * @since 1.0.0 
 */

@Controller
public class CacheController {
    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    @RequestMapping("/change/product")
    @ResponseBody
    public String changeProduct(Long productId) {
        // 拿到一个商品id
        // 调用商品服务的接口，获取商品id对应的商品的最新数据
        // 用HttpClient去调用商品服务的http接口
        String url = "http://127.0.0.1:8083/getProductInfo?productId=" + productId;
        String response = HttpClientUtils.sendGetRequest(url);
        logger.info("【调用商品服务的接口，获取商品id对应的商品的最新数据....】productId={},response={}",response);
        return "success";
    }


    /**
     * 假设服务多级缓存机制失效了 比如redis缓存,本地缓存nginx缓存都没了
     * 前端流量直接请求缓存服务要拉去最原始的数据
     * @param productId
     * @return
     */
    @RequestMapping("/getProductInfo")
    @ResponseBody
    public String getProductInfo(Long productId){
        // 拿到一个商品id
        // 调用商品服务的接口，获取商品id对应的商品的最新数据
        // 用HttpClient去调用商品服务的http接口

        HystrixCommand<ProductInfo> command = new GetProductInfoCommand(productId);
        // 以同步堵塞方式执行
        // hystrix先创建一个新线程运行run()，接着调用程序要在execute()调用处一直堵塞着，直到run()运行完成
        ProductInfo productInfo = command.execute();
        logger.info("【获取HystrixCommand同步阻塞返回的数据productInfo】={}",productInfo);
        return "success";
    }

    @RequestMapping("/getProductInfo2")
    @ResponseBody
    public String getProductInfo2(Long productId){
        // 拿到一个商品id
        // 调用商品服务的接口，获取商品id对应的商品的最新数据
        // 用HttpClient去调用商品服务的http接口

        HystrixCommand<ProductInfo> command = new GetProductInfoCommand(productId);
        // 以异步非堵塞方式执行
        // 一调用queue()就直接返回一个Future对象，同时hystrix创建一个新线程运行run()，调用程序通过Future.get()拿到run()的返回结果，而Future.get()是堵塞执行的
        Future<ProductInfo> future = command.queue();
        logger.info("【HystrixCommand 调用queue后 执行其他的业务操作。。。。。。】");
		try {
			Thread.sleep(1000);
            logger.info("【获取HystrixCommand- future.get() 返回的数据productInfo】={}",future.get());
		} catch (Exception e) {
			e.printStackTrace();
		}
        return "success";
    }


    /**
     * 一次性批量查询多条商品数据的请求
     * @param pids
     * @return
     */
    @RequestMapping("/getProductInfos")
    @ResponseBody
    public String getProductInfosList(String pids){
        HystrixObservableCommand<ProductInfo> command = new GetProductInfosCommand(pids.split(","));
        // observe()：事件注册前执行run()/construct()
        // 1.第一步是事件注册前，先调用observe()自动触发执行run()/construct()
        // 如果继承的是HystrixCommand，hystrix将创建新线程非堵塞执行run()；如果继承的是HystrixObservableCommand，将以调用程序线程堵塞执行construct()
        // 2 第二步是从observe()返回后调用程序调用subscribe()完成事件注册，如果run()/construct()执行成功则触发onNext()和onCompleted()，如果执行异常则触发onError(
        Observable<ProductInfo> observable = command.observe();

        // toObservable()：返回一个 Observable 对象，如果我们订阅这个对象，就会执行 command 并且获取返回结果
//		observable = command.toObservable(); // 还没有执行

        // 等到调用subscribe然后才会执行

        observable.subscribe(new Observer<ProductInfo>() {
            @Override
            public void onCompleted() {
                logger.info("【onCompleted.....】获取完了所有商品数据");
            }
            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }
            @Override
            public void onNext(ProductInfo info) {
                logger.info("【 onNext】={}",info);
            }
        });
        return  "success";
    }
}
