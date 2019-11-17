package com.github.modules.allipay.service;


import com.github.modules.allipay.util.AliPayResponse;
import com.github.modules.allipay.util.TradePayBizContentRequest;
import com.github.modules.allipay.util.TransferBizContentRequest;

/**
 * 功能描述:  支付业务
 * @author: qinxuewu
 * @date: 2019/10/31 9:35
 * @since 1.0.0
 */
public interface PayService {


    /**
     * 支付宝条码支付提交
     * @param bizContent  入参
     * @param notifyUrl  支付宝服务器主动通知商户服务器里指定的页面http/https路径。
     * @return
     */
    AliPayResponse tradePay(TradePayBizContentRequest bizContent, String notifyUrl) ;

    /**
     *  支付宝即时到账
     * @param bizContent  入参
     * @return
     */
    AliPayResponse transfer(TransferBizContentRequest bizContent) ;


    /**
     *  支付宝支付订单状态查询
     * @param outTradeNo  入参
     * @return
     */
    AliPayResponse tradeQuery(String outTradeNo) throws Exception;

    /**
     *  支付宝支付订单撤销
     * @param outTradeNo  入参
     * @return
     */
    AliPayResponse tradeCancel(String outTradeNo);



}
