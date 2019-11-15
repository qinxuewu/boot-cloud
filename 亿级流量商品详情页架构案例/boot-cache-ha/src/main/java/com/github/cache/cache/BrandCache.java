package com.github.cache.cache;


import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述: 品牌缓存
 * @author: qinxuewu
 * @date: 2019/11/13 16:52
 * @since 1.0.0
 */
public class BrandCache {
    private static Map<Long, String> brandMap = new HashMap<Long, String>();
    private static Map<Long, Long> productBrandMap = new HashMap<Long, Long>();

    static {
        brandMap.put(1L, "iphone");
        productBrandMap.put(-1L, 1L);
    }

    public static String getBrandName(Long brandId) {
        return brandMap.get(brandId);
    }

    public static Long getBrandId(Long productId) {
        return productBrandMap.get(productId);
    }
}
