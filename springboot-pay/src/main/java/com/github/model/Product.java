package com.github.model;
import java.io.Serializable;

/**
 * 产品订单信息
 * 创建者 科帮网
 * 创建时间	2017年7月27日
 */
public class Product implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * 商品ID
	 */
	private String productId;
	/**
	 * 订单名称
	 */
	private String subject;
	/**
	 * 商品描述
	 */
	private String body;
	/**
	 * 总金额(单位是分
	 */
	private String totalFee;
	/**
	 * 订单号(唯一)
	 */
	private String outTradeNo;
	/**
	 * 发起人IP地址
	 */
	private String spbillCreateIp;
	/**
	 *  附件数据主要用于商户携带订单的自定义数
	 */
	private String attach;
	/**
	 *  支付类型(1:支付宝 2:微信 3:银
	 */
	private Short payType;
	/**
	 *  支付方式 (1：PC,平板 2：手机)
	 */
	private Short payWay;
	/**
	 *  前台回调地址  非扫码支付使用
	 */
	private String frontUrl;
	
	public Product() {
		super();
	}
	public Product(String productId, String subject, String body,
                   String totalFee, String outTradeNo, String spbillCreateIp,
                   String attach, Short payType, Short payWay, String frontUrl) {
		super();
		this.productId = productId;
		this.subject = subject;
		this.body = body;
		this.totalFee = totalFee;
		this.outTradeNo = outTradeNo;
		this.spbillCreateIp = spbillCreateIp;
		this.attach = attach;
		this.payType = payType;
		this.payWay = payWay;
		this.frontUrl = frontUrl;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTotalFee() {
		return totalFee;
	}
	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}
	public String getOutTradeNo() {
		return outTradeNo;
	}
	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}
	public String getSpbillCreateIp() {
		return spbillCreateIp;
	}
	public void setSpbillCreateIp(String spbillCreateIp) {
		this.spbillCreateIp = spbillCreateIp;
	}
	public String getAttach() {
		return attach;
	}
	public void setAttach(String attach) {
		this.attach = attach;
	}
	public Short getPayType() {
		return payType;
	}
	public void setPayType(Short payType) {
		this.payType = payType;
	}
	public Short getPayWay() {
		return payWay;
	}
	public void setPayWay(Short payWay) {
		this.payWay = payWay;
	}
	public String getFrontUrl() {
		return frontUrl;
	}
	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}
}
