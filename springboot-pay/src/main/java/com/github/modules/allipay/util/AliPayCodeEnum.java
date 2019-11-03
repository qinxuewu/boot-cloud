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
    /** 接口返回错误 请立即调用查询订单API，查询当前订单的状态 如果多次调用依然报此错误码，请联系支付宝客服 */
    SYSTEM_ERROR("ACQ.SYSTEM_ERROR"),
    TRADE_PAY_RESPONSE("alipay_trade_pay_response"),
    ;

    private String name;
    AliPayCodeEnum(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }


}
