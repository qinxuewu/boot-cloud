package com.github.modules.allipay.service;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayResponse;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.demo.trade.config.Configs;
import com.alipay.demo.trade.model.GoodsDetail;
import com.alipay.demo.trade.model.builder.AlipayTradePayRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradePrecreateRequestBuilder;
import com.alipay.demo.trade.model.builder.AlipayTradeRefundRequestBuilder;
import com.alipay.demo.trade.model.result.AlipayF2FPayResult;
import com.alipay.demo.trade.model.result.AlipayF2FPrecreateResult;
import com.alipay.demo.trade.model.result.AlipayF2FRefundResult;
import com.alipay.demo.trade.service.AlipayTradeService;
import com.alipay.demo.trade.utils.ZxingUtils;
import com.github.model.Product;
import com.github.modules.allipay.util.AliPayUtil;
import com.github.utils.CommonUtil;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 功能描述: 支付宝支付 基于官方提供的 SDK DEMO
 * @author: qinxuewu
 * @date: 2019/9/9 15:04
 * @since 1.0.0
 */
@Component
public class AliliPayService {
    private static final Logger log = LoggerFactory.getLogger(AliliPayService.class);

    /**
     * 支付回调地址
     */
    @Value("${alipay.notify-url}")
    private String notify_url;

    /**
     *  当面付2.0预下单(生成二维码)
     * @param product 订单基本信息
     * @param  operatorId (可选)商户操作员编号，添加此参数可以为商户操作员做销售统计
     * @param  storeId  (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持 例如：test_store_id
     */
    public int aliPayTradePrecreate(Product product,String operatorId,String storeId) {
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        String outTradeNo = product.getOutTradeNo();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = product.getSubject();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        // 如果同时传入了【打折金额】,【不可打折金额】,【订单总金额】三者,则必须满足如下条件:【订单总金额】=【打折金额】+【不可打折金额】
        String totalAmount = CommonUtil.divide(product.getTotalFee(), "100").toString();;

        // (可选) 订单不可打折金额，可以配合商家平台配置折扣活动
        String undiscountableAmount = "0";

        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品2件共15.00元"
        String body =  product.getBody();;


        // 支付超时，定义为120分钟
        String timeoutExpress = "120m";

        // (可选)商品明细列表，需填写购买商品详细信息，
        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
//        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx小面包", 1000, 1);
//        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
//        goodsDetailList.add(goods1);
//        goodsDetailList.add(goods2);

        // 创建扫码支付请求builder，设置请求参数
        AlipayTradePrecreateRequestBuilder builder = new AlipayTradePrecreateRequestBuilder()
                .setSubject(subject).setTotalAmount(totalAmount).setOutTradeNo(outTradeNo)
                .setUndiscountableAmount(undiscountableAmount).setSellerId(sellerId).setBody(body)
                .setOperatorId(operatorId).setStoreId(storeId)
                .setTimeoutExpress(timeoutExpress)
                .setNotifyUrl(notify_url);
//                   .setGoodsDetailList(goodsDetailList);

        AlipayF2FPrecreateResult result = AliPayUtil.getAlipayTradeService().tradePrecreate(builder);
        int status=0;
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝预下单成功: )");
                AlipayTradePrecreateResponse response = result.getResponse();
                dumpResponse(response);
                // 需要修改为运行机器上的路径
                String filePath = String.format("G:\\qr-%s.png", response.getOutTradeNo());
                log.info("filePath:" + filePath);
                ZxingUtils.getQRCodeImge(response.getQrCode(), 256, filePath);
                break;
            case FAILED:
                log.error("支付宝预下单失败!!!");
                status=-1;
                break;
            case UNKNOWN:
                log.error("系统异常，预下单状态未知!!!");
                status=-1;
                break;
            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                status=-1;
                break;
        }
        return  status;
    }




    /**
     * 当面付2.0支付  付款条码，用户支付宝钱包手机app点击“付款”产生的付款条码
     * @param product  订单基本信息
     * @param service
     * @param authCode  (必填) 付款条码，  条码示例，286648048691290423
     * @param operatorId   (可选)商户操作员编号，添加此参数可以为商户操作员做销售统计
     * @return
     */
    public int tradePay(Product product,AlipayTradeService service,String authCode,String operatorId) {
        // (必填) 商户网站订单系统中唯一订单号，64个字符以内，只能包含字母、数字、下划线，
        String outTradeNo = product.getOutTradeNo();

        // (必填) 订单标题，粗略描述用户的支付目的。如“xxx品牌xxx门店当面付扫码消费”
        String subject = product.getSubject();

        // (必填) 订单总金额，单位为元，不能超过1亿元
        String totalAmount =product.getTotalFee();


        // 卖家支付宝账号ID，用于支持一个签约账号下支持打款到不同的收款账号，(打款到sellerId对应的支付宝账号)
        // 如果该字段为空，则默认为与支付宝签约的商户的PID，也就是appid对应的PID
        String sellerId = "";

        // 订单描述，可以对交易或商品进行一个详细地描述，比如填写"购买商品3件共20.00元"
        String body = product.getBody();

        // (必填) 商户门店编号，通过门店号和商家后台可以配置精准到门店的折扣信息，详询支付宝技术支持
        String storeId = "test_store_id";


        // 支付超时，线下扫码交易定义为5分钟
        String timeoutExpress = "5m";

//        // 商品明细列表，需填写购买商品详细信息，
//        List<GoodsDetail> goodsDetailList = new ArrayList<GoodsDetail>();
//        // 创建一个商品信息，参数含义分别为商品id（使用国标）、名称、单价（单位为分）、数量，如果需要添加商品类别，详见GoodsDetail
//        GoodsDetail goods1 = GoodsDetail.newInstance("goods_id001", "xxx面包", 1000, 1);
//        goodsDetailList.add(goods1);
//        GoodsDetail goods2 = GoodsDetail.newInstance("goods_id002", "xxx牙刷", 500, 2);
//        goodsDetailList.add(goods2);

       // (可选)根据真实值填写
        String appAuthToken = "应用授权令牌";

        // 创建条码支付请求builder，设置请求参数
        AlipayTradePayRequestBuilder builder = new AlipayTradePayRequestBuilder()
//                .setAppAuthToken(appAuthToken)
                .setOutTradeNo(outTradeNo).setSubject(subject).setAuthCode(authCode)
                .setTotalAmount(totalAmount).setStoreId(storeId)
                .setBody(body).setOperatorId(operatorId)
                .setSellerId(sellerId);
//                .setGoodsDetailList(goodsDetailList).setTimeoutExpress(timeoutExpress);

        // 调用tradePay方法获取当面付应答
        AlipayF2FPayResult result = service.tradePay(builder);

        int status=0;
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝支付成功: )");
                break;
            case FAILED:
                log.error("支付宝支付失败!!!");
                status=-1;
                break;
            case UNKNOWN:
                log.error("系统异常，订单状态未知!!!");
                status=-1;
                break;
            default:
                log.error("不支持的交易状态，交易返回异常!!!");
                status=-1;
                break;
        }
        return  status;
    }


    /**
     * 退款操作
     * @param product
     * @return
     */
    public int aliRefund(Product product) {
        log.info("订单号：" + product.getOutTradeNo() + "支付宝退款");
        // (必填) 外部订单号，需要退款交易的商户外部订单号
        String outTradeNo = product.getOutTradeNo();
        // (必填) 退款金额，该金额必须小于等于订单的支付金额，单位为元
        String refundAmount = CommonUtil.divide(product.getTotalFee(), "100").toString();

        // (必填) 退款原因，可以说明用户退款原因，方便为商家后台提供统计
        String refundReason = "正常退款，用户买多了";

        // (必填) 商户门店编号，退款情况下可以为商家后台提供退款权限判定和统计等作用，详询支付宝技术支持
        String storeId = "test_store_id";

        // 创建退款请求builder，设置请求参数
        AlipayTradeRefundRequestBuilder builder = new AlipayTradeRefundRequestBuilder()
                .setOutTradeNo(outTradeNo)
                .setRefundAmount(refundAmount)
                .setRefundReason(refundReason)
                //.setOutRequestNo(outRequestNo)
                .setStoreId(storeId);

        AlipayF2FRefundResult result = AliPayUtil.getAlipayTradeService().tradeRefund(builder);
        int status=0;
        switch (result.getTradeStatus()) {
            case SUCCESS:
                log.info("支付宝退款成功: )");
                break;
            case FAILED:
                log.info("支付宝退款失败!!!");
                status=-1;
                break;

            case UNKNOWN:
                log.info("系统异常，订单退款状态未知!!!");
                status=-1;
                break;

            default:
                log.info("不支持的交易状态，交易返回异常!!!");
                status=-1;
                break;
        }
        return status;
    }


    /**
     * 支付宝手机支付下单
     * @param product
     * @return
     */
    public String aliPayMobile(Product product) {
        log.info("支付宝手机支付下单");
        AlipayTradeWapPayRequest alipayRequest = new AlipayTradeWapPayRequest();
        String returnUrl = "回调地址 http 自定义";
        //前台通知
        alipayRequest.setReturnUrl(returnUrl);
        //后台回调
        alipayRequest.setNotifyUrl(notify_url);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", product.getOutTradeNo());
        //订单金额:元
        bizContent.put("total_amount", product.getTotalFee());
        //订单标题
        bizContent.put("subject",product.getSubject());
        //实际收款账号，一般填写商户PID即可
        bizContent.put("seller_id", Configs.getPid());
        //手机网页支付
        bizContent.put("product_code", "QUICK_WAP_PAY");
        bizContent.put("body", "两个苹果五毛钱");
        String biz = bizContent.toString().replaceAll("\"", "'");
        alipayRequest.setBizContent(bizContent.toString().replaceAll("\"", "'"));
        log.info("业务参数:"+alipayRequest.getBizContent());
        String form ="fail";
        try {
            form = AliPayUtil.getAlipayClient().pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            log.error("支付宝构造表单失败",e);
        }
        return form;
    }


    /**
     * 支付宝PC支付下单
     * @param product
     * @return
     */
    public String aliPayPc(Product product) {
        log.info("支付宝PC支付下单");
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        String returnUrl = "前台回调地址 http 自定义";
        //前台通知
        alipayRequest.setReturnUrl(returnUrl);
        //后台回调
        alipayRequest.setNotifyUrl(notify_url);
        JSONObject bizContent = new JSONObject();
        bizContent.put("out_trade_no", product.getOutTradeNo());
        //订单金额:元
        bizContent.put("total_amount", product.getTotalFee());
        //订单标题
        bizContent.put("subject",product.getSubject());
        //实际收款账号，一般填写商户PID即可
        bizContent.put("seller_id", Configs.getPid());
        //电脑网站支付
        bizContent.put("product_code", "FAST_INSTANT_TRADE_PAY");
        bizContent.put("body", "两个苹果五毛钱");
        /**
         * 这里有三种模式可供选择
         * 如果在系统内支付，并且是弹出层支付，建议选择模式二、其他模式会跳出当前iframe(亲测有效)
         */
        bizContent.put("qr_pay_mode", "2");
        String biz = bizContent.toString().replaceAll("\"", "'");
        alipayRequest.setBizContent(biz);
        log.info("业务参数:"+alipayRequest.getBizContent());
        String form = "fail";
        try {
            form = AliPayUtil.getAlipayClient().pageExecute(alipayRequest).getBody();
        } catch (AlipayApiException e) {
            log.error("支付宝构造表单失败",e);
        }
        return form;
    }


    /**
     * 验证签名1
     * @param params
     * @return
     */
    public boolean rsaCheckV1(Map<String, String> params) {
        //验证签名 校验签名
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV1(params, Configs.getAlipayPublicKey(), "UTF-8");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return signVerified;
    }

    /**
     * 验证签名2
     * @param params
     * @return
     */
    public boolean rsaCheckV2(Map<String, String> params) {
        //验证签名 校验签名
        boolean signVerified = false;
        try {
            signVerified = AlipaySignature.rsaCheckV2(params, Configs.getAlipayPublicKey(), "UTF-8");
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        return signVerified;
    }
        /**
         * 简单打印应答
         * @param response
         */
        private void dumpResponse (AlipayResponse response){
            if (response != null) {
                log.info(String.format("code:%s, msg:%s", response.getCode(), response.getMsg()));
                if (StringUtils.isNotEmpty(response.getSubCode())) {
                    log.info(String.format("subCode:%s, subMsg:%s", response.getSubCode(),
                            response.getSubMsg()));
                }
                log.info("body:" + response.getBody());
            }
        }
    }


