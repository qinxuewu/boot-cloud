package com.github.modules.wxpay.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.binarywang.wxpay.bean.entpay.EntPayRequest;
import com.github.binarywang.wxpay.bean.entpay.EntPayResult;
import com.github.binarywang.wxpay.exception.WxPayException;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.util.SignUtils;

import com.github.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * 功能描述: 微信企业付款
 * @author: qinxuewu
 * @date: 2019/9/10 15:18
 * @since 1.0.0
 */

@RestController
@RequestMapping("/test")
public class TestEntPayController {


    private static Logger logger = LoggerFactory.getLogger(TestEntPayController.class);
    @Autowired
    private WxPayService payService;
    @Autowired
    private HttpServletRequest request;
    //NO_CHECK：不校验真实姓名  FORCE_CHECK：强校验真实姓名
    private static final String CHECK_NAME="NO_CHECK";
    //企业付款备注
    private static final String DESC="测试";

    private static  final String SUCCESS="SUCCESS";


    @GetMapping("entPay")
    public R entPay(){
        try {
            EntPayRequest var1 = new EntPayRequest();
            var1.setMchAppid(payService.getConfig().getAppId());//申请商户号的appid或商户号绑定的appid
            var1.setNonceStr(UUID.randomUUID().toString().replaceAll("-","").toUpperCase());
            var1.setPartnerTradeNo("201909011546463547895");//商户订单号，需保持唯一性(只能是字母或者数字，不能包含有其他字符)
            var1.setOpenid("oRADx0HQGnlmuqui7uIeJtAsTxCA");
            var1.setCheckName(CHECK_NAME);
            var1.setAmount(30);//企业付款金额，单位为分
            var1.setDescription(DESC);//企业付款备注
            var1.setSpbillCreateIp("127.0.0.1");//IP地址

            String sign = SignUtils.createSign(var1,"MD5", payService.getConfig().getMchId(),null);
            var1.setSign(sign);//签名

            EntPayResult payResult = payService.getEntPayService().entPay(var1);
            System.out.println("payResult==>"+ JSONObject.toJSONString(payResult));

            return R.ok();

        }catch (WxPayException e){
            System.out.println("info==>"+ e);
            return R.error().put("msg", e.getErrCodeDes());
        }


    }

}
