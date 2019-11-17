package com.github.modules.allipay.service;

import com.alibaba.fastjson.JSONObject;

import com.alipay.api.AlipayClient;
import com.github.modules.allipay.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.CompletableFuture;


/**
 * 功能描述: 支付业务
 * @author: qinxuewu
 * @date: 2019/10/31 9:35
 * @since 1.0.0
 */
@Service("payService")
public class PayServiceImpl  implements  PayService {

    private static final Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);



    @Override
    public AliPayResponse tradePay(TradePayBizContentRequest bizContent, String notifyUrl) {

        AliPayResponse response=new AliPayResponse();
        JSONObject info= AliPayHelp.getInstance().tradePay(bizContent,notifyUrl);
        response.setCode(info.getString(AliPayCodeEnum.CODE.getName()));
        response.setMsg(info.getString(AliPayCodeEnum.MSG.getName()));
        if(info.containsKey(AliPayCodeEnum.SUB_CODE.getName())){
            response.setSubCode(info.getString(AliPayCodeEnum.SUB_CODE.getName()));
            response.setSubMsg(info.getString(AliPayCodeEnum.SUB_MSG.getName()));
        }else{
            response.setBuyerLogonId(info.getString(AliPayCodeEnum.BUYER_LOGON_ID.getName()));
            response.setOutTradeNo(info.getString(AliPayCodeEnum.OUT_TRADE_NO.getName()));
        }
        return response;
    }

    @Override
    public AliPayResponse transfer(TransferBizContentRequest bizContent)  {


        AliPayResponse response=new AliPayResponse();
        JSONObject info=AliPayHelp.getInstance().toaccountTransfer(bizContent);
        response.setCode(info.getString(AliPayCodeEnum.CODE.getName()));
        response.setMsg(info.getString(AliPayCodeEnum.MSG.getName()));
        if(info.containsKey(AliPayCodeEnum.SUB_CODE.getName())){
            response.setSubCode(info.getString(AliPayCodeEnum.SUB_CODE.getName()));
            response.setSubMsg(info.getString(AliPayCodeEnum.SUB_MSG.getName()));
        }else{
            response.setOutTradeNo(info.getString(AliPayCodeEnum.OUT_BIZ_NO.getName()));
        }

        return response;
    }

    @Override
    public AliPayResponse tradeQuery(String outTradeNo) {
        AliPayResponse response=new AliPayResponse();
        JSONObject info=AliPayHelp.getInstance().tradeQuery(outTradeNo);
        response.setCode(info.getString(AliPayCodeEnum.CODE.getName()));
        response.setMsg(info.getString(AliPayCodeEnum.MSG.getName()));
        if(info.containsKey(AliPayCodeEnum.SUB_CODE.getName())){
            response.setSubCode(info.getString(AliPayCodeEnum.SUB_CODE.getName()));
            response.setSubMsg(info.getString(AliPayCodeEnum.SUB_MSG.getName()));
        }else{
            response.setBuyerLogonId(info.getString(AliPayCodeEnum.BUYER_LOGON_ID.getName()));
            response.setOutTradeNo(info.getString(AliPayCodeEnum.OUT_BIZ_NO.getName()));
            response.setTradeStatus(info.getString(AliPayCodeEnum.TRADE_STATUS.getName()));
        }
        return response;
    }

    @Override
    public AliPayResponse tradeCancel(String outTradeNo) {
        AliPayResponse response=new AliPayResponse();
        JSONObject info=AliPayHelp.getInstance().tradeCancel(outTradeNo);
        response.setCode(info.getString(AliPayCodeEnum.CODE.getName()));
        response.setMsg(info.getString(AliPayCodeEnum.MSG.getName()));
        if(info.containsKey(AliPayCodeEnum.SUB_CODE.getName())){
            response.setSubCode(info.getString(AliPayCodeEnum.SUB_CODE.getName()));
            response.setSubMsg(info.getString(AliPayCodeEnum.SUB_MSG.getName()));
        }else{
            response.setOutTradeNo(info.getString(AliPayCodeEnum.OUT_BIZ_NO.getName()));
            response.setAction(info.getString(AliPayCodeEnum.ACTION.getName()));
        }
        return response;
    }


}
