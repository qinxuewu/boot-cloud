package com.github.cache.cache;

import java.util.HashMap;
import java.util.Map;


/**
 * 功能描述:  本地缓存模拟
 * @author: qinxuewu
 * @date: 2019/11/13 14:21
 * @since 1.0.0
 */
public class LocationCache {

    private static Map<Long, String> cityMap = new HashMap<Long, String>();

    static {
        cityMap.put(1L, "北京");
    }

    public static String getCityName(Long cityId) {
        return cityMap.get(cityId);
    }
}
