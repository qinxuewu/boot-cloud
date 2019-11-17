package com.github.modules.allipay.util;


/**
 * 功能描述: 支付宝支付状态码枚举类
 * @author: qinxuewu
 * @date: 2019/10/22 14:11
 * @since 1.0.0
 */
public enum AliPayCodeEnum {
    /** 网关返回码 */
    CODE("code"),
    /** 网关返回码描述 */
    MSG("msg"),
    /** 业务返回码 */
    SUB_CODE("sub_code"),
    /** 业务返回码描述 */
    SUB_MSG("sub_msg"),
    /** 支付宝账号 */
    BUYER_LOGON_ID("buyer_logon_id"),
    /** 商户订单号 */
    OUT_TRADE_NO("out_trade_no"),
    OUT_BIZ_NO("out_biz_no"),
    /** 订单查询状态*/
    TRADE_STATUS("trade_status"),
    /** 订单撤销状态 */
    ACTION("action"),
    /** 接口返回错误 请立即调用查询订单API，查询当前订单的状态 如果多次调用依然报此错误码，请联系支付宝客服 */
    SYSTEM_ERROR("ACQ.SYSTEM_ERROR"),
    TRADE_PAY_RES("alipay_trade_pay_response"),
    TRADE_QUERY_RES("alipay_trade_query_response"),
    TRANSFER_RES("alipay_fund_trans_toaccount_transfer_response"),
    REFUND_RES("alipay_trade_refund_response"),
    CANCEL_RES("alipay_trade_cancel_response"),
    /** 支付宝账号对应的支付宝唯一用户号。以2088开头的16位纯数字组成。 */
    ALIPAY_USERID("ALIPAY_USERID"),
    /** 支付宝登录号，支持邮箱和手机号格式。。 */
    ALIPAY_LOGONID("ALIPAY_LOGONID")

    ;

    private String name;
    AliPayCodeEnum(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }


}
