package com.github.modules.allipay.controller;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.github.config.AliPayConfig;
import com.github.modules.allipay.service.AliPayService;
import com.github.modules.allipay.util.TradePayBizContentRequest;

import com.github.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.JsonProcessingException;


/**
 * 功能描述: 支付宝支付测试控制器
 * @author: qinxuewu
 * @date: 2019/9/9 14:50
 * @since 1.0.0
 */
@Api(tags ="支付宝支付")
@RestController
@RequestMapping(value = "alipay")
public class AliliPayController {
    private static final Logger logger = LoggerFactory.getLogger(AliliPayController.class);
    @Value("${alipay.alipay-public-key}")
    private  String alipayPublicKey;
    @Value("${alipay.sign-type}")
    private  String signType;
    @Autowired
    private AliPayService aliPayService;
    @Autowired
    private AliPayConfig aliPayConfig;





    /**
     * 统一收单交易支付接口（条码支付）
     * @return
     */
    @ApiOperation(value = "统一收单交易支付接口（条码支付）", notes = "统一收单交易支付接口（条码支付）")
    @PostMapping("/api/tradeAliPay")
    public R tradeAliPay(@RequestParam(value = "authCode", required = true) String authCode) throws JsonProcessingException {
         TradePayBizContentRequest request=new TradePayBizContentRequest();

         String outTradeNo = "tradeprecreate" + System.currentTimeMillis() + (long) (Math.random() * 10000000L);
         // 商户订单号，需要保证不重
        request.setOutTradeNo(outTradeNo);
        // 条码支付固定传入 bar_code
        request.setScene("bar_code");
        // 即用户在支付宝客户端内出示的付款码，使用一次即失效，需要刷新后再去付款
        request.setAuthCode(authCode);
        // 商品描述
        request.setSubject("xxx品牌xxx门店当面付扫码消");
        request.setTotalAmount("0.01");
         return R.ok().put("data",aliPayService.tradePay(request));
    }


    /**
     * 支付宝支付后台回调
     * @param request
     * @param response
     * @return
     */
    @ApiIgnore
    @RequestMapping(value = "/callBack")
    public void callBack(HttpServletRequest request, HttpServletResponse response)  throws IOException {
        logger.info("支付宝后台回调开始...........................");
        String  message = "success";
        Map<String, String> params = new HashMap<String, String>();

        // 获取所有返回参数
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            params.put(parameterName, request.getParameter(parameterName));
        }
        logger.info("回调参数={}",params);

        //验证签名 校验签名
        boolean signVerified = false;
        try {
            //2018/01/26 以后新建应用只支持RSA2签名方式，目前已使用RSA签名方式的应用仍然可以正常调用接口，注意下自己生成密钥的签名算法
            signVerified = AlipaySignature.rsaCheckV1(params, alipayPublicKey, "UTF-8",signType);
            //正式环境
            //signVerified = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "UTF-8");
        } catch (AlipayApiException e) {
            e.printStackTrace();
            message =  "failed";
        }

        if (signVerified) {
            logger.info("支付宝验证签名成功！");
            // 若参数中的appid和填入的appid不相同，则为异常通知
            if (!aliPayConfig.getAppid().equals(params.get("app_id"))) {
                logger.info("与付款时的appid不同，此为异常通知，应忽略！");
                message =  "failed";
            }else{
                String outtradeno = params.get("out_trade_no");
                //在数据库中查找订单号对应的订单，并将其金额与数据库中的金额对比，若对不上，也为异常通知
                String status = params.get("trade_status");
                if ("WAIT_BUYER_PAY".equals(status)) {
                    // 如果状态是正在等待用户付款
                    logger.info(outtradeno + "订单的状态正在等待用户付款");

                } else if ("TRADE_CLOSED".equals(status)) {
                    // 如果状态是未付款交易超时关闭，或支付完成后全额退款
                    logger.info(outtradeno + "订单的状态已经关闭");

                } else if ("TRADE_SUCCESS".equals(status) || "TRADE_FINISHED".equals(status)) {
                     // 如果状态是已经支付成功
                    logger.info("(支付宝订单号:"+outtradeno+"付款成功)");
                } else {
                    //这里 根据实际业务场景 做相应的操作
                }
            }
        } else { // 如果验证签名没有通过
            message =  "failed";
            logger.info("验证签名失败！");
        }
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(message.getBytes());
        out.flush();
        out.close();
    }


    /**
     * 单笔转账到支付宝账户接
     * @return
     */
    @ApiOperation(value = "单笔转账到支付宝账户接口", notes = "单笔转账到支付宝账户接口")
    @PostMapping("/api/toaccountTransfer")
    public R toaccountTransfer(
            @ApiParam(name = "payee_type", value = "收款方账户类型:1、ALIPAY_USERID：支付宝账号对应的支付宝唯一用户号。以2088开头的16位纯数字组成。2、ALIPAY_LOGONID：支付宝登录号，支持邮箱和手机号格式。")
            @RequestParam(required = true, value = "payee_type") String payee_type,
            @ApiParam(name = "payee_account", value = "收款方账户,与payee_type配合使用。付款方和收款方不能是同一个账户。")
            @RequestParam(required = true, value = "payee_account") String payee_account,
            @ApiParam(name = "amount", value = "转账金额，单位：元,只支持2位小数。金额必须大于等于0.1元。最大转账金额以实际签约的限额为准。")
            @RequestParam(required = true, value = "amount") String amount
//            @ApiParam(name = "payer_show_name", value = "付款方姓名,如果该字段不传，则默认显示付款方的支付宝认证姓名或单位名称。")
//            @RequestParam(value = "payer_show_name") String payer_show_name,
//            @ApiParam(name = "payee_real_name", value = "收款方真实姓名（最长支持100个英文/50个汉字）。")
//            @RequestParam(value = "payee_real_name") String payee_real_name,
//            @ApiParam(name = "remark", value = "转账备注（支持200个英文/100个汉字）。当付款方为企业账户，且转账金额达到（大于等于）50000元，remark不能为空")
//            @RequestParam(value = "remark") String remark
    ) {
        JSONObject parameter=new JSONObject();
        String outTradeNo = "toaccountTransfer" + System.currentTimeMillis() + (long) (Math.random() * 10000000L);
        // 商户转账唯一订单号。发起转账来源方定义的转账单据ID，用于将转账回执通知给来源方。
        parameter.put("out_biz_no",outTradeNo);
        parameter.put("payee_type",payee_type);
        parameter.put("payee_account",payee_account);
        parameter.put("amount",amount);
//        parameter.put("payer_show_name",payer_show_name);
//        parameter.put("payee_real_name",payee_real_name);
//        parameter.put("remark",remark);
        logger.info("请求参数:{}",parameter);
        return aliPayService.toaccountTransfer(parameter);

    }

    /**
     * 单笔转账到支付宝账户接
     * @return
     */
    @ApiOperation(value = "统一收单线下交易查询", notes = "单笔转账到支付宝账户接口")
    @PostMapping("/api/tradeQuery")
    public R tradeQuery(@ApiParam(name = "outTradeNo") @RequestParam(required = true, value = "outTradeNo") String outTradeNo) {
        return R.ok(aliPayService.tradeQuery(outTradeNo));
    }



}
