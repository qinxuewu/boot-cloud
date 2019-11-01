package com.github.inventory.model;



 /**
  * 功能描述: 库存数量model
  * @author: qinxuewu
  * @date: 2019/11/1 9:49
  * @since 1.0.0
  */
public class ProductInventory {

	/**
	 * 商品id
	 */
	private Integer productId;
	/**
	 * 库存数量
	 */
	private Long inventoryCnt;
	
	public ProductInventory() {
		
	}
	
	public ProductInventory(Integer productId, Long inventoryCnt) {
		this.productId = productId;
		this.inventoryCnt = inventoryCnt;
	}
	
	public Integer getProductId() {
		return productId;
	}
	public void setProductId(Integer productId) {
		this.productId = productId;
	}
	public Long getInventoryCnt() {
		return inventoryCnt;
	}
	public void setInventoryCnt(Long inventoryCnt) {
		this.inventoryCnt = inventoryCnt;
	}
	
}
