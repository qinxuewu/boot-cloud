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

    static {
        brandMap.put(1L, "iphone");
    }

    public static String getBrandName(Long brandId) {
        return brandMap.get(brandId);
    }
}
