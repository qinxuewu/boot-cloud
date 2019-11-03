package com.github.modules.allipay.service;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.github.modules.allipay.util.TradePayBizContentRequest;

import com.github.utils.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 功能描述: 支付宝支付 版本2 使用官提供的SDK  不使用SDK DEMO
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
     * @param bizContent 入参
     * @return  可能有四种状态：
     *            支付成功（10000）
     *            支付失败（40004）
     *            等待用户付款（10003）, 此刻需要发起轮询流程：等待 5 秒后调用交易查询接口，
     *                       如果仍然返回等待用户付款，在次等待5秒后继续查询，直到返回确切的支付结果（成功 TRADE_SUCCESS 或 已撤销关闭TRADE_CLOSED）
     *                       或是超出轮询时间。在最后一次查询仍然返回等待用户付款的情况下，必须立即调用交易撤销接口
     *            未知异常（20000）,调用查询接口确认支付结果
     *
     *    code： 网关返回码 如：10000,msg： 网关返回码描述
     *    msg   描述
     *    sign：签名
     *    sub_code： 业务返回码,
     *    sub_msg：业务返回码描述
     * @link 详细文档：https://docs.open.alipay.com/194/106039/
     */
    public R tradePay(TradePayBizContentRequest bizContent) {
        try {
            //创建API对应的request类
            AlipayTradePayRequest request = new AlipayTradePayRequest();
            // 设置回调地址
            request.setNotifyUrl(notifyUrl);
            //设置业务参数
            request.setBizContent(new ObjectMapper().writeValueAsString(bizContent));
            log.info("【条码支付入参】request={}",bizContent);
            // 通过alipayClient调用API，获得对应的response类
            AlipayTradePayResponse response = alipayClient.execute(request);
            log.info("【条码支付返回结果】body={}",response.getBody());
            if(response.isSuccess()){
                return  R.ok().put("data",JSONObject.parseObject(response.getBody()));
            }else{
                return  R.error("fail").put("data",JSONObject.parseObject(response.getBody()));
            }
        }catch (AlipayApiException | JsonProcessingException e){
            log.error("【条码支付接口异常】={},{}",e,bizContent);
            return R.error("条码支付接口异常");
        }
    }

    /**
     * 统一收单线下交易查询:该接口提供所有支付宝支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑
     * @param outTradeNo 请求参数
     *
     * @link https://docs.open.alipay.com/api_1/alipay.trade.query
     * @return
     *
     *    code： 网关返回码 如：10000,msg： 网关返回码描述
     *          10000
     *    msg   描述
     *    sign：签名
     *    sub_code： 业务返回码,
     *    sub_msg：业务返回码描述
     */
    public R tradeQuery (String outTradeNo) {
        try {
            //创建API对应的request类
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            //设置业务参数
            JSONObject parameter=new JSONObject();
            parameter.put("out_trade_no",outTradeNo);
            String [] options={"trade_no","out_trade_no","buyer_logon_id","trade_status","total_amount","buyer_user_name","subject","body"};
            parameter.put("query_options",options);
            request.setBizContent(parameter.toJSONString());
            AlipayTradeQueryResponse response = alipayClient.execute(request);
            JSONObject res=JSONObject.parseObject(response.getBody());
            return R.ok(res);
        }catch (Exception e){
            log.error("统一收单线下交易查询异常={},outTradeNo={}",e,outTradeNo);
        }
        return null;
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

    /**
     * 单笔转账到支付宝账户接口
     * @link  https://docs.open.alipay.com/api_28/alipay.fund.trans.toaccount.transfer
     * @param parameter
     * @return
     */
    public R toaccountTransfer(JSONObject parameter) {
        try {
            //创建API对应的request类
            AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
            //后台回调
            request.setNotifyUrl(notifyUrl);
            //设置业务参数
            request.setBizContent(parameter.toJSONString());
            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
            if(response.isSuccess()){
                log.info("调用成功,{}",response);
            } else {
                log.info("调用失败,{}",response.getBody());
            }
            return  R.ok().put("data",JSONObject.parseObject(response.getBody()));
        }catch (AlipayApiException e){
            log.error("toaccountTransfer={},{}",e,parameter);
            return R.error("单笔转账到支付宝账户接口异常");
        }
    }
}
