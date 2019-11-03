package com.github.cache.controller;

import com.github.cache.model.ProductInfo;
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
}
