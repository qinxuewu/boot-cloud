package com.github.modules.allipay.util;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 功能描述: 统一收单交易支付接口（条码支付）请求
 * @author: qinxuewu
 * @date: 2019/10/23 11:23
 * @since 1.0.0
 */
public class TradePayBizContentRequest {
    /**
     *  必选 商户订单号，需要保证不重
     */
    @JsonProperty("out_trade_no")
    private String outTradeNo;
    /**
     * 必选 条码支付固定传入 bar_code
     */
    @JsonProperty(value = "scene")
    private String scene;
    /**
     *  必选 即用户在支付宝客户端内出示的付款码，使用一次即失效，需要刷新后再去付款
     */
    @JsonProperty("auth_code")
    private String authCode;
    /**
     *  必选 订单标题: xxx品牌xxx门店当面付扫码
     */
    @JsonProperty("subject")
    private String subject;

    /**
     *  订单总金额，单位为元 示范：0.01
     */
    @JsonProperty("total_amount")
    private String totalAmount;
    /**
     * 可选  商户门店编号
     */
    @JsonProperty("store_id")
    private String storeId;
    /**
     * 可选  商户操作员编号
     */
    @JsonProperty("operator_id")
    private String operatorId;
    /**
     * 可选  商户机具终端编号
     */
    @JsonProperty("terminal_id")
    private String terminalId;
    /**
     * 可选  商户机具终端编号
     */
    @JsonProperty("timeout_express")
    private String timeoutExpress;

    public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getOperatorId() {
        return operatorId;
    }

    public void setOperatorId(String operatorId) {
        this.operatorId = operatorId;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getTimeoutExpress() {
        return timeoutExpress;
    }

    public void setTimeoutExpress(String timeoutExpress) {
        this.timeoutExpress = timeoutExpress;
    }

    @Override
    public String toString() {
        return "AliPayTradePayRequest{" +
                "outTradeNo='" + outTradeNo + '\'' +
                ", scene='" + scene + '\'' +
                ", authCode='" + authCode + '\'' +
                ", subject='" + subject + '\'' +
                ", totalAmount='" + totalAmount + '\'' +
                ", storeId='" + storeId + '\'' +
                ", operatorId='" + operatorId + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", timeoutExpress='" + timeoutExpress + '\'' +
                '}';
    }


}
