package com.github.modules.allipay.util;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 功能描述: 单笔转账到支付宝账户
 * @author: qinxuewu
 * @date: 2019/10/23 14:05
 * @since 1.0.0 
 */
public class TransferBizContentRequest {

    /**
     *  必选 商户转账唯一订单号
     */
    @JsonProperty("out_biz_no")
    private String outBizNo;

    /**
     *  必选 收款方账户类型。可取值：
     * 1、ALIPAY_USERID：支付宝账号对应的支付宝唯一用户号。以2088开头的16位纯数字组成。
     * 2、ALIPAY_LOGONID：支付宝登录号，支持邮箱和手机号格式。
     */
    @JsonProperty("payee_type")
    private String payeeType;

    /**
     *  必选 	收款方账户。与payee_type配合使用。付款方和收款方不能是同一个账户。
     */
    @JsonProperty("payee_account")
    private String payeeAccount;

    /**
     *  必选  转账金额，单位：元。
     *      只支持2位小数，小数点前最大支持13位，金额必须大于等于0.1元。
     *      最大转账金额以实际签约的限额为准。
     */
    @JsonProperty("amount")
    private String amount;

    /**
     *  可选  付款方姓名（最长支持100个英文/50个汉字）。显示在收款方的账单详情页。如果该字段不传，则默认显示付款方的支付宝认证姓名或单位名称。
     */
    @JsonProperty("payer_show_name")
    private String payerShowName;

    /**
     *  可选  收款方真实姓名（最长支持100个英文/50个汉字）。
     *      如果本参数不为空，则会校验该账户在支付宝登记的实名是否与收款方真实姓名一致。
     */
    @JsonProperty("payee_real_name")
    private String payeeRealName;


    /**
     *  可选  转账备注（支持200个英文/100个汉字）。
     *      当付款方为企业账户，且转账金额达到（大于等于）50000元，remark不能为空。收款方可见，会展示在收款用户的收支详情中。
     */
    @JsonProperty("remark")
    private String remark;

    public String getOutBizNo() {
        return outBizNo;
    }

    public void setOutBizNo(String outBizNo) {
        this.outBizNo = outBizNo;
    }

    public String getPayeeType() {
        return payeeType;
    }

    public void setPayeeType(String payeeType) {
        this.payeeType = payeeType;
    }

    public String getPayeeAccount() {
        return payeeAccount;
    }

    public void setPayeeAccount(String payeeAccount) {
        this.payeeAccount = payeeAccount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getPayerShowName() {
        return payerShowName;
    }

    public void setPayerShowName(String payerShowName) {
        this.payerShowName = payerShowName;
    }

    public String getPayeeRealName() {
        return payeeRealName;
    }

    public void setPayeeRealName(String payeeRealName) {
        this.payeeRealName = payeeRealName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "TransferBizContentRequest{" +
                "outBizNo='" + outBizNo + '\'' +
                ", payeeType='" + payeeType + '\'' +
                ", payeeAccount='" + payeeAccount + '\'' +
                ", amount='" + amount + '\'' +
                ", payerShowName='" + payerShowName + '\'' +
                ", payeeRealName='" + payeeRealName + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }
}
