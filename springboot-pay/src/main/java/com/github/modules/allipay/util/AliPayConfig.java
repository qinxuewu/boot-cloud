package com.github.modules.allipay.util;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 功能描述: 支付宝相关参数初始化
 * @author: qinxuewu
 * @date: 2019/9/10 10:01
 * @since 1.0.0
 */
public class AliPayConfig {
    private static final Logger logger = LoggerFactory.getLogger(AliPayConfig.class);
    public  static  String openApiDomain;
    public  static  String mcloudApiDomain;
    public  static  String pid;
    public  static  String appid;
    public  static  String privateKey;
    public  static  String publicKey;
    public  static  String alipayPublicKey;
    public  static  String signType;
    public  static int maxQueryRetry;
    public  static long queryDuration;
    public  static int maxCancelRetry;
    public  static long cancelDuration;
    public  static long heartbeatDelay ;
    public  static long heartbeatDuration ;
    public  static AlipayClient alipayClient;

    static {
        try {
            Configuration propertie = new PropertiesConfiguration("alipay.properties");
            openApiDomain=propertie.getString("alipay.open-api-domain");
            mcloudApiDomain=propertie.getString("alipay.mcloud-api-domain");
            pid=propertie.getString("alipay.pid");
            appid=propertie.getString("alipay.appid");
            privateKey=propertie.getString("alipay.private-key");
            publicKey=propertie.getString("alipay.public-key");
            alipayPublicKey=propertie.getString("alipay.alipay-public-key");
            signType=propertie.getString("alipay.sign-type");
            maxQueryRetry=propertie.getInt("alipay.max-query-retry");
            queryDuration=propertie.getLong("alipay.query-duration");
            maxCancelRetry=propertie.getInt("alipay.max-cancel-retry");
            cancelDuration=propertie.getLong("alipay.cancel-duration");
            heartbeatDelay=propertie.getLong("alipay.heartbeat-delay");
            heartbeatDuration=propertie.getLong("alipay.heartbeat-duration");
            alipayClient = new DefaultAlipayClient(openApiDomain, appid,privateKey, "json", "utf-8", alipayPublicKey,signType);
            logger.info("【支付宝支付配置初始化............】");
        } catch (ConfigurationException e) {
            logger.info("【支付宝支付配置文件加载异常】={}",e);
        }
    }


}
