package com.github.modules.allipay.util;


/**
 * 功能描述: 响应参数
 * @author: qinxuewu
 * @date: 2019/10/31 11:01
 * @since 1.0.0
 */
public class AliPayResponse {

    /**
     * 网关返回码
     * 支付成功（10000）， 支付失败（40004）,等待用户付款（10003),未知异常（20000）
     */
    private String code;
    /**
     * 网关描述
     */
    private String msg;
    /**
     * 商户订单号
     */
    private String outTradeNo;
    /**
     * 买家支付宝账号   非必选  可为空
     */
    private String buyerLogonId;

    /**
     * 异常返情况下： 才会返回的字段
     */
    private String subCode;
    private String subMsg;


    /************以下字段 调用订单查询接口成功时才会返回**************/
    /**
     * 交易状态：
     *      WAIT_BUYER_PAY （交易创建，等待买家付款）、
     *      TRADE_CLOSED （未付款交易超时关闭，或支付完成后全额退款）、
     *      TRADE_SUCCESS （交易支付成功）、
     *      RADE_FINISHED （交易结束，不可退款）
     */
    private String tradeStatus;

    /************以下字段 调用订单撤销接口成功时才会返回**************/

    /**
     *  本次撤销触发的交易动作,接口调用成功且交易存在时返回。可能的返回值：
     *      close：交易未支付，触发关闭交易动作，无退款；
     *      refund：交易已支付，触发交易退款动作；未返回：未查询到交易，或接口调用失败；
     */
    private String action;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getBuyerLogonId() {
        return buyerLogonId;
    }

    public void setBuyerLogonId(String buyerLogonId) {
        this.buyerLogonId = buyerLogonId;
    }

    public String getSubCode() {
        return subCode;
    }

    public void setSubCode(String subCode) {
        this.subCode = subCode;
    }

    public String getSubMsg() {
        return subMsg;
    }

    public void setSubMsg(String subMsg) {
        this.subMsg = subMsg;
    }

    public String getTradeStatus() {
        return tradeStatus;
    }

    public void setTradeStatus(String tradeStatus) {
        this.tradeStatus = tradeStatus;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return "AliPayResponse{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", subCode='" + subCode + '\'' +
                ", subMsg='" + subMsg + '\'' +
                '}';
    }
}
