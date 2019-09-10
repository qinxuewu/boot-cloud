package com.github.modules.allipay.controller;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.demo.trade.config.Configs;
import com.github.model.Product;
import com.github.modules.allipay.service.AliPayService;
import com.github.modules.allipay.service.AliliPayService;
import com.github.modules.allipay.util.AliPayUtil;
import com.github.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述: 支付宝支付测试控制器
 * @author: qinxuewu
 * @date: 2019/9/9 14:50
 * @since 1.0.0
 */
@Api(tags ="支付宝支付")
@Controller
@RequestMapping(value = "alipay")
public class AliliPayController {
    private static final Logger logger = LoggerFactory.getLogger(AliliPayController.class);
    @Value("${alipay.alipay-public-key}")
    private  String alipayPublicKey;
    @Value("${alipay.sign-type}")
    private  String signType;
    @Autowired
    private AliliPayService aliliPayService;
    @Autowired
    private AliPayService aliPayService;

    /**
     * 当面付2.0预下单(生成二维码)
     * @return
     */
    @ApiOperation(value = "当面付2.0预下单(生成二维码)", notes = "当面付2.0预下单(生成二维码)")
    @GetMapping("/api/tradePrecreate")
    public R tradePrecreate() {
        Product product=new Product();
        String outTradeNo = "tradeprecreate" + System.currentTimeMillis() + (long) (Math.random() * 10000000L);
        product.setOutTradeNo(outTradeNo);
        product.setSubject("xxx品牌xxx门店当面付扫码消费");
        product.setTotalFee("0.01");
        product.setBody("购买商品3件共20.00元");
        int status=aliliPayService.aliPayTradePrecreate(product,null,"test_operator_id");
        return R.ok().put("status",status);
    }
    /**
     * 当面付2.0预下单(条码支付)
     * @return
     */
    @ApiOperation(value = "当面付2.0预下单(条码支付)", notes = "当面付2.0预下单(条码支付)")
    @PostMapping("/api/tradePay")
    public R tradePay(@RequestParam(value = "authCode", required = true) String authCode) {
        Product product=new Product();
        String outTradeNo = "tradeprecreate" + System.currentTimeMillis() + (long) (Math.random() * 10000000L);
        product.setOutTradeNo(outTradeNo);
        product.setSubject("xxx品牌xxx门店当面付扫码消费");
        product.setTotalFee("0.01");
        product.setBody("购买商品3件共20.00元");
        int status=aliliPayService.tradePay(product, AliPayUtil.getAlipayTradeService(),authCode,null);
        return R.ok().put("status",status);
    }


    //---------------------------------------------分隔线 以下不使用用支付宝提供的SDK demo------------------------------------


    /**
     * 统一收单交易支付接口（条码支付）
     * @return
     */
    @ApiOperation(value = "统一收单交易支付接口（条码支付）", notes = "统一收单交易支付接口（条码支付）")
    @PostMapping("/api/tradeAliPay")
    public R tradeAliPay(@RequestParam(value = "authCode", required = true) String authCode) {
         JSONObject parameter=new JSONObject();
         String outTradeNo = "tradeprecreate" + System.currentTimeMillis() + (long) (Math.random() * 10000000L);
         // 商户订单号，需要保证不重
         parameter.put("out_trade_no",outTradeNo);
         // 条码支付固定传入 bar_code
         parameter.put("scene","bar_code");
         // 即用户在支付宝客户端内出示的付款码，使用一次即失效，需要刷新后再去付款
         parameter.put("auth_code",authCode);
         // 商品描述
         parameter.put("subject","xxx品牌xxx门店当面付扫码消");
         // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
         parameter.put("store_id","test_store_id");
        // 支付超时，线下扫码交易定义为5分钟
         parameter.put("timeout_express","5m");
         // (必填) 订单总金额，单位为元，不能超过1亿元
         parameter.put("total_amount","0.01");
         return aliPayService.tradePay(parameter);
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
            if (!Configs.getAppid().equals(params.get("app_id"))) {
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

}
