//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.github.binarywang.wxpay.util;

import com.github.binarywang.wxpay.bean.request.BaseWxPayRequest;
import com.github.binarywang.wxpay.bean.result.BaseWxPayResult;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SignUtils {
    private static final Logger log = LoggerFactory.getLogger(SignUtils.class);

    public SignUtils() {
    }

    /** @deprecated */
    @Deprecated
    public static String createSign(Object xmlBean, String signKey) {
        return createSign(xmlBean2Map(xmlBean), signKey);
    }

    /** @deprecated */
    @Deprecated
    public static String createSign(Map<String, String> params, String signKey) {
        return createSign((Map)params, (String)null, signKey, new String[0]);
    }

    public static String createSign(Object xmlBean, String signType, String signKey, String[] ignoredParams) {
        return createSign(xmlBean2Map(xmlBean), signType, signKey, ignoredParams);
    }

    public static String createSign(Map<String, String> params, String signType, String signKey, String[] ignoredParams) {
        SortedMap<String, String> sortedMap = new TreeMap(params);
        StringBuilder toSign = new StringBuilder();
        Iterator var6 = sortedMap.keySet().iterator();

        while(var6.hasNext()) {
            String key = (String)var6.next();
            String value = (String)params.get(key);
            boolean shouldSign = false;
            if(StringUtils.isNotEmpty(value) && !ArrayUtils.contains(ignoredParams, key) && !Lists.newArrayList(new String[]{"sign", "key", "xmlString", "xmlDoc", "couponList"}).contains(key)) {
                shouldSign = true;
            }

            if(shouldSign) {
                toSign.append(key).append("=").append(value).append("&");
            }
        }

        toSign.append("key=").append(signKey);
        if("HMAC-SHA256".equals(signType)) {
            return me.chanjar.weixin.common.util.SignUtils.createHmacSha256Sign(toSign.toString(), signKey);
        } else {
            return DigestUtils.md5Hex(toSign.toString()).toUpperCase();
        }
    }

    public static boolean checkSign(Object xmlBean, String signType, String signKey) {
        return checkSign(xmlBean2Map(xmlBean), signType, signKey);
    }

    public static boolean checkSign(Map<String, String> params, String signType, String signKey) {
        String sign = createSign(params, signType, signKey, new String[0]);
        return sign.equals(params.get("sign"));
    }

    public static Map<String, String> xmlBean2Map(Object bean) {
        Map<String, String> result = Maps.newHashMap();
        List<Field> fields = new ArrayList(Arrays.asList(bean.getClass().getDeclaredFields()));
        fields.addAll(Arrays.asList(bean.getClass().getSuperclass().getDeclaredFields()));
        if(bean.getClass().getSuperclass().getSuperclass() == BaseWxPayRequest.class) {
            fields.addAll(Arrays.asList(BaseWxPayRequest.class.getDeclaredFields()));
        }

        if(bean.getClass().getSuperclass().getSuperclass() == BaseWxPayResult.class) {
            fields.addAll(Arrays.asList(BaseWxPayResult.class.getDeclaredFields()));
        }

        Iterator var3 = fields.iterator();

        while(var3.hasNext()) {
            Field field = (Field)var3.next();

            try {
                boolean isAccessible = field.isAccessible();
                field.setAccessible(true);
                if(field.get(bean) == null) {
                    field.setAccessible(isAccessible);
                } else {
                    if(field.isAnnotationPresent(XStreamAlias.class)) {
                        result.put(((XStreamAlias)field.getAnnotation(XStreamAlias.class)).value(), field.get(bean).toString());
                    } else if(!Modifier.isStatic(field.getModifiers())) {
                        result.put(field.getName(), field.get(bean).toString());
                    }

                    field.setAccessible(isAccessible);
                }
            } catch (IllegalArgumentException | IllegalAccessException | SecurityException var6) {
                log.error(var6.getMessage(), var6);
            }
        }

        return result;
    }
}
