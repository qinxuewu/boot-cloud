package com.github.modules.allipay.util;

import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 功能描述: 支付宝支付
 * @author: qinxuewu
 * @date: 2019/9/9 15:04
 * @since 1.0.0
 */
public class AliPayHelp {
    private static final Logger log = LoggerFactory.getLogger(AliPayHelp.class);


    private    AliPayHelp(){}


    /**
     * 静态内部类-单例模式
     *       这种方式采用了类装载的机制来保证初始化实例时只有一个线程
     *       静态内部类方式在AliPayHelp类被装载时并不会立即实例化，而是在需要实例化时，调用getInstance方法，才会装载SingleInstance类，从而完成AliPayHelp的实例化
     *       类的静态属性只会在第一次加载类的时候初始化，所以在这里， JVM帮助我们保证了线程的安全性，在类进行初始化时，别的线程是无法进入的
     *
     *     优点：避免了线程不安全，利用静态内部类特点实现延迟加载，效率高
     */
    private  static  class SingleInstance{
        private  static  final  AliPayHelp INTEANCE=new AliPayHelp();
    }
    public  static  AliPayHelp getInstance(){
        return  SingleInstance.INTEANCE;
    }
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
     *      #基本返回参数（建议日志记录保存整个AliPayResult,不定字段太多）
     *      code： 网关返回码 如：20000
     *      msg : 网关描述
     *      sign：签名
     *     #异常情况会返回业务返回码,处理不同的操作
     *      sub_msg：业务返回码描述
     *      sub_code： 业务返回码  详情见 com.github.alipay.sdk.AliPayCodeEnum 或 官方文档
     *                      ACQ.SYSTEM_ERROR=请立即调用查询订单API，查询当前订单的状态
     *                      ACQ.PAYMENT_AUTH_CODE_INVALID=用户刷新条码后，重新扫码发起请求
     *
     *
     * @link 详细文档：https://docs.open.alipay.com/api_1/alipay.trade.pay
     */
    public JSONObject tradePay(TradePayBizContentRequest bizContent, String notifyUrl) {
        JSONObject jsonObject=new JSONObject();
        try {
            String parameter=new ObjectMapper().writeValueAsString(bizContent);
            AlipayTradePayRequest request = new AlipayTradePayRequest();
            request.setNotifyUrl(notifyUrl);
            request.setBizContent(parameter);
            log.info("【支付宝条码支付入参】request={}",parameter);
            AlipayTradePayResponse response = AliPayConfig.alipayClient.execute(request);
            log.info("【支付宝条码支付返回结果】response={}",response.getBody());
            return  JSONObject.parseObject(response.getBody()).getJSONObject(AliPayCodeEnum.TRADE_PAY_RES.getName());
        }catch (AlipayApiException | JsonProcessingException e){
            log.error("【条码支付统一收单交易支付接口异常】={},{}",e,bizContent);
            jsonObject.put("code",-1);
            jsonObject.put("msg","系统异常");
            return jsonObject;
        }
    }

    /**
     * 单笔转账到支付宝账户接口
     * @param bizContent  入参
     * @link 详细文档： https://docs.open.alipay.com/api_28/alipay.fund.trans.toaccount.transfer
     * @return
     *    code： 网关返回码
     *          10000 ：接口调用成功
     *          20000 ： 授权权限不足
     *          40001：缺少必选参数
     *          40002： 非法的参数
     *          40004：业务处理失败
     *          40006：权限不足
     *    msg   描述
     *    sign：签名
     *    #异常情况会返回业务返回码,处理不同的操作
     *    sub_code： 业务返回码,
     *    sub_msg：业务返回码描述
     */
    public JSONObject toaccountTransfer(TransferBizContentRequest bizContent) {
        JSONObject jsonObject=new JSONObject();
        try {
            AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();
            String parameter=new ObjectMapper().writeValueAsString(bizContent);
            request.setBizContent(parameter);
            log.info("【单笔转账到支付宝账户入参】request={}",parameter);
            AlipayFundTransToaccountTransferResponse response = AliPayConfig.alipayClient.execute(request);
            return JSONObject.parseObject(response.getBody()).getJSONObject(AliPayCodeEnum.TRANSFER_RES.getName());
        }catch (AlipayApiException | JsonProcessingException e){
            log.error("【单笔转账到支付宝账户接口异常】={},{}",e,bizContent);
            jsonObject.put("code",-1);
            jsonObject.put("msg","系统异常");
            return jsonObject;
        }
    }

    /**
     * 统一收单线下交易查询:该接口提供所有支付宝支付订单的查询，商户可以通过该接口主动查询订单状态，完成下一步的业务逻辑
     * @param outTradeNo 订单支付时传入的商户订单号
     * @link 详细文档： https://docs.open.alipay.com/api_1/alipay.trade.query
     * @return
     *    code： 网关返回码   10000 ：接口调用成功， 40004：业务处理失败
     *    msg   描述
     *    sign：签名
     *    trade_no：支付宝交易号
     *    out_trade_no：商家订单号
     *    buyer_logon_id：买家支付宝账号
     *    trade_status：交易状态：WAIT_BUYER_PAY（交易创建，等待买家付款）、TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）、TRADE_SUCCESS（交易支付成功）、TRADE_FINISHED（交易结束，不可退款）
     *    total_amount：交易的订单金额，单位为元，两位小数。该参数的值为支付时传入的total_amount
     *    .....此处省略几个字段
     *    #异常情况会返回业务返回码,处理不同的操作
     *    sub_code： 业务返回码,
     *    sub_msg：业务返回码描述
     */
    public JSONObject tradeQuery (String outTradeNo) {
        JSONObject jsonObject=new JSONObject();
        try {
            AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
            JSONObject parameter=new JSONObject();
            parameter.put("out_trade_no",outTradeNo);
            request.setBizContent(parameter.toJSONString());
            AlipayTradeQueryResponse response = AliPayConfig.alipayClient.execute(request);
            JSONObject res= JSONObject.parseObject(response.getBody()).getJSONObject(AliPayCodeEnum.TRADE_QUERY_RES.getName());
            log.info("【统一收单线下交易查询返回接口】={}",res);
            return res;
        }catch (Exception e){
             log.error("统一收单线下交易查询异常={},outTradeNo={}",e,outTradeNo);
            jsonObject.put("code",-1);
            jsonObject.put("msg","系统异常");
            return jsonObject;
        }
    }

    /**
     * 统一收单交易撤销接口: 支付交易返回失败或支付系统超时，调用该接口撤销交易
     * @link  文档详情：https://docs.open.alipay.com/api_1/alipay.trade.cancel
     * @param outTradeNo  订单支付时传入的商户订单号
     * @return
     *    code： 网关返回码   10000 ：接口调用成功， 40004：业务处理失败
     *    msg   描述
     *    sign：签名
     *    trade_no：支付宝交易号
     *    out_trade_no：商家订单号
     *    action：本次撤销触发的交易动作,接口调用成功且交易存在时返回。可能的返回值：
     *              close：交易未支付，触发关闭交易动作，无退款；
     *              refund：交易已支付，触发交易退款动作；未返回：未查询到交易，或接口调用失败；
     *    #异常情况会返回业务返回码,处理不同的操作
     *    sub_code： 业务返回码,
     *    sub_msg：业务返回码描述
     */
    public JSONObject tradeCancel (String outTradeNo) {
        JSONObject jsonObject=new JSONObject();
        try {
            AlipayTradeCancelRequest request = new AlipayTradeCancelRequest();
            JSONObject parameter=new JSONObject();
            parameter.put("out_trade_no",outTradeNo);
            request.setBizContent(parameter.toJSONString());
            AlipayTradeCancelResponse response = AliPayConfig.alipayClient.execute(request);
            JSONObject res= JSONObject.parseObject(response.getBody()).getJSONObject(AliPayCodeEnum.CANCEL_RES.getName());
            log.info("【统一收单交易撤销接口返回结果】={}",res);
            return res;
        }catch (AlipayApiException e){
            log.error("【统一收单交易撤销接口异常】={},{}",e,outTradeNo);
            jsonObject.put("code",-1);
            jsonObject.put("msg","系统异常");
            return jsonObject;
    }
    }

    /**
     * 统一收单交易退款接口
     * @link 文档详情：https://docs.open.alipay.com/api_1/alipay.trade.refund
     * @param outTradeNo  订单支付时传入的商户订单号,
     * @param  refundAmount  需要退款的金额，该金额不能大于订单金额,单位为元，支持两位小数
     * @return
     *    code： 网关返回码   10000 ：接口调用成功， 40004：业务处理失败
     *    msg   描述
     *    sign：签名
     *    trade_no：支付宝交易号
     *    out_trade_no：商家订单号
     *    buyer_logon_id：用户的登录id
     *    refund_fee：退款总金额
     *    gmt_refund_pay：退款支付时间
     *    #异常情况会返回业务返回码,处理不同的操作
     *    sub_code： 业务返回码,
     *    sub_msg：业务返回码描述
     */
    public JSONObject tradeRefund(String outTradeNo, String refundAmount) {
        JSONObject jsonObject=new JSONObject();
        try {
            AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
            JSONObject parameter=new JSONObject();
            parameter.put("out_trade_no",outTradeNo);
            parameter.put("refund_amount",refundAmount);
            request.setBizContent(parameter.toJSONString());
            AlipayTradeRefundResponse response = AliPayConfig.alipayClient.execute(request);
            JSONObject res= JSONObject.parseObject(response.getBody()).getJSONObject(AliPayCodeEnum.REFUND_RES.getName());
            log.info("【统一收单交易退款接口返回结果】={}",res);
            return res;
        }catch (AlipayApiException e){
            log.error("【统一收单交易退款接口异常】={},outTradeNo={}",e,outTradeNo);
            jsonObject.put("code",-1);
            jsonObject.put("msg","系统异常");
            return jsonObject;
        }
    }


}
