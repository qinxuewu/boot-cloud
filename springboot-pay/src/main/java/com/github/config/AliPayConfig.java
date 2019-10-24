package com.github.config;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 功能描述: 支付宝相关参数初始化
 * @author: qinxuewu
 * @date: 2019/9/10 10:01
 * @since 1.0.0
 */

@Configuration
public class AliPayConfig {
    /** 支付宝openapi域名 **/
    @Value("${alipay.open-api-domain}")
    private  String openApiDomain;

    /** 支付宝mcloudmonitor域名 **/
    @Value("${alipay.mcloud-api-domain}")
    private  String mcloudApiDomain;

    /** 商户partner id **/
    @Value("${alipay.pid}")
    private  String pid;

    /** 商户应用id **/
    @Value("${alipay.appid}")
    private  String appid;

    /** RSA私钥，用于对商户请求报文加签 **/
    @Value("${alipay.private-key}")
    private  String privateKey;

    /** RSA公钥，仅用于验证开发者网关 **/
    @Value("${alipay.public-key}")
    private  String publicKey;

    /** 支付宝RSA公钥，用于验签支付宝应答 **/
    @Value("${alipay.alipay-public-key}")
    private  String alipayPublicKey;

    /** 签名类型 **/
    @Value("${alipay.sign-type}")
    private  String signType;

    /** 最大查询次数 **/
    @Value("${alipay.max-query-retry}")
    private  int maxQueryRetry;

    /**  查询间隔（毫秒） **/
    @Value("${alipay.query-duration}")
    private  long queryDuration;

    /**  最大撤销次数 **/
    @Value("${alipay.max-cancel-retry}")
    private  int maxCancelRetry;

    /**  撤销间隔（毫秒） **/
    @Value("${alipay.cancel-duration}")
    private  long cancelDuration;

    /** 交易保障线程第一次调度延迟（秒） **/
    @Value("${alipay.heartbeat-delay}")
    private  long heartbeatDelay ;

    /** 交易保障线程调度间隔（秒） **/
    @Value("${alipay.heartbeat-duration}")
    private  long heartbeatDuration ;


    public String getAppid() {
        return appid;
    }

    /**
     * 支付宝客户端
     * @return
     */
    @Bean
    public AlipayClient alipayClient(){
        AlipayClient alipayClient = new DefaultAlipayClient(openApiDomain, appid,privateKey, "json", "utf-8", alipayPublicKey,signType);
        return  alipayClient;
    }
}
