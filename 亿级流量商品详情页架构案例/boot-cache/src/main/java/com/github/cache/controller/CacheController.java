package com.github.cache.controller;

import com.github.cache.model.ProductInfo;
import com.github.cache.model.ShopInfo;
import com.github.cache.service.CacheService;
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
        System.out.println("=================从redis中获取缓存，商品信息=" + productInfo);
        if(productInfo == null) {
            productInfo = cacheService.getProductInfoFromLocalCache(productId);
            System.out.println("=================从ehcache中获取缓存，商品信息=" + productInfo);
        }
        if(productInfo == null) {
            // 就需要从数据源重新拉去数据，重建缓存，
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
