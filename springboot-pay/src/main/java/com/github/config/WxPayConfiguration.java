package com.github.config;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 功能描述: 微信支付
 * @author: qinxuewu
 * @date: 2019/9/10 15:02
 * @since 1.0.0
 */
@Configuration
@ConditionalOnClass(WxPayService.class)
public class WxPayConfiguration {

    /**
     * 设置微信公众号或者小程序等的appid
     */
    @Value("${wx.pay.appId}")
    private String appId;
    /**
     * 微信支付商户号
     */
    @Value("${wx.pay.mchId}")
    private String mchId;
    /**
     * 微信支付商户密钥
     */
    @Value("${wx.pay.mchKey}")
    private String mchKey;
    /**
     * 服务商模式下的子商户公众账号ID，普通模式请不要配置，请在配置文件中将对应项删除
     */
    @Value("${wx.pay.subAppId}")
    private String subAppId;
    /**
     * 服务商模式下的子商户号，普通模式请不要配置，最好是请在配置文件中将对应项删除
     */
    @Value("${wx.pay.subMchId}")
    private String subMchId;
    /**
     * apiclient_cert.p12文件的绝对路径，或者如果放在项目中，请以classpath:开头指定
     */
    @Value("${wx.pay.keyPath}")
    private String keyPath;

    @Bean
    @ConditionalOnMissingBean
    public WxPayService wxService() {
        WxPayConfig payConfig = new WxPayConfig();
        payConfig.setAppId(appId);
        payConfig.setMchId(mchId);
        payConfig.setMchKey(mchKey);
//        payConfig.setSubAppId(subAppId);
//        payConfig.setSubMchId(subMchId);
        payConfig.setKeyPath(keyPath);


        WxPayService wxPayService = new WxPayServiceImpl();
        wxPayService.setConfig(payConfig);
        return wxPayService;


    }
}
