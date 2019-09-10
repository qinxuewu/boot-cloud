package com.github.modules.allipay.util;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.service.impl.AlipayTradeServiceImpl;

/**
 * 功能描述: 支付宝支付工具类
 * @author: qinxuewu
 * @date: 2019/9/9 14:52
 * @since 1.0.0
 */
public class AliPayUtil {
    /**
     * 签名方式
     */
    public static String SIGN_TYPE = "RSA2";
    /**
     * 参数类型
     */
    public static String PARAM_TYPE = "json";
    /**
     * 编码
     */
    public static String CHARSET = "utf-8";
    private  AliPayUtil(){}

    /**
     * 类级的内部类，也就是静态的成员式内部类，该内部类的实例与外部类的实例
     * 没有绑定关系，而且只有被调用到才会装载，从而实现了延迟加载
     */
    private static class SingletonHolder{
        private  static AlipayClient alipayClient = new DefaultAlipayClient(
                Configs.getOpenApiDomain(), Configs.getAppid(),
                Configs.getPrivateKey(), PARAM_TYPE, CHARSET,
                Configs.getAlipayPublicKey(),"RSA2");

        private  static AlipayTradeService tradeService = new AlipayTradeServiceImpl.ClientBuilder().build();
    }


    /**
     *  支付宝APP请求客户端实例
     * @return
     */
    public static AlipayClient getAlipayClient(){
        return SingletonHolder.alipayClient;
    }

    /**
     *  支付宝当面付2.0服务（集成了交易保障接口逻辑）
     * @return
     */
    public static AlipayTradeService getAlipayTradeService(){
        return SingletonHolder.tradeService;
    }
}
