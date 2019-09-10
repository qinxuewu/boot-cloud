package com.github.modules.allipay.service;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.github.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


/**
 * 功能描述: 支付宝支付 版本2 使用官提供的SDK  不适用SDK DEMO
 * @author: qinxuewu
 * @date: 2019/9/9 15:04
 * @since 1.0.0
 */
@Component
public class AliPayService {
    private static final Logger log = LoggerFactory.getLogger(AliPayService.class);

    /**
     * 支付后宝支付后台回调地址
     */
    @Value("${alipay.notify-url}")
    private String notifyUrl;

    @Autowired
    private AlipayClient alipayClient;


    /**
     * 统一收单交易支付接口（条码支付）
     * 这笔交易可能有四种状态：支付成功（10000），支付失败（40004），等待用户付款（10003）和未知异常（20000）。
     * @param parameter 请求参数
     * @return
     */
    public R tradePay(JSONObject parameter) {
        try {
            //创建API对应的request类
            AlipayTradePayRequest request = new AlipayTradePayRequest();
            // 设置回调地址
            request.setNotifyUrl(notifyUrl);
            //设置业务参数
            request.setBizContent(parameter.toJSONString());
            // 通过alipayClient调用API，获得对应的response类
            AlipayTradePayResponse response = alipayClient.execute(request);
            return  R.ok().put("data",JSONObject.parseObject(response.getBody()));
        }catch (AlipayApiException e){
            log.error("（条码支付）统一收单交易支付接口异常={},{}",e,parameter);
            return R.error("（条码支付）统一收单交易支付接口异常");
        }
    }

    /**
     * 统一收单线下交易查询:该接口提供所有支付宝支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑
     * @param parameter 请求参数
     * @link https://docs.open.alipay.com/api_1/alipay.trade.query
     * @return
     */
    public R tradeQuery (JSONObject parameter) {
        try {
            //创建API对应的request类
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            //设置业务参数
            request.setBizContent(parameter.toJSONString());
            // 通过alipayClient调用API，获得对应的response类
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            return  R.ok().put("data",JSONObject.parseObject(response.getBody()));
        }catch (AlipayApiException e){
            log.error("tradeQuery={},{}",e,parameter);
            return R.error("统一收单线下交易查询接口异常");
        }
    }

    /**
     * 统一收单交易撤销接口: 支付交易返回失败或支付系统超时，调用该接口撤销交易
     * @link  https://docs.open.alipay.com/api_1/alipay.trade.cancel
     * @param parameter
     * @return
     */
    public R tradeCancel (JSONObject parameter) {
        try {
            //创建API对应的request类
            AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
            //设置业务参数
            request.setBizContent(parameter.toJSONString());
            // 通过alipayClient调用API，获得对应的response类
            AlipayTradeCancelResponse response = alipayClient.execute(request);
            return  R.ok().put("data",JSONObject.parseObject(response.getBody()));
        }catch (AlipayApiException e){
            log.error("tradeCancel={},{}",e,parameter);
            return R.error("统一收单交易撤销接口异常");
        }
    }

    /**
     * 统一收单交易退款接口
     * @link https://docs.open.alipay.com/api_1/alipay.trade.refund
     * @param parameter
     * @return
     */
    public R tradeRefund(JSONObject parameter) {
        try {
            //创建API对应的request类
            AlipayTradeRefundRequest  request = new AlipayTradeRefundRequest();
            //设置业务参数
            request.setBizContent(parameter.toJSONString());
            // 通过alipayClient调用API，获得对应的response类
            AlipayTradeRefundResponse response = alipayClient.execute(request);
            return  R.ok().put("data",JSONObject.parseObject(response.getBody()));
        }catch (AlipayApiException e){
            log.error("tradeRefund={},{}",e,parameter);
            return R.error("统一收单交易退款接口异常");
        }
    }

    /**
     * 手机网站支付接口2.0
     * @link https://docs.open.alipay.com/api_1/alipay.trade.wap.pay
     * @param parameter
     * @return
     */
    public R tradeWapPay(JSONObject parameter) {
        try {
            //创建API对应的request类
            AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
            // 前台通知
            request.setReturnUrl("");
            //后台回调
            request.setNotifyUrl(notifyUrl);
            //设置业务参数
            request.setBizContent(parameter.toJSONString());
            // 通过alipayClient调用API，获得对应的response类
            AlipayTradeWapPayResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                log.info("调用成功,{}",parameter);
            } else {
                log.info("调用失败,{}",parameter);
            }
            return  R.ok().put("data",JSONObject.parseObject(response.getBody()));
        }catch (AlipayApiException e){
            log.error("tradeWapPay={},{}",e,parameter);
            return R.error("（条码支付）统一收单交易支付接口异常");
        }
    }


    /**
     * 支付宝PC支付下单
     * @param parameter
     * @return
     */
    public R tradePagePay(JSONObject parameter) {
        try {
            //创建API对应的request类
            AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
            // 前台通知
            request.setReturnUrl("");
            //后台回调
            request.setNotifyUrl(notifyUrl);
            //设置业务参数
            request.setBizContent(parameter.toJSONString());
            // 通过alipayClient调用API，获得对应的response类
            AlipayTradePagePayResponse  response = alipayClient.execute(request);
            if(response.isSuccess()){
                log.info("调用成功,{}",parameter);
            } else {
                log.info("调用失败,{}",parameter);
            }
            return  R.ok().put("data",JSONObject.parseObject(response.getBody()));
        }catch (AlipayApiException e){
            log.error("tradePagePay={},{}",e,parameter);
            return R.error("支付宝PC支付下单接口异常");
        }
    }
}
