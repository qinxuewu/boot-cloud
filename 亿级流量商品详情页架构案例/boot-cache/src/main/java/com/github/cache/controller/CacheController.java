package com.github.cache.controller;
import com.alibaba.fastjson.JSONObject;
import com.github.cache.model.ProductInfo;
import com.github.cache.model.ShopInfo;
import com.github.cache.rebuild.RebuildCacheQueue;
import com.github.cache.service.CacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import javax.annotation.Resource;

/**
 *  缓存Controller
 *
 * @author qinxuewu
 * @create 19/11/3下午12:50
 * @since 1.0.0
 */

@Controller
public class CacheController {

    private static final Logger logger = LoggerFactory.getLogger(CacheController.class);

    @Resource
    private CacheService cacheService;


    /**
     * 测试保存缓存
     * @param productInfo
     * @return
     */
    @RequestMapping("/testPutCache")
    @ResponseBody
    public String testPutCache(ProductInfo productInfo) {
        cacheService.saveLocalCache(productInfo);
        return "success";
    }


    /**
     * 测试获取缓存
     * @param id
     * @return
     */
    @RequestMapping("/testGetCache")
    @ResponseBody
    public ProductInfo testGetCache(Long id) {
        return cacheService.getLocalCache(id);
    }


    /**
     * 提供给nginx获取数据 存储到nginx本地缓存接口
     * @param productId
     * @return
     */
    @RequestMapping("/getProductInfo")
    @ResponseBody
    public ProductInfo getProductInfo(Long productId) {
        ProductInfo productInfo = null;
        productInfo = cacheService.getProductInfoFromReidsCache(productId);
        logger.info("【从redis中获取缓存，商品信】={}",productInfo.toString());

        if(productInfo == null) {
            productInfo = cacheService.getProductInfoFromLocalCache(productId);
            logger.info("【从ehcache中获取缓存，商品信息】={}",productInfo.toString());
        }
        if(productInfo == null) {
            // 就需要从数据源重新拉去数据，重建缓存，
            String productInfoJSON = "{\"id\": 2, \"name\": \"iphone7手机\", \"price\": 5599, \"pictureList\":\"a.jpg,b.jpg\", \"specification\": \"iphone7的规格\", \"service\": \"iphone7的售后服务\", \"color\": \"红色,白色,黑色\", \"size\": \"5.5\", \"shopId\": 1, \"modified_time\": \"2017-01-01 12:01:00\"}";
            productInfo = JSONObject.parseObject(productInfoJSON, ProductInfo.class);
            // 将数据推送到一个内存队列中
            RebuildCacheQueue rebuildCacheQueue = RebuildCacheQueue.getInstance();
            rebuildCacheQueue.putProductInfo(productInfo);
        }
        return productInfo;
    }


    /**
     * 提供给nginx获取数据 存储到nginx本地缓存接口
     * @param shopId
     * @return
     */
    @RequestMapping("/getShopInfo")
    @ResponseBody
    public ShopInfo getShopInfo(Long shopId) {
        ShopInfo shopInfo = null;

        shopInfo = cacheService.getShopInfoFromReidsCache(shopId);
        System.out.println("=================从redis中获取缓存，店铺信息=" + shopInfo);
        if(shopInfo == null) {
            shopInfo = cacheService.getShopInfoFromLocalCache(shopId);
            System.out.println("=================从ehcache中获取缓存，店铺信息=" + shopInfo);
        }
        if(shopInfo == null) {
            // 就需要从数据源重新拉去数据，重建缓存，但是这里先不讲
        }
        return shopInfo;
    }


}
