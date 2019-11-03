package com.github.inventory.request;

/**
 * 功能描述: 请求接口
 * @author: qinxuewu
 * @date: 2019/11/1 10:04
 * @since 1.0.0
 */
public interface Request {

    /**
     * 处理请求
     */
    void  process();

    /**
     * 获取商品ID
     * @return
     */
    Integer getProductId();

    /**
     * 是否强制刷新
     * @return
     */
    boolean isForceRefresh();
}
